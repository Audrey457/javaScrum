package databasemodel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import analyser.TagCalculator;
import domainmodel.Author;
import domainmodel.Message;
import domainmodel.Tag;
import domainmodel.Topic;
import sometools.SetTranslationTools;

/**
 * The Java representation of a "scrum database" with the URL, login and passwd user
 * Construct a ScrumDataBase means to connect the user to the database with jdbc
 * @author Audrey Loriette
 *
 */
public class ForumDataBase {
	
	/**
	 * the url to localhost database could be : jdbc:mysql://localhost/<database_name>?autoReconnect=true&useSSL=false
	 * this : ?autoReconnect=true&useSSL=false is added to avoid ssl warnings
	 */
	private String url;
	private String login;
	private String passwd;
	private Connection connect;
	private TopicsTable topicTable;
	private MessagesTable messageTable;
	private AuthorsTable authorTable;
	private TagsTable tagsTable;
	private KeyWordsTable keyWordsTable;
	private final Logger logger = Logger.getLogger(ForumDataBase.class);
	
	/**
	 * Create a ScrumDataBase object
	 * This is the "basic" constructor
	 * @param url an instance of String, the database url
	 * @param login an instance of String, the user
	 * @param passwd an instance of String, the user passwd
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public ForumDataBase(String url, String login, String passwd){
		this.url = url;
		this.login = login;
		this.passwd = passwd;
		this.topicTable = new TopicsTable(this, "topics");
		this.messageTable = new MessagesTable(this, "messages");
		this.authorTable = new AuthorsTable(this, "authors");
		this.tagsTable =  new TagsTable(this, "tags");
		this.keyWordsTable = new KeyWordsTable(this, "keywords");
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.fatal(e);
		}
	}
	
	/**
	 * @return the tagsTable
	 */
	public TagsTable getTagsTable() {
		return tagsTable;
	}

	/**
	 * @return the keyWordsTable
	 */
	public KeyWordsTable getKeyWordsTable() {
		return keyWordsTable;
	}

	public void openDB(){
		try {
			connect = DriverManager.getConnection(url, login, passwd);
		} catch (SQLException e) {
			logger.fatal(e);
		} finally {
			logger.info("Connected to database");
		}
	}
	
	/**
	 * @return an instance of Connection
	 * @see Connection
	 */
	public Connection getConnection(){
		return this.connect;
	}
	
	/**
	 * Close the connection to the scrum data base
	 */
	public void closeDB(){
		try {
			connect.close();
		} catch (SQLException e) {
			logger.error(e);
		} finally {
			logger.info("Database connection closed");
		}
	}
	
	/**
	 * @param topicsList an instance of ArrayList/<Topic/>
	 * @param messagesList an instance of ArrayList/<Message/>
	 * @param authorsList an instance of LinkedHashSet/<Author/>
	 */
	public void insertInTopicAuthorAndMessageTables(List<Topic> topicsList, List<Message> messagesList, Set<Author> authorsList){
		this.topicTable.insertAll(topicsList);
		this.authorTable.insertAuthorsList(authorsList);
		this.messageTable.insertAll(messagesList);
	}
	
	public void deleteAllLinesInTopicAuthorAndMessageTables(){
		this.messageTable.deleteAllLines();
		this.topicTable.deleteAllLines();
		this.authorTable.deleteAllLines();
	}

	/**
	 * @return the topicTable
	 */
	public TopicsTable getTopicTable() {
		return topicTable;
	}

	/**
	 * @return the messageTable
	 */
	public MessagesTable getMessageTable() {
		return messageTable;
	}

	/**
	 * @return the authorTable
	 */
	public AuthorsTable getAuthorTable() {
		return authorTable;
	}
	
	/**
	 * @return true if all the tables in the forum database are empty, 
	 * false otherwise
	 */
	public boolean isEmpty(){
		return this.topicTable.isEmpty() &&
				this.messageTable.isEmpty() &&
				this.authorTable.isEmpty();
	}
	
//	public static void main(String [] args){
//		ForumDataBase fdb = new ForumDataBase(
//				"jdbc:mysql://localhost/scrumdata?autoReconnect=true&useSSL=false", 
//				"root", "");
//		HashSet<Topic> topicSet = new HashSet<>();
//		fdb.openDB();
//		topicSet = (HashSet<Topic>) fdb.getTopicTable().getAllTopics();
//		HashSet<Tag> tagSet = new HashSet<>();
//		for(Topic topic: topicSet){
//			Set<String> temp = TagCalculator.calculTag(topic);
//			tagSet.add(new Tag(topic.getId(), temp));
//		}
//		fdb.getTagsTable().insertAll(tagSet, TagsTable.REPLACE);
//		fdb.closeDB();
//	}
}
