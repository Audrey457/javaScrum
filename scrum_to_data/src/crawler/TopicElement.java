package crawler;

import org.jsoup.nodes.Element;

/**
 * Encapsulate an Element just to add some methods and use only the needed 
 * methods
 * @author Audrey Loriette
 *
 */
public class TopicElement {
	Element topicNode;

	public TopicElement(Element topicNode) {
		this.topicNode = topicNode;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return topicNode.toString();
	}
	
	public Element getNodeUrl(){
		return topicNode.select(".forum-list-item-title .forum__title > div > a").get(0);
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
		return Integer.parseInt(topicNode.select(".forum-list-item-replies").get(0).text().replaceAll("Replies ", ""));
	}
	
	public String getTopicType(){
		return topicNode.select("div.forum-list-item-title > div:nth-child(1)").attr("title");
	}
	
	public boolean isStickyTopic(){
		return "Sticky topic".equals(this.getTopicType());
	}
}
