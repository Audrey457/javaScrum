package java_objects;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * An array list of "Topic" objects
 * @author Audrey uti
 *
 */
public class ArrayTopics extends ArrayList<Topic> {

	/**
	 * As recommended by the documentation for java.io.Serializable (ArrayList
	 * is a Serializable class)
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Return a JsonArray containing all of the posts in the ArrayPosts
	 * 
	 * @return an instance of JsonArray
	 */
	public JsonArray toJsonArray() {
		ArrayTopics temp = this;
		JsonArray jsa = null;
		JsonArrayBuilder jsab = Json.createArrayBuilder();
		JsonObject jso;
		for (Topic p : temp) {
			jso = p.toJsonTopic();
			jsab.add(jso);
		}
		jsa = jsab.build();
		return jsa;
	}
}
