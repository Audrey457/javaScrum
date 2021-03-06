package databasemodel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import domainmodel.Topic;

/**
 * The representation of a mysql table, containing the topics
 * @author Audrey Loriette
 */
public class TopicsTable extends AbstractTable {
	
	private final String ID = "id";
	private final String TITLE = "title";
	private final String URL = "url";
	private final String NB_REPLIES = "nbReplies";
	
	/**
	 * Constructor
	 * @param forumDataBase an instance of ScrumDataBase
	 * @param tableName an instance of String
	 * @see ForumDataBase
	 */
	public TopicsTable(ForumDataBase forumDataBase, String tableName){
		super(forumDataBase, tableName);
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
			ps = forumDataBase.getConnection().prepareStatement(insert);
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
				+ " WHERE " + this.ID + " = ?";
		PreparedStatement stmt;
		ResultSet rs;
		boolean exist = false;
		try{
			stmt = forumDataBase.getConnection().prepareStatement(test);
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
		String sql = "SELECT " + this.NB_REPLIES + " FROM " + tableName
				+ " WHERE " + this.ID + " = " + topic.getId();
		Statement stmt = null;
		ResultSet rs;
		int nbReplies = -1;
		try{
			stmt = forumDataBase.getConnection().createStatement();
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
				" SET " + this.NB_REPLIES + " = " + nbReplies + 
				" WHERE " + this.ID + " = " + topicId;
		Statement stmt = null;
		try{
			stmt = forumDataBase.getConnection().createStatement();
			stmt.executeUpdate(sql);
			stmt.close();
		}catch(SQLException e){
			logger.error(e + "\n updateNbReplies failed");
		}
	}
	
	/**
	 * @return an instance of Set/<Topic/>
	 */
	public Set<Topic> getAllTopics(){
		String sql = "SELECT * FROM " + tableName;
		Statement stmt = null;
		ResultSet rs;
		HashSet<Topic> set = new HashSet<>();
		
		try{
			stmt = forumDataBase.getConnection().createStatement();
			rs = stmt.executeQuery(sql);
			while(rs.next()){
				set.add(new Topic(rs.getInt(this.ID),
						rs.getString(this.TITLE), 
						rs.getString(this.URL), 
						rs.getInt(this.NB_REPLIES)));
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			logger.error(e);
		}
		
		return set;
	}
}
