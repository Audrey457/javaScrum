package crawler;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ScrumPageCrawler {

	private String url;
	private Document scrumPage;
	
	public ScrumPageCrawler(String url){
		this.url = url;
		try{
			this.scrumPage = Jsoup.connect(url).get();
		}catch(IOException e){
			System.out.println("An error occured when trying to construct a ScrumPageCrawler " + 
					"can not access to this site: " + url + "\n" + e.getMessage());
		}
	}
	
	
	/**
	 * Crawl a scrum forum page and get all the topics Element
	 * @param doc the HTML web page to crawl
	 * @return an instance of Elements, which is an array of topic Element
	 */
	public Elements getTopicsElements(){
		Elements topicsOnPage = new Elements();
		topicsOnPage = this.scrumPage.select(".forum-list-view-item");
		return topicsOnPage;
	}
	
	
	/**
	 * Get all topics urls from a scrum forum page
	 * @return an ArrayList/<String/>
	 */
	public ArrayList<String> getAllTopicsUrl(){
		ArrayList<String> topicsUrls = new ArrayList<>();
		Elements topicsOnPageUrls = this.getTopicsElements().select(".forum-list-item-title .forum__title > div > a");
		for(Element e : topicsOnPageUrls){
			topicsUrls.add(e.attr("abs:href"));
		}
		return topicsUrls;
	}
	
}
