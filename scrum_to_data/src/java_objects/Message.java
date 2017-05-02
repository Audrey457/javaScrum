package java_objects;

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
	private String date_message;
	private int id_post;
	private String msg;
	private int id_author;
	private int id_msg;
	
	public Message(String date_message, String msg, int id_post, int author_id) {
		super();
		this.date_message = date_message;
		this.id_post = id_post;
		this.msg = msg;
		this.id_author = author_id;
	}
	
	public Message(String date_message, String msg, int id_post, int author_id, int id_msg) {
		super();
		this.date_message = date_message;
		this.id_post = id_post;
		this.msg = msg;
		this.id_author = author_id;
		this.id_msg = id_msg;
	}
	
	
	public int getId_msg() {
		return id_msg;
	}


	public void setId_msg(int id_msg) {
		this.id_msg = id_msg;
	}


	public int getAuthor_id() {
		return id_author;
	}


	public void setAuthor_id(int author_id) {
		this.id_author = author_id;
	}


	public String getDate_message() {
		return date_message;
	}


	public void setDate_message(String id_message) {
		this.date_message = id_message;
	}


	public int getId_post() {
		return id_post;
	}


	public void setId_post(int id_post) {
		this.id_post = id_post;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	/**
	 * Use this method to create a JsonObject that represents one message
	 * (id_message, id_post, msg) from a Post object
	 * @return an instance of JsonObject
	 */
	public JsonObject toJsonMsg(){
		JsonObjectBuilder msgBuilder = Json.createObjectBuilder();
		msgBuilder.add("date_message", this.getDate_message());
		msgBuilder.add("id_post", this.getId_post());
		msgBuilder.add("msg", this.getMsg());
		msgBuilder.add("author_id", this.id_author);
		return msgBuilder.build();
	}
	
	public String toString(){
		return "-------------------" + 
				"\nDate Message : " + date_message +
				"\nID msg : " + id_msg +
				"\nID Post : " + id_post +
				"\nID Author : " + id_author + 
				"\nMessage : " + msg + "\n" + 
				"-------------------";
	}

	@Override
	public int compareTo(Message message) {
		if(this.getId_msg() == message.getId_msg()){
			return 0;
		}
		return this.getDate_message().compareTo(message.getDate_message());
	}
	
	
}
