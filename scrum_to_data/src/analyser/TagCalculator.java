package analyser;

import java.util.HashSet;
import java.util.Set;

import domainmodel.Topic;

public class TagCalculator {
	
	private TagCalculator(){
	}
	
	/**
	 * For the moment it returns only the title of the topic, 
	 * but as it probably should be a list of key word, 
	 * this method as to change
	 * @param topic
	 * @return an instance of String
	 */
	public static Set<String> calculTag(Topic topic){
		HashSet<String> set = new HashSet<String>();
		set.add(topic.getTitle());
		return set;
	}
}
