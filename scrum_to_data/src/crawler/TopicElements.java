package crawler;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TopicElements {
	Elements topicElements;

	public TopicElements(Elements topicElements) {
		super();
		this.topicElements = topicElements;
	}
	
	public Element get(int index){
		return topicElements.get(index);
	}
	
	
}
