package crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import db_interactions.AuthorTable;
import db_interactions.MessageTable;
import db_interactions.ScrumDataBase;
import db_interactions.TopicTable;
import java_objects.ArrayMessages;
import java_objects.ArrayTopics;
import java_objects.Author;
import java_objects.HashSetAuthor;
import java_objects.Message;
import java_objects.Topic;
import some_tools.IOSerialTools;
import some_tools.Tools;

public class CrawlerApp {
	
	ArrayMessages allMessage;
	ArrayTopics allPosts;
	HashSetAuthor allAuthors;
	ScrumDataBase sdb = null;
	
	public CrawlerApp(){
		allMessage = new ArrayMessages();
		allPosts = new ArrayTopics();
		allAuthors = new HashSetAuthor();
	}
	
	public CrawlerApp(ScrumDataBase sdb){
		this();
		this.sdb = sdb;
	}
	
	/**
	 * Method used for the first build of a database, or if it's needed to rebuild it
	 * @param url the site to crawl
	 * @throws IOException
	 */
	public void visitAllPages(String url) throws IOException{
		ScrumForumCrawler sfc = new ScrumForumCrawler(url);
		String nextPageUrl = sfc.getNextPageUrl();
		visitAllTopics(new ScrumPageCrawler(url));
		System.out.println("Page : " + url + " visited");
		if (!nextPageUrl.equals("EOS")){
			visitAllPages(nextPageUrl);
		}
	}
	
	public void updateDatabase(String url) throws IOException{
		ScrumForumCrawler sfc = new ScrumForumCrawler(url);
		String nextPageUrl = sfc.getNextPageUrl();
		boolean goToNextPage = updateTopics(new ScrumPageCrawler(url));
		System.out.println("Go to next page = " + goToNextPage);
		System.out.println("Page : " + url + " visited");
		if (goToNextPage && !nextPageUrl.equals("EOS")){
			updateDatabase(nextPageUrl);
		}
	}
	
	public boolean updateTopics(ScrumPageCrawler spc) throws IOException{
		Elements topicsOnPage = spc.getTopicsElements();
		TopicTable topicTable = new TopicTable(sdb, "topics");
		ScrumMessageCrawler smc;
		TopicElement topicElement;
		String topicUrl;
		MessageElements messageElements;
		Topic topic;
		int topic_id;
		int nbReplies;
		boolean goToNextTopic = true;
		
		while(!(topicsOnPage.isEmpty()) && goToNextTopic){
			topicElement = new TopicElement(topicsOnPage.remove(0));
			if(!topicElement.isStickyTopic()){
				topicUrl = topicElement.getStringUrl();
				topic_id = topicElement.getTopicId();
				nbReplies = topicElement.getTopicNbReplies();
				topic = new Topic(topic_id, topicElement.getTopicTitle(), 
						topicUrl, nbReplies);
				smc = new ScrumMessageCrawler(topicUrl);
				messageElements = new MessageElements(smc.getReplyMessagesNodes(), smc.getTopicMessageNode(), smc.getFkId());
				try {
					if(topicTable.contains(topic)){
						if(topicTable.getNbReplies(topic) != nbReplies){
							topicTable.updateNbReplies(topic_id, nbReplies);
						}
						goToNextTopic = updateMessages(messageElements);
						System.out.println("Topic num: " + topic_id + " is in database");
					}
					else{
						System.out.println("Topic num: " + topic_id + " is not in database");
						this.allPosts.add(topic);
						updateANewTopic(messageElements);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return goToNextTopic;
	}
	
	public void updateANewTopic(MessageElements messagesElements){
		MessageElement messageElement;
		Message message;
		Author author;
		AuthorTable authorTable = new AuthorTable(sdb, "authors");
		int fkId = messagesElements.getFkId();
		int authorID;
		for(int i = 0; i < messagesElements.size(); i++){
			messageElement = new MessageElement(messagesElements.get(i), fkId);
			authorID = messageElement.getAuthorId();
			if(authorTable.contains(authorID) == null){
				allAuthors.add(new Author(messageElement.getAuthor(), authorID));
			}
			message = new Message(messageElement.getDateMessage(), messageElement.getMessage(), fkId, authorID);
			System.out.println("Message from: " + message.getDate_message() + " inserted");
			allMessage.add(message);
		}
	}
	
	public boolean updateMessages(MessageElements messageElements) throws SQLException{
		int fkId = messageElements.getFkId();
		MessageElement messageElement = new MessageElement(messageElements.remove(messageElements.size() - 1), fkId);
		System.out.println(messageElement.getMessage());
		String date = Tools.stringDateToDateTimeSql(messageElement.getDateMessage());
		date += ".0";
		MessageTable messageTable = new MessageTable(sdb, "messages");
		AuthorTable authorTable = new AuthorTable(sdb, "authors");
		String lastMessageDate = messageTable.getLastMessageDate(fkId);
		int comparison = date.compareTo(lastMessageDate);
		
		int authorID;
		Message message;
		
		if(comparison == 0){
			return false;
		}
		while(!(messageElements.size() == 0) && comparison > 0){
			authorID = messageElement.getAuthorId();
			if(authorTable.contains(authorID) == null){
				allAuthors.add(new Author(messageElement.getAuthor(), authorID));
			}
			message = new Message(messageElement.getDateMessage(), messageElement.getMessage(), fkId, authorID);
			System.out.println("Message from: " + message.getDate_message() + " inserted");
			allMessage.add(message);
			messageElement = new MessageElement(messageElements.remove(messageElements.size() - 1), fkId);
			date = Tools.stringDateToDateTimeSql(messageElement.getDateMessage());
			date += ".0";
			comparison = date.compareTo(lastMessageDate);
			
		}
		return true;
	}
	
	/**
	 * Crawl all messages, to build the author and messages tables
	 * @param messageElements an instance of MessageElements
	 */
	public void visitAllMessageElements(MessageElements messageElements){
		MessageElement msg;
		Message message;
		Author author;
		boolean ajout;
		int fkId = messageElements.getFkId();
		int authorID;
		for(int i = 0; i < messageElements.size(); i++){
			msg = new MessageElement(messageElements.get(i), fkId);
			authorID = msg.getAuthorId();
			author = new Author(msg.getAuthor(), authorID);
			ajout = allAuthors.add(author);
			if(!(allAuthors == null) && !ajout){
				author = allAuthors.getAuthor(author);
			}
			message = new Message(msg.getDateMessage(), msg.getMessage(), fkId, authorID);
			allMessage.add(message);
		}
	}
	
	/**
	 * Crawl a forum page to get all topics, authors and messages
	 * @param spc an instance of ScrumPageCrawler
	 * @throws IOException
	 */
	public void visitAllTopics(ScrumPageCrawler spc) throws IOException{
		Elements topicsOnPage = spc.getTopicsElements();
		ScrumMessageCrawler smc;
		TopicElement topic;
		String topicUrl;
		MessageElements messageElements;
		Topic post;
		for(Element e : topicsOnPage){
			topic = new TopicElement(e);
			topicUrl = topic.getStringUrl();
			post = new Topic(topic.getTopicId(), topic.getTopicTitle(), topicUrl, topic.getTopicNbReplies());
			allPosts.add(post);
			smc = new ScrumMessageCrawler(topicUrl);
			messageElements = new MessageElements(smc.getReplyMessagesNodes(), smc.getTopicMessageNode(), smc.getFkId());
			visitAllMessageElements(messageElements);
		}
	}
	
	public void writeJsonFiles() throws IOException{
		JsonArray jsonMessage = allMessage.toJsonArray();
		JsonArray jsonTopics = allPosts.toJsonArray();
		JsonObjectBuilder jsobPosts = Json.createObjectBuilder();
		JsonObjectBuilder jsobMsg = Json.createObjectBuilder();
		JsonObject postsObject;
		JsonObject msgObject;
		OutputStream os_posts;
		OutputStream os_msg;
		JsonWriter postsWriter;
		JsonWriter msgWriter;
		
		jsobPosts.add("posts", jsonTopics);
		jsobMsg.add("messages", jsonMessage);
		postsObject = jsobPosts.build();
		msgObject = jsobMsg.build();
		
		os_posts = new FileOutputStream("E:\\Documents\\cours\\stage\\scrum\\scrumPosts.json");
		postsWriter = Json.createWriter(os_posts);
		postsWriter.writeObject(postsObject);
		postsWriter.close();
		os_posts.close();
		
		os_msg = new FileOutputStream("E:\\Documents\\cours\\stage\\scrum\\scrumMsg.json");
		msgWriter = Json.createWriter(os_msg);
		msgWriter.writeObject(msgObject);
		msgWriter.close();
		os_msg.close();	
	}
	
	public void insertAllMessages(MessageTable messageTable) throws SQLException{
		messageTable.insertAll(this.allMessage);
	}
	
	public void insertAllTopics(TopicTable topicTable) throws SQLException{
		topicTable.insertAll(this.allPosts);
	}
	
	public void insertAllAuthors(AuthorTable authorTable) throws SQLException{
		authorTable.insertAllAuthor(this.allAuthors);
	}
	
	
	public void bddFirstBuild() throws IOException{
		this.visitAllPages("https://www.scrum.org/forum/scrum-forum");
		IOSerialTools.saveTopicsAsObject(allPosts);
		IOSerialTools.saveAuthorsAsObject(allAuthors);
		IOSerialTools.saveMessagesAsObject(allMessage);
		try {
			this.sdb = new ScrumDataBase("jdbc:mysql://localhost/scrumdata?autoReconnect=true&useSSL=false", "root", "");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if(this.sdb != null){
			try {
				this.insertAllTopics(new TopicTable(this.sdb, "topics"));
				this.insertAllAuthors(new AuthorTable(this.sdb, "authors"));
				//you must insert the messages after topics and authors had been inserted, because of the foreign keys constraints
				this.insertAllMessages(new MessageTable(this.sdb, "messages"));
				this.sdb.closeDB();

			} catch (SQLException e) {
				e.getSQLState();
				e.printStackTrace();
			}
		}
	}
	
	public void writeObjectsToDatabase(File ficMessages, File ficTopics, File ficAuthors){
		this.allAuthors = IOSerialTools.readAuthorsObject(ficAuthors);
		this.allMessage = IOSerialTools.readMessagesObject(ficMessages);
		this.allPosts = IOSerialTools.readTopicsObject(ficTopics);
		try {
			this.sdb = new ScrumDataBase("jdbc:mysql://localhost/base_de_test?autoReconnect=true&useSSL=false", "root", "");
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		if(this.sdb != null){
			try {
				this.insertAllTopics(new TopicTable(this.sdb, "topics"));
				this.insertAllAuthors(new AuthorTable(this.sdb, "authors"));
				//you must insert the messages after topics and authors had been inserted, because of the foreign keys constraints
				this.insertAllMessages(new MessageTable(this.sdb, "messages"));
				this.sdb.closeDB();

			} catch (SQLException e) {
				e.getSQLState();
				e.printStackTrace();
			}
		}
	}
	
	public void basicUpdate(){
		try {
			this.updateDatabase("https://www.scrum.org/forum/scrum-forum");
		} catch (IOException e1) {
			System.out.println("Unable to updateDatabase " + e1.getMessage());;
		}
		if(this.sdb != null){
			try {
				this.insertAllTopics(new TopicTable(this.sdb, "topics"));
				this.insertAllAuthors(new AuthorTable(this.sdb, "authors"));
				//you must insert the messages after topics and authors had been inserted, because of the foreign keys constraints
				this.insertAllMessages(new MessageTable(this.sdb, "messages"));
				this.sdb.closeDB();

			} catch (SQLException e) {
				e.getSQLState();
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
		CrawlerApp ca = new CrawlerApp(new ScrumDataBase("jdbc:mysql://localhost/scrumdata?autoReconnect=true&useSSL=false", "root", ""));
		ca.basicUpdate();
		
	}

}
