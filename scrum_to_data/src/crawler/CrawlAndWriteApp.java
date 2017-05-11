package crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import databasemodel.ForumDataBase;
import domainmodel.Author;
import domainmodel.Message;
import domainmodel.Topic;
import sometools.IOSerialTools;

public class CrawlAndWriteApp {

	private ForumDataBase forumDataBase = null;
	private ForumCrawler forumCrawler;

	public CrawlAndWriteApp(ForumDataBase forumDataBase, String forumUrl) {
		this.forumDataBase = forumDataBase;
		this.forumCrawler = new ForumCrawler(forumUrl);

	}
	
	public ForumDataBase getForumDataBase(){
		return this.forumDataBase;
	}

	public void bddFirstBuild() {
		this.forumCrawler.browseAllPages();
		saveAllAsObjects();

		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.insertInAllTables(this.forumCrawler.getTopicsList(), this.forumCrawler.getMessagesList(),
					this.forumCrawler.getAuthorsList());
			this.forumDataBase.closeDB();
		}
	}
	
	public void saveAllAsObjects(){
		IOSerialTools.saveTopicsAsObject((ArrayList<Topic>)this.forumCrawler.getTopicsList());
		IOSerialTools.saveAuthorsAsObject((LinkedHashSet<Author>)this.forumCrawler.getAuthorsList());
		IOSerialTools.saveMessagesAsObject((ArrayList<Message>)this.forumCrawler.getMessagesList());
	}

	public void writeObjectsToDatabase(File ficMessages, File ficTopics, File ficAuthors) {
		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.insertInAllTables(IOSerialTools.readTopicsObject(ficTopics),
					IOSerialTools.readMessagesObject(ficMessages), IOSerialTools.readAuthorsObject(ficAuthors));
			this.forumDataBase.closeDB();
		}
	}
	
	public void crawlAndRewrite(){
		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.deleteAllLinesInAllTables();
			this.forumDataBase.closeDB();
		}
		bddFirstBuild();
	}

	public void basicUpdate() {
		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumCrawler.browsePagesToUpdate(this.forumDataBase.getTopicTable(),
					this.forumDataBase.getMessageTable());
			this.forumDataBase.closeDB();
		}
		saveAllAsObjects();
		
		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.insertInAllTables(this.forumCrawler.getTopicsList(), this.forumCrawler.getMessagesList(),
					this.forumCrawler.getAuthorsList());
			this.forumDataBase.closeDB();
		}
		
	}

}
