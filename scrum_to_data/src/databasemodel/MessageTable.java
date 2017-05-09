package databasemodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import domainmodel.Message;
import sometools.DateTools;

/**
 * The representation of a mysql table, containing the messages
 * @author Audrey Loriette
 *
 */
public class MessageTable {
	private ForumDataBase sdb;
	private String tableName;
	private final Logger logger = Logger.getLogger(MessageTable.class);

	
	/**
	 * Constructor
	 * @param sdb, an instance of ScrumDataBase
	 * @param tableName, an instance of String
	 */
	public MessageTable(ForumDataBase sdb, String tableName){
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
		PreparedStatement ps;
		try{
			ps = sdb.getConnection().prepareStatement(insert);
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
	 * Return the date of the latest message for the given topic
	 * @param topicId an integer, the topic id for which you want to search the last message date
	 * @return a String
	 */
	public String getLastMessageDate(int topicId){
		String sql = "SELECT date_msg"
				+ " FROM messages"
				+ " WHERE messages.topic_id = " + topicId 
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
				+ " FROM messages"
				+ " WHERE messages.topic_id = " + topicId 
				+ " ORDER BY date_msg DESC";
		ArrayList<Message> messagesList = new ArrayList<>();
		
		try{
			stmt = sdb.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				messagesList.add(new Message(rs.getString("date_msg"), 
						rs.getString("msg"), rs.getInt("topic_id"), 
						rs.getInt("author_id"), rs.getInt("id_msg")));
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
