package crawler;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class MessageElements{
	
	Elements messageElements;
	int id;
	
	/**
	 * Construct a MessageElements (a list of MessageElement) containing all replyMessagesNodes an the topicMessageNode of a topic
	 * @param replyMessagesNodes an instance of Elements
	 * @param topicMessageNode an instance of Element
	 */
	public MessageElements(Elements replyMessagesNodes, Element topicMessageNode, int id){
		Elements messageElements = new Elements(replyMessagesNodes);
		messageElements.add(topicMessageNode);
		this.messageElements = messageElements;
		this.id = id;
	}
	
	public Element get(int index){
		return messageElements.get(index);
	}
	
	public int size(){
		return messageElements.size();
	}
	
	/**
	 * The id of the related topic (foreign key)
	 * Should it be static ??
	 * @return an int
	 */
	public int getFkId(){
		return id;
	}
}
