package db_interactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java_objects.ArrayMessages;
import java_objects.Message;
import some_tools.Tools;

/**
 * The representation of a mysql table, containing the messages
 * @author Audrey Loriette
 *
 */
public class MessageTable {
	private ScrumDataBase sdb;
	private String tableName;
	
	/**
	 * Constructor
	 * @param sdb, an instance of ScrumDataBase
	 * @param tableName, an instance of String
	 */
	public MessageTable(ScrumDataBase sdb, String tableName){
		this.sdb = sdb;
		this.tableName = tableName;
	}
	
	/**
	 * Insert a message in the database
	 * Be careful : no checks is carried out !
	 * @param message an instance of Message
	 * @see Message
	 */
	public void insertMessage(Message message){
		String insert = "INSERT INTO " + tableName 
				+" (date_msg, msg, topic_id, author_id) "
				+ " VALUES (?, ?, ?, ?)";
		String formattedDate = Tools.stringDateToDateTimeSql(message.getDate_message());
		PreparedStatement ps;
		try{
			ps = sdb.getConnection().prepareStatement(insert);
			ps.setString(1,  formattedDate);
			ps.setString(2, message.getMsg());
			ps.setInt(3, message.getId_post());
			ps.setInt(4, message.getAuthor_id());
			ps.executeUpdate();
			ps.close();
		}catch(SQLException e){
			System.out.println("an error occured when trying to execute a MessageTable method: + \n" +
					"insertMessage(Message message) with message date = " + message.getDate_message() + "\n" + 
					e.getMessage());
		}
		
	}
	
	/**
	 * Insert all the messages in the database
	 * Be careful : no checks is carried out !
	 * @param messages an instance of ArrayMessages
	 */
	public void insertAll(ArrayMessages messages){
		Message m;
		for(int i = 0; i < messages.size(); i++){
			m = messages.get(i);
			insertMessage(m);
		}
	}
	
	/**
	 * Return the date of the latest message for the given topic
	 * @param topic_id an integer, the topic id for which you want to search the last message date
	 * @return a String
	 */
	public String getLastMessageDate(int topic_id){
		String sql = "SELECT date_msg"
				+ " FROM messages"
				+ " WHERE messages.topic_id = " + topic_id 
				+ " ORDER BY date_msg DESC";
		String date = "";
		Statement stmt;
		ResultSet rs;
		
		try{
			stmt = sdb.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				date = rs.getString(1);;
			}
			stmt.close();
		}catch(SQLException e){
			System.out.println("an error occured when trying to execute a MessageTable method: + \n" +
					"getLastMessageDate(int topic_id) with topic id = " + topic_id + "\n" + 
					e.getMessage());
		}
		
		return date;
	}
}
