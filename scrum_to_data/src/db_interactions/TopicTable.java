package db_interactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java_objects.ArrayTopics;
import java_objects.Topic;

public class TopicTable {
	ScrumDataBase sdb;
	String tableName;
	
	public TopicTable(ScrumDataBase sdb, String tableName){
		this.sdb = sdb;
		this.tableName = tableName;
	}
	
	public void insertTopic(Topic topic) throws SQLException{
		String insert = "INSERT INTO " + tableName
				+ " VALUES (?, ?, ?, ?)";
		PreparedStatement ps = sdb.getConnection().prepareStatement(insert);
		ps.setString(1,  topic.getTitle());
		ps.setString(2, topic.getUrl());
		ps.setInt(3, topic.getId());
		ps.setInt(4, topic.getNbReplies());
		ps.executeUpdate();
		ps.close();
		
	}
	
	public void insertAll(ArrayTopics topics) throws SQLException{
		Topic topic;
		for(int i = 0; i < topics.size(); i++){
			topic = topics.get(i);
			insertTopic(topic);
		}
	}
	
	public boolean contains(Topic topic) throws SQLException{
		String test = "SELECT * FROM " + tableName
				+ " WHERE id=?";
		PreparedStatement stmt = sdb.getConnection().prepareStatement(test);
		stmt.setInt(1, topic.getId());
		ResultSet rs = stmt.executeQuery();
		boolean exist = rs.next();
		stmt.close();
		
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
