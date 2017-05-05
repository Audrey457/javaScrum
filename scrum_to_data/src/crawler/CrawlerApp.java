package crawler;

import java.io.File;
import java.io.IOException;

import db_interactions.ForumDataBase;
import some_tools.IOSerialTools;

public class CrawlerApp {

	ForumDataBase forumDataBase = null;
	ForumCrawler forumCrawler;

	public CrawlerApp(ForumDataBase forumDataBase, String forumUrl) {
		this.forumDataBase = forumDataBase;
		this.forumCrawler = new ForumCrawler(forumUrl);

	}

	public void bddFirstBuild() throws IOException {
		this.forumCrawler.browseAllPages();
		IOSerialTools.saveTopicsAsObject(this.forumCrawler.getTopicsList());
		IOSerialTools.saveAuthorsAsObject(this.forumCrawler.getAuthorsList());
		IOSerialTools.saveMessagesAsObject(this.forumCrawler.getMessagesList());

		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.insertInAllTables(this.forumCrawler.getTopicsList(), this.forumCrawler.getMessagesList(),
					this.forumCrawler.getAuthorsList());
			this.forumDataBase.closeDB();
		}
	}

	public void writeObjectsToDatabase(File ficMessages, File ficTopics, File ficAuthors) {
		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.insertInAllTables(IOSerialTools.readTopicsObject(ficTopics),
					IOSerialTools.readMessagesObject(ficMessages), IOSerialTools.readAuthorsObject(ficAuthors));
			this.forumDataBase.closeDB();
		}
	}

	public void basicUpdate() {
		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumCrawler.browsePagesToUpdate(this.forumDataBase.getTopicTable(),
					this.forumDataBase.getMessageTable());
			this.forumDataBase.closeDB();
		}
		IOSerialTools.saveTopicsAsObject(this.forumCrawler.getTopicsList());
		IOSerialTools.saveAuthorsAsObject(this.forumCrawler.getAuthorsList());
		IOSerialTools.saveMessagesAsObject(this.forumCrawler.getMessagesList());
		
		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.insertInAllTables(this.forumCrawler.getTopicsList(), this.forumCrawler.getMessagesList(),
					this.forumCrawler.getAuthorsList());
			this.forumDataBase.closeDB();
		}
		
	}

	public static void main(String[] args) {
		CrawlerApp crawlerApp = new CrawlerApp(
				new ForumDataBase("jdbc:mysql://localhost/base_de_test?autoReconnect=true&useSSL=false", "root", ""),
				"https://www.scrum.org/forum/scrum-forum");
		crawlerApp.basicUpdate();
	}

}
