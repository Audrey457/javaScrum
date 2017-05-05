package db_interactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import java_objects.Author;
import java_objects.Message;
import java_objects.Topic;

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
	private TopicTable topicTable;
	private MessageTable messageTable;
	private AuthorTable authorTable;
	
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
		this.topicTable = new TopicTable(this, "topics");
		this.messageTable = new MessageTable(this, "messages");
		this.authorTable = new AuthorTable(this, "authors");
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("An error occured when trying to construct a ScrumDataBase " + 
					e.getMessage());
		}
	}
	
	public void openDB(){
		try {
			connect = DriverManager.getConnection(url, login, passwd);
		} catch (SQLException e) {
			System.out.println("An error occured when trying to construct a ScrumDataBase " + 
					e.getMessage());
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
			System.out.println("An error occured when trying to close the database Connection " + 
					e.getMessage());
		}
	}
	
	/**
	 * @param topicsList an instance of ArrayList/<Topic/>
	 * @param messagesList an instance of ArrayList/<Message/>
	 * @param authorsList an instance of LinkedHashSet/<Author/>
	 */
	public void insertInAllTables(ArrayList<Topic> topicsList, ArrayList<Message> messagesList, LinkedHashSet<Author> authorsList){
		this.topicTable.insertAll(topicsList);
		this.authorTable.insertAuthorsList(authorsList);
		this.messageTable.insertAll(messagesList);
	}

	/**
	 * @return the topicTable
	 */
	public TopicTable getTopicTable() {
		return topicTable;
	}

	/**
	 * @return the messageTable
	 */
	public MessageTable getMessageTable() {
		return messageTable;
	}

	/**
	 * @return the authorTable
	 */
	public AuthorTable getAuthorTable() {
		return authorTable;
	}
	
	
	
	
}
