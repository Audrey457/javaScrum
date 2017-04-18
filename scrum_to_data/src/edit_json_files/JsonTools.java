package edit_json_files;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonWriter;

public class JsonTools {
	
	private ArrayTopics allPosts;
	private ArrayMessages allMessages;
	
	public JsonTools() {
		super();
		allPosts = new ArrayTopics();
		allMessages = new ArrayMessages();
	}
	
	public ArrayTopics getAllPosts(){
		return allPosts;
	}
	
	public ArrayMessages getAllMessages(){
		return allMessages;
	}

	public JsonObject getJsonObject(String path) throws IOException{
		InputStream fis;
		fis = new FileInputStream(path);
		JsonReader jsr = Json.createReader(fis);
		JsonObject jso = jsr.readObject();
		jsr.close();
		fis.close();
		return jso;
	}
	
	public JsonArray getArrayJsonPosts(JsonObject jso){
		return jso.getJsonArray("posts");
	}
	
	public JsonArray getArrayJsonMessages(JsonObject jso){
		return jso.getJsonArray("messages");
	}
	
	public int getPostIdFromUrl(JsonObject jso){
		String url = jso.getString("url");
		String splitedUrl[] = url.split("/");
		return Integer.parseInt(splitedUrl[5]);
	}
	
	//a post = id, title, url, nb_replies
	public void setAllPosts(JsonArray jsa){
		JsonObject jso;
		Topic p;
		for(int i = 0; i < jsa.size(); i++){
			jso = jsa.getJsonObject(i);
			p = new Topic(getPostIdFromUrl(jso), jso.getString("title"), jso.getString("url"));
			allPosts.add(p);
		}
	}
	
	//a message = id_message, id_post, msg
	public void setAllMessages(JsonArray jsa){
		JsonObject jso;
		Message m;
		for(int i = 0; i < jsa.size(); i++){
			jso = jsa.getJsonObject(i);
			m = new Message(jso.getString("date_message"), jso.getString("msg"), getPostIdFromUrl(jso));
			allMessages.add(m);
		}
	}


	public static void main(String[] args) throws IOException {
		JsonObject searchResult;
		JsonArray postResult;
		JsonArray msgResult;
		
		JsonTools aitp = new JsonTools();
		
		//get the result file
		searchResult = aitp.getJsonObject("E:\\Documents\\cours\\stage\\scrum\\scrum_forum_page_1.json");
		
		//get each array
		postResult = aitp.getArrayJsonPosts(searchResult);
		msgResult = aitp.getArrayJsonMessages(searchResult);
		
		//modify each array so it can match with the database structure
		aitp.setAllPosts(postResult);
		aitp.setAllMessages(msgResult);
		postResult = aitp.allPosts.toJsonArray();
		msgResult = aitp.allMessages.toJsonArray();
		
		//write each array to a jsonObject
		JsonObjectBuilder jsobPosts = Json.createObjectBuilder();
		JsonObjectBuilder jsobMsg = Json.createObjectBuilder();
		JsonObject postsObject;
		JsonObject msgObject;
		jsobPosts.add("posts", postResult);
		jsobMsg.add("messages", msgResult);
		postsObject = jsobPosts.build();
		msgObject = jsobMsg.build();
		
		//write each object to a JsonFile
		OutputStream os_posts = new FileOutputStream("E:\\Documents\\cours\\stage\\scrum\\scrum_posts.json");
		JsonWriter postsWriter = Json.createWriter(os_posts);
		postsWriter.writeObject(postsObject);
		postsWriter.close();
		os_posts.close();
		OutputStream os_msg = new FileOutputStream("E:\\Documents\\cours\\stage\\scrum\\scrum_msg.json");
		JsonWriter msgWriter = Json.createWriter(os_msg);
		msgWriter.writeObject(msgObject);
		msgWriter.close();
		os_msg.close();
	}

}
