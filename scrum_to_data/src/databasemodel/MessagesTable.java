package databasemodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

import domainmodel.Message;

/**
 * The representation of a mysql table, containing the messages
 * @author Audrey Loriette
 *
 */
public class MessagesTable extends AbstractTable{
	private String MESSAGE_DATE =  "messageDate";
	private String TOPIC_ID = "topicId";
	private String MESSAGE_TEXT = "messageText";
	private String AUTHOR_ID = "authorId";
	private String MESSAGE_ID = "messageId";

	
	/**
	 * Constructor
	 * @param sdb, an instance of ScrumDataBase
	 * @param tableName, an instance of String
	 */
	public MessagesTable(ForumDataBase forumDataBase, String tableName){
		super(forumDataBase, tableName);
	}
	
	/**
	 * Insert a message in the database
	 * Be careful : no checks is carried out !
	 * @param message an instance of Message
	 * @see Message
	 */
	public void insertMessage(Message message){
		String insert = "INSERT INTO " + this.tableName 
				+" (" + this.MESSAGE_DATE + ", " + 
				this.MESSAGE_TEXT + ", " + 
				this.TOPIC_ID + ", " + 
				this.AUTHOR_ID + ") "
				+ " VALUES (?, ?, ?, ?)";
		PreparedStatement ps;
		try{
			ps = forumDataBase.getConnection().prepareStatement(insert);
			ps.setString(1,  message.getMessageDate());
			ps.setString(2, message.getMsg());
			ps.setInt(3, message.getTopicId());
			ps.setInt(4, message.getAuthorId());
			ps.executeUpdate();
			ps.close();
			logger.info("Message from: " + message.getMessageDate() + " topic id: " + message.getTopicId() + " inserted");
		}catch(SQLException e){
			logger.error(e + "\nError when trying "
					+ "to insert message from "
					+ message.getMessageDate());
		}
		
	}
	
	/**
	 * Insert all the messages in the database
	 * Be careful : no check is carried out !
	 * @param messages an instance of ArrayMessages
	 */
	public void insertAll(List<Message> messagesList){
		Message m;
		for(int i = 0; i < messagesList.size(); i++){
			m = messagesList.get(i);
			insertMessage(m);
		}
	}
	
	/**
	 * To delete all lines in the MessagesTable
	 */
	public void deleteMessages(){
		String sql = "DELETE FROM " + this.tableName + 
				" WHERE 1";
		Statement stmt;
		try{
			stmt = forumDataBase.getConnection().createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
			
		} catch(SQLException e){
			logger.fatal(e + "\n" + "Messages not deleted.");
		}
	}
	
	/**
	 * Return the date of the latest message for the given topic
	 * @param topicId an integer, the topic id for which you want to search the last message date
	 * @return a String
	 */
	public String getLastMessageDate(int topicId){
		String sql = "SELECT " + this.MESSAGE_DATE
				+ " FROM " + this.tableName
				+ " WHERE " + this.TOPIC_ID + " = " + topicId 
				+ " ORDER BY " + this.MESSAGE_DATE + " DESC";
		String date = "";
		Statement stmt;
		ResultSet rs;
		
		try{
			stmt = forumDataBase.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			if(rs.next()){
				date = rs.getString(1);;
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e + "\nCan not get the " 
					+ "last message date of this topic: "
					+ topicId);
		}
		
		return date;
	}
	
	public List<Message> getAllMessagesOfATopic(int topicId){
		Statement stmt;
		ResultSet rs;
		String sql = "SELECT *"
				+ " FROM " + this.tableName
				+ " WHERE " + this.TOPIC_ID + " = " + topicId 
				+ " ORDER BY " + this.MESSAGE_DATE + " DESC";
		ArrayList<Message> messagesList = new ArrayList<>();
		
		try{
			stmt = forumDataBase.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				messagesList.add(new Message(rs.getString(this.MESSAGE_DATE), 
						rs.getString(this.MESSAGE_TEXT), rs.getInt(this.TOPIC_ID), 
						rs.getInt(this.AUTHOR_ID), rs.getInt(this.MESSAGE_ID)));
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e + "\nCan not get the " 
					+ "messages list of this topic: "
					+ topicId);
		}
		
		return messagesList;
	}
}
