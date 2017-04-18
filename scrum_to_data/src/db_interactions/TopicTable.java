package db_interactions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import edit_json_files.ArrayTopics;
import edit_json_files.Topic;

public class TopicTable {
	ScrumDataBase sdb;
	String tableName;
	
	public TopicTable(ScrumDataBase sdb, String tableName){
		this.sdb = sdb;
		this.tableName = tableName;
	}
	
	public void insertTopic(Topic topic) throws SQLException{
		String insert = "INSERT INTO " + tableName
				+ " VALUES (?, ?, ?)";
		PreparedStatement ps = sdb.getConnection().prepareStatement(insert);
		ps.setString(1,  topic.getTitle());
		ps.setString(2, topic.getUrl());
		ps.setInt(3, topic.getId());
		ps.executeUpdate();
		System.out.println("Topic : " + topic.getId() + " inserted");
		ps.close();
	}
	
	public void insertAll(ArrayTopics topics) throws SQLException{
		Topic topic;
		for(int i = 0; i < topics.size(); i++){
			topic = topics.get(i);
			insertTopic(topic);
		}
	}
}
