package db_interactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ScrumDataBase {
	/*the url to localhost database could be : jdbc:mysql://localhost/<database_name>?autoReconnect=true&useSSL=false
	 * this : ?autoReconnect=true&useSSL=false is added to avoid ssl warnings
	 */
	String url;
	String login;
	String passwd;
	Connection connect;
	
	/**
	 * Create a ScrumDataBase object
	 * @param url an instance of String, the database url
	 * @param login an instance of String, the user
	 * @param passwd an instance of String, the user passwd
	 * @throws ClassNotFoundException 
	 * @throws SQLException 
	 */
	public ScrumDataBase(String url, String login, String passwd) throws ClassNotFoundException, SQLException{
		this.url = url;
		this.login = login;
		this.passwd = passwd;
		Class.forName("com.mysql.jdbc.Driver");
		connect = DriverManager.getConnection(url, login, passwd);
	}
	
	public Connection getConnection(){
		return this.connect;
	}
	
	public void closeDB() throws SQLException{
		connect.close();
	}
	
	
}
