package db_interactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java_objects.Author;
import java_objects.HashSetAuthor;

/**
 * The representation of a mysql table, containing the authors
 * @author Audrey Loriette
 *
 */
public class AuthorTable {
	private ScrumDataBase sdb;
	private String tableName;
	
	
	/**
	 * Constructor
	 * @param sdb an instance of ScrumDataBase
	 * @param tableName an instance of String
	 * @see ScrumDataBase
	 */
	public AuthorTable(ScrumDataBase sdb, String tableName) {
		super();
		this.sdb = sdb;
		this.tableName = tableName;
	}
	
	/**
	 * Insert an author in the tableName (see constructor) table
	 * @param author an instance of Author
	 * @see Author
	 */
	public void insertAuthor(Author author){
		String insert = "INSERT INTO " + tableName 
				+" (author_id, login) "
				+ " VALUES (?, ?)";
		PreparedStatement stmt;
		try{
			stmt = sdb.getConnection().prepareStatement(insert);
			stmt.setInt(1, author.getAuthor_id());
			stmt.setString(2, author.getLogin());
			stmt.executeUpdate();
			stmt.close();
		}catch(SQLException e){
			System.out.println("an error occured when trying to execute an AuthorTable method: + \n" +
					"insertAuthor(Author author) with author login = " + author.getLogin() + "\n" + 
					e.getMessage());
		}
				
	}
	
	/**
	 * Insert authors in the database
	 * Be careful : this method does not make any control !
	 * @param allAuthors an instance of HashSetAuthor
	 */
	public void insertAllAuthor(HashSetAuthor allAuthors){
		for(Author a : allAuthors){
			insertAuthor(a);
		}
	}
	
	
	/**
	 * Get in the database the authors with the given login
	 * @param login an instance of String
	 * @return an HashSetAuthor containing all the authors with the given login, or an empty one if no user in the database has this login
	 */
	public HashSetAuthor getAuthorsByLogin(String login){
		String select = "SELECT * FROM " + tableName + 
				"WHERE login = " + login;
		Statement stmt;
		ResultSet rs;
		HashSetAuthor hashSetAuthor = new HashSetAuthor();
		try{
			stmt = sdb.getConnection().createStatement();
			rs = stmt.executeQuery(select);
			while(rs.next()){
				hashSetAuthor.add(new Author(rs.getString("login"), rs.getInt("author_id")));
			}
			stmt.close();
		}catch(SQLException e){
			System.out.println("an error occured when trying to execute an AuthorTable method: + \n" +
					"getAuthorByLogin(String login) with author login = " + login + "\n" + 
					e.getMessage());
		}
		
		return hashSetAuthor;
	}
	
	
	/**
	 * @param login, the author login
	 * @return an instance of Author, the author with the given id if it is in the table, null if not 
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
