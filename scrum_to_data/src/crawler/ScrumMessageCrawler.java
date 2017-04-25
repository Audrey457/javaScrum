package crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrumMessageCrawler {
	private String url;
	private Document scrumMessagePage;
	
	public ScrumMessageCrawler(String url) throws IOException{
		this.url = url;
		this.scrumMessagePage = Jsoup.connect(url).get();
	}
	
	/**
	 * Get the topic message node
	 * @return an instance of Element
	 */
	public Element getTopicMessageNode(){
		Element topicMessage = this.scrumMessagePage.select(".forum-node-topic").get(0);
		return topicMessage;
	}
	
	/**
	 * Get the reply messages nodes
	 * @return an instance of Elements
	 */
	public Elements getReplyMessagesNodes(){
		Elements replyMessages = this.scrumMessagePage.select(".forum-node-container > .forum-node-reply");
		return replyMessages;
	}
	
	/**
	 * Get the topic id (foreign key)
	 * @return an int
	 */
	public int getFkId(){
		return Integer.parseInt(this.url.split("/")[5]);
	}
}
