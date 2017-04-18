package crawler;

import org.jsoup.nodes.Element;

public class MessageElement {
	Element messageElement;
	int id;

	/**
	 * Constructs a MessageElement with the specified Element
	 * @param messageElement
	 */
	public MessageElement(Element messageElement, int id) {
		super();
		this.messageElement = messageElement;
		this.id = id;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return messageElement.toString();
	}
	
	/**
	 * Get the message text
	 * @return an instance of String
	 */
	public String getMessage(){
		return messageElement.select("div p").text();
	}
	
	/**
	 * Get the topic id (foreign key)
	 * @return an int
	 */
	public int getfkIdTopic(){
		return id;
	}
	
	/**
	 * Get the message date, in a raw text format. To use this, it will probably be necessary to format it
	 * @return an instance
	 */
	public String getDateMessage(){
		return messageElement.select(".forum-node-messages-date").text();
	}

}
