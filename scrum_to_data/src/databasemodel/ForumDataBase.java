package databasemodel;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import domainmodel.Author;
import domainmodel.Message;
import domainmodel.Topic;

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
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			logger.fatal(e);
		}
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
	public void insertInAllTables(List<Topic> topicsList, List<Message> messagesList, Set<Author> authorsList){
		this.topicTable.insertAll(topicsList);
		this.authorTable.insertAuthorsList(authorsList);
		this.messageTable.insertAll(messagesList);
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
}
