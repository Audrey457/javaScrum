package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	private Elements messageElements;
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
		this.messageElements = this.getAllMessageElements();
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
	 * It gets all the new messages that belong to an already existing topic.
	 * @param messageTable an instance of MessageTable, the table to update
	 * @return a List/<Message/> (an empty list if the topic do not need to 
	 * be updated)
	 */
	public List<Message> updateAnExistingTopic(MessageTable messageTable){
		ArrayList<Message> messagesList = (ArrayList<Message>) this.getMessagesList();
		ArrayList<Message> messagesListFromDataBase = (ArrayList<Message>) messageTable.getAllMessagesOfATopic(this.getTopicId());
		messagesList.removeAll(messagesListFromDataBase);
		return messagesList;
	}
	
	/**
	 * @return an instance of Elements
	 */
	private Elements getAllMessageElements(){
		Elements messageElements;
		messageElements = this.getReplyMessagesNodes();
		messageElements.add(0, this.getInitialMessageNode());
		return messageElements;
	}
	
	
	/**
	 * Method used to convert an instance of Elements, which represents 
	 * all the messages of a topic in HTML format, to a list of Message 
	 * Crawl a topic page to get all messages
	 * @return an instance of List/<Message/>
	 * @see Message
	 */
	public List<Message> getMessagesList(){
		MessageElement msg;
		Message message;
		int authorID;
		ArrayList<Message> messagesList = new ArrayList<>();
		int fkId = this.getTopicId();
		
		for (int i = 0; i < this.messageElements.size(); i++) {
			msg = new MessageElement(this.messageElements.get(i), fkId);
			authorID = msg.getAuthorId();
			message = new Message(DateTools.stringDateToDateTimeSql(msg.getDateMessage())+".0",
					msg.getMessage(), fkId, authorID);
			messagesList.add(message);
		}
		return messagesList;
	}
	
	/**
	 * Method used when you do not already have a database, 
	 * or when you want to rewrite it, or when you want to insert 
	 * the messages authors of a new topic
	 * Crawl a topic page to get all authors
	 * @return an instance of Set/<Author/>
	 */
	public Set<Author> getAuthorsList() {
		MessageElement msg;
		Author author;
		LinkedHashSet<Author> authorsList = new LinkedHashSet<>();
		int fkId = this.getTopicId();
		
		for (int i = 0; i < this.messageElements.size(); i++) {
			msg = new MessageElement(this.messageElements.get(i), fkId);
			author = new Author(msg.getAuthor(), msg.getAuthorId());
			authorsList.add(author);
		}
		return authorsList;
	}
}
