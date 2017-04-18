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
//		Element nbRepliesNode = topicElement.select(".forum-list-item-replies").get(0);
//		String nbRepliesString = nbRepliesNode.toString().replaceAll("<d.*[\\s\\S]*an>", "");
//		nbRepliesString = nbRepliesString.trim();
//		nbRepliesString = nbRepliesString.replaceAll("[\\s]*</div>", "");
//		return Integer.parseInt(nbRepliesString);
		return Integer.parseInt(topicElement.select(".forum-list-item-replies").get(0).text().replaceAll("Replies ", ""));
	}
}
