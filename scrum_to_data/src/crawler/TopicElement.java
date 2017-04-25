package crawler;

import org.jsoup.nodes.Element;

public class TopicElement {
	Element topicElement;

	public TopicElement(Element topic) {
		super();
		this.topicElement = topic;
	}
	
	public String toString(){
		return topicElement.toString();
	}
	
	public Element getNodeUrl(){
		return topicElement.select(".forum-list-item-title .forum__title > div > a").get(0);
	}
	
	public String getStringUrl(){
		return this.getNodeUrl().attr("abs:href");
	}
	
	public int getTopicId(){
		return Integer.parseInt(this.getStringUrl().split("/")[5]);
	}
	
	public String getTopicTitle(){
		return this.getNodeUrl().html();
	}
	
	public int getTopicNbReplies(){
		return Integer.parseInt(topicElement.select(".forum-list-item-replies").get(0).text().replaceAll("Replies ", ""));
	}
	
	public String getTopicType(){
		return topicElement.select("div.forum-list-item-title > div:nth-child(1)").attr("title");
	}
	
	public boolean isStickyTopic(){
		return this.getTopicType().equals("Sticky topic");
	}
}
