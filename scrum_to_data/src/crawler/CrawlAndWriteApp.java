package crawler;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import analyser.TagCalculator;
import databasemodel.ForumDataBase;
import databasemodel.TagsTable;
import domainmodel.Author;
import domainmodel.Message;
import domainmodel.Tag;
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
	
	public ForumCrawler getForumCrawler(){
		return this.forumCrawler;
	}

	public void bddFirstBuild() {
		HashSet<Tag> tagSet = new HashSet<>();
		this.forumCrawler.browseAllPages();
		saveAllAsObjects();

		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.insertInTopicAuthorAndMessageTables(this.forumCrawler.getTopicsList(), this.forumCrawler.getMessagesList(),
					this.forumCrawler.getAuthorsList());
			for(Topic topic: this.forumCrawler.getTopicsList()){
				Set<String> temp = TagCalculator.calculTag(topic);
				tagSet.add(new Tag(topic.getId(), temp));
			}
			this.forumDataBase.getTagsTable().insertAll(tagSet, TagsTable.REPLACE);
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
			this.forumDataBase.insertInTopicAuthorAndMessageTables(IOSerialTools.readTopicsObject(ficTopics),
					IOSerialTools.readMessagesObject(ficMessages), IOSerialTools.readAuthorsObject(ficAuthors));
			this.forumDataBase.closeDB();
		}
	}
	
	public void crawlAndRewrite(){
		if (this.forumDataBase != null) {
			this.forumDataBase.openDB();
			this.forumDataBase.deleteAllLinesInTopicAuthorAndMessageTables();
			this.forumDataBase.getTagsTable().deleteAllLines();
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
			this.forumDataBase.insertInTopicAuthorAndMessageTables(this.forumCrawler.getTopicsList(), this.forumCrawler.getMessagesList(),
					this.forumCrawler.getAuthorsList());
			this.forumDataBase.closeDB();
		}
		
	}

}
