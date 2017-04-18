package edit_json_files;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

public class ArrayPosts extends ArrayList<Post> {

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
		ArrayPosts temp = this;
		JsonArray jsa = null;
		JsonArrayBuilder jsab = Json.createArrayBuilder();
		JsonObject jso;
		for (Post p : temp) {
			jso = p.toJsonPost();
			jsab.add(jso);
		}
		jsa = jsab.build();
		return jsa;
	}
}
