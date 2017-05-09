package databasemodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.log4j.Logger;

import domainmodel.Topic;

/**
 * The representation of a mysql table, containing the topics
 * @author Audrey Loriette
 */
public class TopicTable {
	ForumDataBase sdb;
	String tableName;
	private final Logger logger = Logger.getLogger(TopicTable.class);
	
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
	 * @param topic an instance of Topic
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
			logger.info("Topic: " + topic.getId() + " inserted");
		}catch(SQLException e){
			logger.error(e + "\nCan not insert topic: "
					+ topic.getId());
		}
	}
	
	/**
	 * Insert all topics in the tableName (see constructor) of the database
	 * @param topics an instance of ArrayTopics
	 * @see ArrayTopics
	 */
	public void insertAll(List<Topic> topicsList){
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
			logger.error(e);;
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
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e + "\nCan not get the number of replies of "
					+ "topic: " + topic.getId());
		}
		return nbReplies;
	}
	
	/**
	 * update the "nb_replies" field for the given topic
	 * @param topicId an integer
	 * @param nbReplies an integer
	 */
	public void updateNbReplies(int topicId, int nbReplies){
		String sql = "UPDATE " + tableName + 
				" SET nb_replies = " + nbReplies + 
				" WHERE id=" + topicId;
		Statement stmt = null;
		try{
			stmt = sdb.getConnection().createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}catch(SQLException e){
			logger.error(e + "\n updateNbReplies failed");
		}
	}
}
