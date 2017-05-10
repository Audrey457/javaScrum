package domainmodel;

import java.io.Serializable;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 * A "Message" object, with its date, its author id, the id of its relative post, its id,  and the message itself
 * @author Audrey Loriette
 *
 */
public class Message implements Serializable, java.lang.Comparable<Message>{
	
	/**
	 * As recommended by the documentation for java.io.Serializable
	 */
	private static final long serialVersionUID = 1173414698663652202L;
	private String messageDate;
	private int topicId;
	private String messageText;
	private int authorId;
	private int messageId;
	
	public Message(String messageDate, String msg, int topicId, int authorId) {
		super();
		this.messageDate = messageDate;
		this.topicId = topicId;
		this.messageText = msg;
		this.authorId = authorId;
	}
	
	public Message(String messageDate, String msg, int topicId, int authorId, int msgId) {
		super();
		this.messageDate = messageDate;
		this.topicId = topicId;
		this.messageText = msg;
		this.authorId = authorId;
		this.messageId = msgId;
	}
	
	
	public int getMsgId() {
		return messageId;
	}


	public void setMsgId(int msgId) {
		this.messageId = msgId;
	}


	public int getAuthorId() {
		return authorId;
	}


	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}


	public String getMessageDate() {
		return messageDate;
	}


	public void setMessageDate(String messageDate) {
		this.messageDate = messageDate;
	}


	public int getTopicId() {
		return topicId;
	}


	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}


	public String getMsg() {
		return messageText;
	}


	public void setMsg(String msg) {
		this.messageText = msg;
	}
	
	/**
	 * Use this method to create a JsonObject that represents one message
	 * (id_message, id_post, msg) from a Post object
	 * @return an instance of JsonObject
	 */
	public JsonObject toJsonMsg(){
		JsonObjectBuilder msgBuilder = Json.createObjectBuilder();
		msgBuilder.add("date_message", this.getMessageDate());
		msgBuilder.add("id_post", this.getTopicId());
		msgBuilder.add("msg", this.getMsg());
		msgBuilder.add("author_id", this.authorId);
		return msgBuilder.build();
	}
	
	@Override
	public String toString(){
		return "-------------------" + 
				"\nDate Message : " + messageDate +
				"\nID msg : " + messageId +
				"\nID Post : " + topicId +
				"\nID Author : " + authorId + 
				"\nMessage : " + messageText + "\n" + 
				"-------------------";
	}

	@Override
	public int compareTo(Message message) {
		if (message.equals(this)){
			return 0;
		}
		return this.messageDate.compareTo(message.messageDate);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + authorId;
		result = prime * result + ((messageDate == null) ? 0 : messageDate.hashCode());
		result = prime * result + topicId;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Message other = (Message) obj;
		if (topicId != other.topicId)
			return false;
		if (authorId != other.authorId)
			return false;
		if (messageDate == null) {
			if (other.messageDate != null)
				return false;
		} else if (!messageDate.equals(other.messageDate))
			return false;
		return true;
	}
	
	
	
	
}
