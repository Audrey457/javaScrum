package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import databasemodel.MessageTable;
import databasemodel.TopicTable;
import domainmodel.Author;
import domainmodel.Message;
import domainmodel.Topic;

public class ForumCrawler {
	private Document forumPage;
	private TopicCrawler topicCrawler;
	private String pageUrl;
	private ArrayList<Topic> topicsList;
	private String nextPageCssSelector;
	private String topicElementsCssSelector;
	private String topicsUrlCssSelector;
	private final Logger logger = Logger.getLogger(ForumCrawler.class);


	/**
	 * Basic constructor, can be used only for the scrum forum,
	 * and if there is no changes !
	 * (I could do a config file... Need to see later)
	 * @param pageUrl
	 */
	public ForumCrawler(String pageUrl){
		
		this.pageUrl = pageUrl;
		this.topicsList = new ArrayList<>();
		this.nextPageCssSelector = "li.pager__item.pager__item--next > a";
		this.topicElementsCssSelector = ".forum-list-view-item";
		this.topicsUrlCssSelector = ".forum-list-item-title .forum__title > div > a";
		try{
			this.forumPage = Jsoup.connect(pageUrl).get();
		}catch(IOException e){
			logger.error(e);
		}
	}
	
	/**
	 * @return an ArrayList/<Topic/>, the list of topic to write in the database, 
	 * after an update / first creation / rewriting operation
	 */
	public List<Topic> getTopicsList() {
		return topicsList;
	}
	
	
	/**
	 * @return an instance of String, the URL next page, or "EOS" if the end of the 
	 * site is reached
	 */
	private String getNextPageUrl(){
		Elements nextPageNode = forumPage.select(nextPageCssSelector);
		if(nextPageNode.size() == 0)
			return "EOS";
		return nextPageNode.attr("abs:href");
	}
	
	/**
	 * Method used when you do not already have a database, 
	 * or when you want to rewrite it.
	 * Browse all pages of a forum, and get all the needed data
	 * 
	 */
	public void browseAllPages(){
		String nextPageUrl = this.getNextPageUrl();
		getAllTopicsData();
		logger.info("Page : " + this.pageUrl + " visited");
		if(!"EOS".equals(nextPageUrl)){
			this.pageUrl = nextPageUrl;
			try{
				this.forumPage = Jsoup.connect(this.pageUrl).get();
			}catch(IOException e){
				logger.fatal(e + "can not connect to " + this.pageUrl);
			}
			browseAllPages();
		}
	}
	
	/**
	 * Method used when you already have a database, 
	 * and you want to update it.
	 * Browse only the necessary pages of a forum, and get all the needed data
	 * @param topicTable an instance of TopicTable, the table to update
	 * @see TopicTable
	 */
	public void browsePagesToUpdate(TopicTable topicTable, MessageTable messageTable){
		String nextPageUrl = this.getNextPageUrl();
		boolean goToNextPage = this.updateTopicTable(topicTable, messageTable);
		logger.info("Page : " + this.pageUrl + " visited");
		if (goToNextPage && !"EOS".equals(nextPageUrl)) {
			this.pageUrl = nextPageUrl;
			try{
				this.forumPage = Jsoup.connect(this.pageUrl).get();
			}catch(IOException e){
				logger.fatal(e + "can not connect to " + this.pageUrl);
			}
			browsePagesToUpdate(topicTable, messageTable);
		}
	}
	
	
	/**
	 *The Elements object returned contains all the "topic" elements
	 * @return an instance of Elements
	 */
	private Elements getTopicsElements(){
		return this.forumPage.select(this.topicElementsCssSelector);
	}
	
	/**
	 * Get all topics urls from a forum page
	 * The CSS selector must be from a topic Element, not from the 
	 * page itself
	 * @return an ArrayList/<String/>
	 */
	public List<String> getAllTopicsUrl(){
		ArrayList<String> topicsUrls = new ArrayList<>();
		Elements topicsOnPageUrls = this.getTopicsElements().select(this.topicsUrlCssSelector);
		for(Element e : topicsOnPageUrls){
			topicsUrls.add(e.attr("abs:href"));
		}
		return topicsUrls;
	}
	
	/**
	 * Method used when you do not already have a database, 
	 * or when you want to rewrite it.
	 * Crawl a forum page to get all topics, authors and messages
	 */
	private void getAllTopicsData(){
		Elements topicsOnPage = this.getTopicsElements();
		TopicElement topicElement;
		String topicUrl;
		Topic topic;
		for (Element e : topicsOnPage) {
			topicElement = new TopicElement(e);
			topicUrl = topicElement.getStringUrl();
			topic = new Topic(topicElement.getTopicId(), topicElement.getTopicTitle(), topicUrl, topicElement.getTopicNbReplies());
			topicsList.add(topic);
			this.topicCrawler = new TopicCrawler(topicUrl);
			this.topicCrawler.getAllMessagesData();
		}
	}
	
	/**
	 * Method used when you already have a database, 
	 * and you want to update it.
	 * @return true when the last topic in a page needed to be updated (if it's a new topic, or if the topic already 
	 * exists in the database, but has one more recent message at least), false otherwise
	 */
	private boolean updateTopicTable(TopicTable topicTable, MessageTable messageTable){
		Elements topicsOnPage = getTopicsElements();
		TopicElement topicElement;
		boolean goToNextTopic = true;

		while (!(topicsOnPage.isEmpty()) && goToNextTopic) {
			topicElement = new TopicElement(topicsOnPage.remove(0));
			goToNextTopic = updateTopicStrategy(topicElement, topicTable, messageTable);
		}
		return goToNextTopic;
	}
	
	private boolean updateTopicStrategy(TopicElement topicElement, TopicTable topicTable, MessageTable messageTable){
		String topicUrl;
		Topic topic;
		int topicId;
		int nbReplies;
		boolean goToNextTopic = true;
		if (!topicElement.isStickyTopic()) {
			topicUrl = topicElement.getStringUrl();
			topicId = topicElement.getTopicId();
			nbReplies = topicElement.getTopicNbReplies();
			topic = new Topic(topicId, topicElement.getTopicTitle(), topicUrl, nbReplies);
			this.topicCrawler = new TopicCrawler(topicUrl);
			if (topicTable.contains(topic)) {
				if (topicTable.getNbReplies(topic) != nbReplies) {
					topicTable.updateNbReplies(topicId, nbReplies);
				}
				logger.info("Topic num: " + topicId + " is in database");
				goToNextTopic = topicCrawler.updateAnExistingTopic(messageTable);
			} else {
				logger.info("Topic num: " + topicId + " is not in database");
				this.topicsList.add(topic);
				this.topicCrawler.getANewTopicMessages();
			}
		}
			return goToNextTopic;
	}
	
	public List<Message> getMessagesList() {
		return this.topicCrawler.getMessagesList();
	}
	
	public Set<Author> getAuthorsList(){
		return this.topicCrawler.getAuthorList();
	}
}
