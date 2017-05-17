package databasemodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

public class KeyWordsTable extends AbstractTable {
	
	private final String KEY_WORD = "keyword";

	/**
	 * Constructor
	 * @param forumDataBase an instance of ForumDataBase
	 * @param tableName an instance of String
	 */
	public KeyWordsTable(ForumDataBase forumDataBase, String tableName) {
		super(forumDataBase, tableName);
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @param keyWord an instance of String
	 */
	public void insertKeyWord(String keyWord){
		if(!contains(keyWord)){
			String insert = "INSERT INTO " + tableName 
				+ " VALUES (?)";
			PreparedStatement stmt;
			try{
				stmt = forumDataBase.getConnection().prepareStatement(insert);
				stmt.setString(1, keyWord);
				stmt.executeUpdate();
				stmt.close();
				logger.info("Key word: " + keyWord + " inserted");
			}catch(SQLException e){
				logger.error(e + "\nKey word: " + keyWord + " not inserted");
			}
		}else{
			logger.info("Key word: " + keyWord + " already in the table");
		}
	}
	
	/**
	 * @param keyWordsList an instance of Set/<String/>
	 */
	public void insertKeyWordsList(Set<String> keyWordsList){
		for(String s: keyWordsList){
			insertKeyWord(s);
		}
	}
	
	/**
	 * @param keyWord an instance of String
	 * @return true if the table contains the given key word, 
	 * false otherwise
	 */
	public boolean contains(String keyWord){
		String sql = "SELECT * FROM " + tableName + 
				" WHERE " + this.KEY_WORD + " = '" + keyWord + "'";
		boolean cont = false;
		try{
			Statement stmt = forumDataBase.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			cont=rs.next();
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e);
		}
		return cont;
	}

}
