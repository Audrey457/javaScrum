package databasemodel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

public abstract class AbstractTable {
	protected ForumDataBase forumDataBase;
	protected String tableName;
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * Constructor
	 * @param sdb, an instance of ScrumDataBase
	 * @param tableName, an instance of String
	 */
	public AbstractTable(ForumDataBase forumDataBase, String tableName){
		this.forumDataBase = forumDataBase;
		this.tableName = tableName;
	}
	
	/**
	 * To delete all lines in the table
	 */
	public void deleteAllLines(){
		String sql = "DELETE FROM " + this.tableName + 
				" WHERE 1";
		Statement stmt;
		try{
			stmt = forumDataBase.getConnection().createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			
		} catch(SQLException e){
			logger.fatal(e + "\n" + "Lines not deleted.");
		}
	}
	
	public boolean isEmpty(){
		Statement stmt;
		ResultSet rs;
		int returnValue = 0;
		String sql = "SELECT 1 FROM "
				+ this.tableName + " LIMIT 1";
		try{
			stmt = forumDataBase.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				returnValue = rs.getInt(1);
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e + "\nCan not test " 
					+ "if the table is empty");
		}
		return returnValue == 0;
	}
}
