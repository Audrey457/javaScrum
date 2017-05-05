package db_interactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

import java_objects.Author;

/**
 * The representation of a mysql table, containing the authors
 * @author Audrey Loriette
 *
 */
public class AuthorTable {
	private ForumDataBase forumDataBase;
	private String tableName;
	
	
	/**
	 * Constructor
	 * @param forumDataBase an instance of ForumDataBase
	 * @param tableName an instance of String
	 * @see ForumDataBase
	 */
	public AuthorTable(ForumDataBase forumDataBase, String tableName) {
		super();
		this.forumDataBase = forumDataBase;
		this.tableName = tableName;
	}
	
	/**
	 * Insert an author in the tableName (see constructor) table
	 * @param author an instance of Author
	 * @see Author
	 */
	public void insertAuthor(Author author){
		if(this.contains(author.getAuthor_id()) != null){
			String insert = "INSERT INTO " + tableName 
				+" (author_id, login) "
				+ " VALUES (?, ?)";
			PreparedStatement stmt;
			try{
				stmt = forumDataBase.getConnection().prepareStatement(insert);
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
	}
	
	/**
	 * Insert authors in the database
	 * @param allAuthors an instance of Set/<Author/>
	 */
	public void insertAuthorsList(Set<Author> authorsList){
		for(Author a : authorsList){
			insertAuthor(a);
		}
	}
	
	
	/**
	 * Get in the database the authors with the given login
	 * @param login an instance of String
	 * @return a LinkedHashSet/<Author/> containing all the authors 
	 * with the given login, or an empty one if no user 
	 * in the database has this login
	 */
	public LinkedHashSet<Author> getAuthorsByLogin(String login){
		String select = "SELECT * FROM " + tableName + 
				"WHERE login = " + login;
		Statement stmt;
		ResultSet rs;
		LinkedHashSet<Author> listAuthors = new LinkedHashSet<>();
		try{
			stmt = forumDataBase.getConnection().createStatement();
			rs = stmt.executeQuery(select);
			while(rs.next()){
				listAuthors.add(new Author(rs.getString("login"), rs.getInt("author_id")));
			}
			stmt.close();
		}catch(SQLException e){
			System.out.println("an error occured when trying to execute an AuthorTable method: + \n" +
					"getAuthorByLogin(String login) with author login = " + login + "\n" + 
					e.getMessage());
		}
		
		return listAuthors;
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
			Statement stmt = forumDataBase.getConnection().createStatement();
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
