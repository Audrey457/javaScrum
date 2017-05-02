package crawler;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.mysql.fabric.xmlrpc.base.Array;

import java_objects.Message;

/**
 * "Elements" encapsulation + the topic id (foreign key)
 * @author Audrey Loriette
 *
 */
public class MessageElements{
	
	Elements messageElements;
	int id;
	
	/**
	 * Construct a MessageElements (a list of MessageElement) containing all replyMessagesNodes and the topicMessageNode of a topic
	 * @param replyMessagesNodes an instance of Elements
	 * @param topicMessageNode an instance of Element
	 */
	public MessageElements(Elements replyMessagesNodes, Element topicMessageNode, int id){
		Elements messageElements = new Elements();
		messageElements.add(topicMessageNode);
		messageElements.addAll(replyMessagesNodes);
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
	 * @return an int
	 */
	public int getFkId(){
		return id;
	}
	
	public Element remove(int index){
		return messageElements.remove(index);
	}
	
	public List<Message> toMessageList(){
		MessageElement messageElement;
		ArrayList<Message> list = new ArrayList<>();
		for(int i = 0; i < this.size(); i++){
			messageElement = new MessageElement(this.get(i), this.getFkId());
			list.add(new Message(messageElement.getDateMessage(),
					messageElement.getMessage(),
					this.getFkId(), 
					messageElement.getAuthorId()));
		}
		return list;
	}
}
