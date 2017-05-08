package crawler;

import org.jsoup.nodes.Element;

/**
 * Represent an Element (a Node) for a message
 * @author Audrey Loriette
 *
 */
public class MessageElement {
	private Element messageNode;
	private Element authorNode;
	private int fkId;

	/**
	 * Constructs a MessageElement with the specified Element and the foreign key id = the topic id
	 * @param messageAndAuthorNode an instance of Element
	 * @param fkId an integer, the topic id
	 */
	public MessageElement(Element messageAndAuthorNode, int fkId) {
		super();
		this.messageNode = messageAndAuthorNode.select(".forum-node-messages-message").get(0);
		this.authorNode = messageAndAuthorNode.select(".forum-node-messages-author").get(0);
		this.fkId = fkId;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return messageNode.toString();
	}
	
	/**
	 * Get the message text
	 * @return an instance of String
	 */
	public String getMessage(){
		return messageNode.select("div p").text();
	}
	
	/**
	 * Get the topic id (foreign key)
	 * @return an int
	 */
	public int getfkIdTopic(){
		return fkId;
	}
	
	/**
	 * Get the message date, in a raw text format. To use this, it will probably be necessary to format it
	 * @return an instance of String
	 */
	public String getDateMessage(){
		return messageNode.select(".forum-node-messages-date").text();
	}
	
	/**
	 * Get the author login
	 * @return an instance of String
	 */
	public String getAuthor(){
		return authorNode.text();
	}
	
	/**
	 * Get the id of the author, and return it's hash code. If the author has not an id, return its login hash code.
	 * @return an int
	 */
	public int getAuthorId(){
		String id = authorNode.select("a").attr("href");
		if("".equals(id)){
			id = this.getAuthor();
		}
		return id.hashCode();
	}

}
