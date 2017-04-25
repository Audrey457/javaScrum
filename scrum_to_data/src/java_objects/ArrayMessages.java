package java_objects;

import java.util.ArrayList;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;

/**
 * An array list of "Message" objects
 * @author Audrey Loriette
 *
 */
public class ArrayMessages extends ArrayList<Message> {

	/**
	 * As recommended by the documentation for java.io.Serializable (ArrayList
	 * is a Serializable class)
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Return a JsonArray containing all of the messages in the ArrayMessages
	 * 
	 * @return an instance of JsonArray
	 */
	public JsonArray toJsonArray() {
		ArrayMessages temp = this;
		JsonArray jsa = null;
		JsonArrayBuilder jsab = Json.createArrayBuilder();
		JsonObject jso;
		for (Message m : temp) {
			jso = m.toJsonMsg();
			jsab.add(jso);
		}
		jsa = jsab.build();
		return jsa;
	}

}
