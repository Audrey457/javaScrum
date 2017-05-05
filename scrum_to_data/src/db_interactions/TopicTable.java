package db_interactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import java_objects.Topic;

/**
 * The representation of a mysql table, containing the topics
 * @author Audrey Loriette
 */
public class TopicTable {
	ForumDataBase sdb;
	String tableName;
	
	/**
	 * Constructor
	 * @param sdb an instance of ScrumDataBase
	 * @param tableName an instance of String
	 * @see ForumDataBase
	 */
	public TopicTable(ForumDataBase sdb, String tableName){
		this.sdb = sdb;
		this.tableName = tableName;
	}
	
	/**
	 * Insert a topic in the tableName (see constructor) of the database
	 * @param topic an insance of Topic
	 * @see Topic
	 */
	public void insertTopic(Topic topic){
		String insert = "INSERT INTO " + tableName
				+ " VALUES (?, ?, ?, ?)";
		PreparedStatement ps;
		try{
			ps = sdb.getConnection().prepareStatement(insert);
			ps.setString(1,  topic.getTitle());
			ps.setString(2, topic.getUrl());
			ps.setInt(3, topic.getId());
			ps.setInt(4, topic.getNbReplies());
			ps.executeUpdate();
			ps.close();
		}catch(SQLException e){
			System.out.println("an error occured when trying to execute a TopicTable method: + \n" +
					"insertTopic(Topic topic) with topic id = " + topic.getId() + "\n" + 
					e.getMessage());
		}
	}
	
	/**
	 * Insert all topics in the tableName (see constructor) of the database
	 * @param topics an instance of ArrayTopics
	 * @see ArrayTopics
	 */
	public void insertAll(ArrayList<Topic> topicsList){
		Topic topic;
		for(int i = 0; i < topicsList.size(); i++){
			topic = topicsList.get(i);
			insertTopic(topic);
		}
	}
	
	/**
	 * See if the tableName (see constructor) contains the given Topic
	 * @param topic an instance of Topic
	 * @return true if the tableName contains the topic, false otherwise
	 */
	public boolean contains(Topic topic){
		String test = "SELECT * FROM " + tableName
				+ " WHERE id=?";
		PreparedStatement stmt;
		ResultSet rs;
		boolean exist = false;
		try{
			stmt = sdb.getConnection().prepareStatement(test);
			stmt.setInt(1, topic.getId());
			rs = stmt.executeQuery();
			exist = rs.next();
			stmt.close();
		}catch(SQLException e){
			System.out.println("an error occured when trying to execute a TopicTable method: + \n" +
					"contains(Topic topic) with topic id = " + topic.getId() + "\n" + 
					e.getMessage());
		}
		return exist;
	}
	
	/**
	 * @param topic, an instance of Topic
	 * @return the number of Replies for the given Topic, -1 if it doesn't exist in the database, or if an SQLException occurred
	 */
	public int getNbReplies(Topic topic){
		String sql = "SELECT nb_replies FROM " + tableName
				+ " WHERE id=" + topic.getId();
		Statement stmt = null;
		ResultSet rs;
		int nbReplies = -1;
		try{
			stmt = sdb.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			rs.next();
			nbReplies = rs.getInt(1);
			stmt.close();
		}catch(SQLException e){
			System.out.println("Method getNbReplies(Topic topic) of TopicTable failed due to an SQL error "
					+ e.getMessage());
		}
		return nbReplies;
	}
	
	/**
	 * update the "nb_replies" field for the given topic
	 * @param topic_id an integer
	 * @param nbReplies an integer
	 */
	public void updateNbReplies(int topic_id, int nbReplies){
		String sql = "UPDATE " + tableName + 
				" SET nb_replies = " + nbReplies + 
				" WHERE id=" + topic_id;
		Statement stmt = null;
		try{
			stmt = sdb.getConnection().createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}catch(SQLException e){
			System.out.println("Method updateNbReplies(int topic_id, int nbReplies) "
					+ "of TopicTable failed due to an SQL error "
					+ e.getMessage());
		}
	}
}
