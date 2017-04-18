package edit_json_files;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Message {
	public String date_message;
	public int id_post;
	public String msg;
	
	
	public Message(String date_message, String msg, int id_post) {
		super();
		this.date_message = date_message;
		this.id_post = id_post;
		this.msg = msg;
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
		return msgBuilder.build();
	}
	
	public String toString(){
		return "-------------------" + 
				"\nID Message : " + date_message + 
				"\nID Post : " + id_post + 
				"\nMessage : " + msg + "\n" + 
				"-------------------";
	}
	
	
}
