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
	
}
