package edit_json_files;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Topic {
	private int id;
	private String title;
	private String url;
	
	public Topic(int id, String title, String url) {
		super();
		this.id = id;
		this.title = title;
		this.url = url;
	}
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String toString(){
		return "------ ID = " + id + "------"
				+ "\n" + title 
				+ "\n" + url
				+ "---------------------------\n";
		
	}
	
	/**
	 * Use this method to create a JsonObject that represents one post
	 * (id, title, url, nb_replies) from a Post object
	 * @param post an instance of Post
	 * @return an instance of JsonObject
	 */
	public JsonObject toJsonTopic(){
		JsonObjectBuilder postBuilder = Json.createObjectBuilder();
		postBuilder.add("id", this.getId());
		postBuilder.add("title", this.getTitle());
		postBuilder.add("url", this.getUrl());
		return postBuilder.build();
	}
}
