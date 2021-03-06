package databasemodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import domainmodel.Author;

/**
 * The representation of a mysql table, containing the authors
 * @author Audrey Loriette
 *
 */
public class AuthorsTable extends AbstractTable{
	
	private final Logger logger = Logger.getLogger(AuthorsTable.class);
	private final String AUTHOR_ID = "authorId";
	private final String LOGIN = "login";
	
	
	/**
	 * Constructor
	 * @param forumDataBase an instance of ForumDataBase
	 * @param tableName an instance of String
	 * @see ForumDataBase
	 */
	public AuthorsTable(ForumDataBase forumDataBase, String tableName) {
		super(forumDataBase, tableName);
	}
	
	/**
	 * Insert an author in the tableName (see constructor) table
	 * @param author an instance of Author
	 * @see Author
	 */
	public void insertAuthor(Author author){
		if(this.contains(author.getAuthorId()) == null){
			String insert = "INSERT INTO " + tableName 
				+" (" + this.AUTHOR_ID + ", " + this.LOGIN + ") "
				+ " VALUES (?, ?)";
			PreparedStatement stmt;
			try{
				stmt = forumDataBase.getConnection().prepareStatement(insert);
				stmt.setInt(1, author.getAuthorId());
				stmt.setString(2, author.getLogin());
				stmt.executeUpdate();
				stmt.close();
				logger.info("Author: " + author.getLogin() + " inserted");
			}catch(SQLException e){
				logger.error(e + "\nAuthor id = " 
						+ author.getAuthorId() + " not inserted");
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
	public Set<Author> getAuthorsByLogin(String login){
		String select = "SELECT * FROM " + tableName + 
				"WHERE " + this.LOGIN + " = " + login;
		Statement stmt;
		ResultSet rs;
		LinkedHashSet<Author> listAuthors = new LinkedHashSet<>();
		try{
			stmt = forumDataBase.getConnection().createStatement();
			rs = stmt.executeQuery(select);
			while(rs.next()){
				listAuthors.add(new Author(rs.getString(this.LOGIN), rs.getInt(this.AUTHOR_ID)));
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e);
		}
		
		return listAuthors;
	}
	
	
	/**
	 * @param login, the author login
	 * @return an instance of Author, the author with the given id if it is in the table, null if not 
	 */
	public Author contains(int id){
		String sql = "SELECT * FROM " + tableName + 
				" WHERE " + this.AUTHOR_ID + " = " + id;
		Author author = null;
		try{
			Statement stmt = forumDataBase.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()){
				author = new Author(rs.getString(this.LOGIN), rs.getInt(this.AUTHOR_ID));
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e);
		}
		return author;
	}
}
