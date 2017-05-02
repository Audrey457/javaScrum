package db_interactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The Java representation of a "scrum database" with the URL, login and passwd user
 * Construct a ScrumDataBase means to connect the user to the database with jdbc
 * @author Audrey Loriette
 *
 */
public class ScrumDataBase {
	/**
	 * the url to localhost database could be : jdbc:mysql://localhost/<database_name>?autoReconnect=true&useSSL=false
	 * this : ?autoReconnect=true&useSSL=false is added to avoid ssl warnings
	 */
	private String url;
	private String login;
	private String passwd;
	private Connection connect;
	
	/**
	 * Create a ScrumDataBase object
	 * @param url an instance of String, the database url
	 * @param login an instance of String, the user
	 * @param passwd an instance of String, the user passwd
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public ScrumDataBase(String url, String login, String passwd){
		this.url = url;
		this.login = login;
		this.passwd = passwd;
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println("An error occured when trying to construct a ScrumDataBase " + 
					e.getMessage());
		}
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
	
	
}
