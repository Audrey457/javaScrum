package crawler;

import org.jsoup.nodes.Element;

public class MessageElement {
	Element messageElement;
	Element authorElement;
	int id;

	/**
	 * Constructs a MessageElement with the specified Element
	 * @param messageElement
	 */
	public MessageElement(Element messageNode, int id) {
		super();
		this.messageElement = messageNode.select(".forum-node-messages-message").get(0);
		this.authorElement = messageNode.select(".forum-node-messages-author").get(0);
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
	 * @return an instance of String
	 */
	public String getDateMessage(){
		return messageElement.select(".forum-node-messages-date").text();
	}
	
	/**
	 * Get the author login
	 * @return an instance of String
	 */
	public String getAuthor(){
		return authorElement.text();
	}
	
	/**
	 * Get the id of the author, and return it's hash code. If the author has not an id, return its login hash code.
	 * @return an int
	 */
	public int getAuthorId(){
		String id = authorElement.select("a").attr("href");
		if(id.equals("")){
			id = this.getAuthor();
		}
		return id.hashCode();
	}

}
