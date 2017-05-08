package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import databasemodel.MessageTable;
import domainmodel.Author;
import domainmodel.Message;
import sometools.DateTools;

public class TopicCrawler {
	private String topicUrl;
	private Document topicPage;
	private ArrayList<Message> messagesList;
	private LinkedHashSet<Author> authorsList;
	private String initialMessageCssSelector;
	private String replyMessagesCssSelector;
	private final Logger logger = Logger.getLogger(TopicCrawler.class);
	

	/**
	 * Create a TopicCrawler, which get all
	 * informations from a topic web page
	 * @param url an instance of String, the topic url
	 */
	public TopicCrawler(String url){
		this.topicUrl = url;
		this.messagesList = new ArrayList<>();
		this.authorsList = new LinkedHashSet<>();
		this.initialMessageCssSelector = ".forum-node-topic";
		this.replyMessagesCssSelector = ".forum-node-container > .forum-node-reply";
		try{
			this.topicPage = Jsoup.connect(url).get();
		}catch(IOException e){
			logger.fatal(e + "\nCan not connect to: " + url);
		}
	}
	
	/**
	 * The messages list contains all the messages to write, when 
	 * you need to write / rewrite / update the database
	 * @return the messagesList, an instance of ArrayList/<Message/>
	 */
	public List<Message> getMessagesList() {
		return messagesList;
	}
	
	/**
	 * Get the initial message node (the message posted 
	 * by the topic creator)
	 * @return an instance of Element
	 */
	private Element getInitialMessageNode(){
		return this.topicPage.select(this.initialMessageCssSelector).get(0);
	}
	
	/**
	 * Get the reply messages nodes
	 * @return an instance of Elements
	 */
	private Elements getReplyMessagesNodes(){
		return this.topicPage.select(this.replyMessagesCssSelector);
	}
	
	/**
	 * Get the topic id (foreign key)
	 * @return an int
	 */
	private int getTopicId(){
		return Integer.parseInt(this.topicUrl.split("/")[5]);
	}
	
	/**
	 * Method used when you already have a database and you want to
	 * update it.
	 *  It gets all the messages of a new topic.
	 */
	public void getANewTopicMessages(){
		Elements messagesElements;
		MessageElement messageElement;
		Message message;
		int fkId = this.getTopicId();
		int authorID;
		
		messagesElements = this.getReplyMessagesNodes();
		messagesElements.add(0, this.getInitialMessageNode());
		for (int i = 0; i < messagesElements.size(); i++) {
			messageElement = new MessageElement(messagesElements.get(i), fkId);
			authorID = messageElement.getAuthorId();
			this.authorsList.add(new Author(messageElement.getAuthor(), authorID));
			message = new Message(messageElement.getDateMessage(), messageElement.getMessage(), fkId, authorID);
			logger.info("Message from: " + message.getMessageDate() + " inserted");
			this.messagesList.add(message);
		}
	}
	
	/**
	 * Method used when you already have a database and you want to
	 * update it.
	 * It gets all the new messages that belong to an already existing topic.
	 * @param messageTable an instance of MessageTable, the table to update
	 * @return false if the last message date is the same than 
	 * the last message date in the database for this topic,
	 * true otherwise 
	 */
	public boolean updateAnExistingTopic(MessageTable messageTable){
		Elements messageElements;
		int authorID;
		Message message;
		MessageElement messageElement;
		String date;
		int compar;

		int fkId = this.getTopicId();
		String lastMessageDate = messageTable.getLastMessageDate(fkId);
		
		messageElements = this.getReplyMessagesNodes();
		messageElements.add(0, this.getInitialMessageNode());
		messageElement = new MessageElement(messageElements.remove(messageElements.size() - 1), fkId);
		date = DateTools.stringDateToDateTimeSql(messageElement.getDateMessage());
		date += ".0";
		compar = date.compareTo(lastMessageDate);

		if (compar <= 0) {
			return false;
		}
		while (!(messageElements.size() == 0) && compar > 0) {
			authorID = messageElement.getAuthorId();
			this.authorsList.add(new Author(messageElement.getAuthor(), authorID));
			message = new Message(messageElement.getDateMessage(), messageElement.getMessage(), fkId, authorID);
			logger.info("Message from: " + message.getMessageDate() + " inserted");
			this.messagesList.add(message);
			messageElement = new MessageElement(messageElements.remove(messageElements.size() - 1), fkId);
			date = DateTools.stringDateToDateTimeSql(messageElement.getDateMessage());
			date += ".0";
			compar = date.compareTo(lastMessageDate);

		}
		return true;
	}
	
	
	/**
	 * Method used when you do not already have a database, 
	 * or when you want to rewrite it.
	 * Crawl a topic page to get all authors and messages
	 */
	public void getAllMessagesData(){
		Elements messageElements;
		MessageElement msg;
		Message message;
		Author author;
		int authorID;
		int fkId = this.getTopicId();
		
		messageElements = this.getReplyMessagesNodes();
		messageElements.add(0, this.getInitialMessageNode());
		for (int i = 0; i < messageElements.size(); i++) {
			msg = new MessageElement(messageElements.get(i), fkId);
			authorID = msg.getAuthorId();
			author = new Author(msg.getAuthor(), authorID);
			this.authorsList.add(author);
			message = new Message(msg.getDateMessage(), msg.getMessage(), fkId, authorID);
			this.messagesList.add(message);
		}
	}
	
	/**
	 * @return the authorList
	 */
	public Set<Author> getAuthorList() {
		return authorsList;
	}
}
