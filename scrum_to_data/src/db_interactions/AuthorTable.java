package db_interactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java_objects.Author;
import java_objects.HashSetAuthor;
import java_objects.Message;

/**
 * A mysql table, containing the authors
 * @author Audrey Loriette
 *
 */
public class AuthorTable {
	private ScrumDataBase sdb;
	private String tableName;
	//to do : change author_id
	
	public AuthorTable(ScrumDataBase sdb, String tableName) {
		super();
		this.sdb = sdb;
		this.tableName = tableName;
	}
	
	public void insertAuthor(Author author) throws SQLException{
		String insert = "INSERT INTO " + tableName 
				+" (author_id, login) "
				+ " VALUES (?, ?)";
		PreparedStatement stmt = sdb.getConnection().prepareStatement(insert);
		stmt.setInt(1, author.getAuthor_id());
		stmt.setString(2, author.getLogin());
		stmt.executeUpdate();
		stmt.close();
	}
	
	/**
	 * This method must be used only one time, when the site is for the first time crawled 
	 * @param allAuthors an instance of HashSetAuthor
	 * @throws SQLException 
	 */
	public void insertAllAuthor(HashSetAuthor allAuthors) throws SQLException{
		for(Author a : allAuthors){
			insertAuthor(a);
		}
	}
	
	public void insertAuthor(String login) throws SQLException{
		String insert = "INSERT INTO " + tableName 
				+" (login) "
				+ " VALUES " + login;
		Statement stmt = sdb.getConnection().createStatement();
		stmt.executeQuery(insert);
		stmt.close();
		
	}
	
	public Author getAuthorByLogin(String login) throws SQLException{
		String select = "SELECT * FROM " + tableName + 
				"WHERE login = " + login;
		Statement stmt = sdb.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(select);
		Author author = new Author(rs.getString("login"), rs.getInt("author_id"));
		stmt.close();
		
		return author;
	}
	
	public int getAuthorId(String login) throws SQLException{
		String select = "SELECT author_id FROM " + tableName + 
				"WHERE login = " + login;
		Statement stmt = sdb.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(select);
		stmt.close();
		
		return rs.getInt(1);
	}
	
	/**
	 * @param login, the author login
	 * @return an instance of Author, the author with the given login if it is in the table, null if not 
	 * @throws SQLException
	 */
	public Author contains(String login) throws SQLException{
		String sql = "SELECT * FROM " + tableName + 
				" WHERE login = " + login;
		Statement stmt = sdb.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		Author author = null;
		if(rs.next()){
			author = new Author(rs.getString("login"), rs.getInt("author_id"));
		}
		stmt.close();
		
		return author;
	}
	
	/**
	 * @param login, the author login
	 * @return an instance of Author, the author with the given id if it is in the table, null if not 
	 * @throws SQLException
	 */
	public Author contains(int id){
		String sql = "SELECT * FROM " + tableName + 
				" WHERE author_id = " + id;
		Author author = null;
		try{
			Statement stmt = sdb.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				author = new Author(rs.getString("login"), rs.getInt("author_id"));
			}
			stmt.close();
		}catch(SQLException e){
			System.out.println("SQL Error when trying to execute contains(int id) method "
					+ e.getMessage());
		}
		return author;
	}
}
