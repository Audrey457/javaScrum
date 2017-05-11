package controlers;

import databasemodel.ForumDataBase;

public class CrawlAndWriteControler {
	
	
	/**
	 * Verify if there are already lines in the forumDataBase. If so, the button 
	 * "create" must not be enabled
	 * @param forumDataBase
	 * @return true if the forumDataBase does not contain even just a line, 
	 * false otherwise
	 */
	public static boolean dataBaseEmpty(ForumDataBase forumDataBase){
		boolean empty;
		forumDataBase.openDB();
		empty = forumDataBase.isEmpty();
		forumDataBase.closeDB();
		return empty;
	}
	
}
