package controlers;

import crawler.CrawlAndWriteApp;
import databasemodel.ForumDataBase;

public class CrawlAndWriteControler {
	CrawlAndWriteApp crawlerApp;
	
	public CrawlAndWriteControler(CrawlAndWriteApp crawlerApp){
		this.crawlerApp = crawlerApp;
	}
	
	
	/**
	 * Verify if there are already lines in the forumDataBase. If so, the button 
	 * "create" must not be enabled
	 * @param forumDataBase
	 * @return true if the forumDataBase does not contain even just a line, 
	 * false otherwise
	 */
	public boolean dataBaseEmpty(){
		boolean empty;
		ForumDataBase forumDataBase = this.crawlerApp.getForumDataBase();
		forumDataBase.openDB();
		empty = forumDataBase.isEmpty();
		forumDataBase.closeDB();
		return empty;
	}
	
	public boolean noDatabase(){
		boolean isNull;
		ForumDataBase forumDataBase = this.crawlerApp.getForumDataBase();
		forumDataBase.openDB();
		isNull = (forumDataBase.getConnection() == null);
		if(!isNull)
			forumDataBase.closeDB();
		return isNull;
		
	}
	
	public boolean canAccessForum(){
		return this.crawlerApp.getForumCrawler().canAccessForum();
	}
	
}
