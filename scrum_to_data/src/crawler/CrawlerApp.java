package crawler;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonWriter;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import edit_json_files.ArrayMessages;
import edit_json_files.ArrayPosts;
import edit_json_files.Message;
import edit_json_files.Post;

public class CrawlerApp {
	
	ArrayMessages allMessage;
	ArrayPosts allPosts;
	
	public CrawlerApp(){
		allMessage = new ArrayMessages();
		allPosts = new ArrayPosts();
	}
	
	public void visitAllPages(String url) throws IOException{
		ScrumForumCrawler sfc = new ScrumForumCrawler(url);
		String nextPageUrl = sfc.getNextPageUrl();
		visitAllTopics(new ScrumPageCrawler(url));
		if (!nextPageUrl.equals("EOS")){
			visitAllPages(nextPageUrl);
		}
	}
	
	public void visitAllMessageElements(MessageElements messageElement){
		MessageElement msg;
		Message message;
		int fkId = messageElement.getFkId();
		for(int i = 0; i < messageElement.size(); i++){
			msg = new MessageElement(messageElement.get(i), fkId);
			message = new Message(msg.getDateMessage(), msg.getMessage(), fkId);
			allMessage.add(message);
		}
	}
	
	public void visitAllTopics(ScrumPageCrawler spc) throws IOException{
		Elements topicsOnPage = spc.getTopicsElements();
		ScrumMessageCrawler smc;
		TopicElement topic;
		String topicUrl;
		MessageElements messageElements;
		Post post;
		for(Element e : topicsOnPage){
			topic = new TopicElement(e);
			topicUrl = topic.getStringUrl();
			post = new Post(topic.getTopicId(), topic.getTopicTitle(), topicUrl, topic.getTopicNbReplies());
			allPosts.add(post);
			smc = new ScrumMessageCrawler(topicUrl);
			messageElements = new MessageElements(smc.getReplyMessagesNodes(), smc.getTopicMessageNode(), smc.getFkId());
			visitAllMessageElements(messageElements);
		}
	}
	
	public void writeJsonFiles() throws IOException{
		JsonArray jsonMessage = allMessage.toJsonArray();
		JsonArray jsonTopics = allPosts.toJsonArray();
		JsonObjectBuilder jsobPosts = Json.createObjectBuilder();
		JsonObjectBuilder jsobMsg = Json.createObjectBuilder();
		JsonObject postsObject;
		JsonObject msgObject;
		OutputStream os_posts;
		OutputStream os_msg;
		JsonWriter postsWriter;
		JsonWriter msgWriter;
		
		jsobPosts.add("posts", jsonTopics);
		jsobMsg.add("messages", jsonMessage);
		postsObject = jsobPosts.build();
		msgObject = jsobMsg.build();
		
		os_posts = new FileOutputStream("E:\\Documents\\cours\\stage\\scrum\\scrumPosts.json");
		postsWriter = Json.createWriter(os_posts);
		postsWriter.writeObject(postsObject);
		postsWriter.close();
		os_posts.close();
		
		os_msg = new FileOutputStream("E:\\Documents\\cours\\stage\\scrum\\scrumMsg.json");
		msgWriter = Json.createWriter(os_msg);
		msgWriter.writeObject(msgObject);
		msgWriter.close();
		os_msg.close();	
	}

	public static void main(String[] args) throws IOException {
		CrawlerApp ca = new CrawlerApp();
		ca.visitAllPages("https://www.scrum.org/forum/scrum-forum");
		
		ca.writeJsonFiles();
	}

}
