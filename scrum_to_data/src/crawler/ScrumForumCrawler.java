package crawler;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class ScrumForumCrawler {
	private Document scrumForum;
	private String url;
	
	public ScrumForumCrawler(String url){
		this.url = url;
		try{
			this.scrumForum = Jsoup.connect(url).get();
		}catch(IOException e){
			System.out.println("An error occured when trying to construct a ScrumForumCrawler " + 
					"can not access to this site: " + url + "\n" + e.getMessage());
		}
	}
	
	
	public String getNextPageUrl(){
		Elements nextPageNode = scrumForum.select("li.pager__item.pager__item--next > a");
		if(nextPageNode.size() == 0)
			return "EOS";
		return nextPageNode.attr("abs:href");
	}
	
	
}
