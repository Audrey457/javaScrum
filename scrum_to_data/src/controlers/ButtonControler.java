package controlers;

import crawler.CrawlAndWriteApp;
import databasemodel.ForumDataBase;

public class ButtonControler {
	CrawlAndWriteApp crawlerApp;
	ForumDataBase forumDataBase;
	
	public ButtonControler(CrawlAndWriteApp crawlerApp){
		this.crawlerApp = crawlerApp;
		this.forumDataBase = this.crawlerApp.getForumDataBase();

	}
	
	public ButtonControler(ForumDataBase forumDataBase){
		this.forumDataBase = forumDataBase;
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
		forumDataBase.openDB();
		empty = forumDataBase.isEmpty();
		forumDataBase.closeDB();
		return empty;
	}
	
	public boolean noDatabase(){
		boolean isNull;
		forumDataBase.openDB();
		isNull = (forumDataBase.getConnection() == null);
		if(!isNull)
			forumDataBase.closeDB();
		return isNull;
		
	}
	
	public boolean canAccessForum(){
		return this.crawlerApp.getForumCrawler().canAccessForum();
	}
	
	public boolean keyWordsTableEmpty(){
		boolean empty;
		this.forumDataBase.openDB();
		empty = forumDataBase.getKeyWordsTable().isEmpty();
		this.forumDataBase.closeDB();
		return empty;
	}
	
}
