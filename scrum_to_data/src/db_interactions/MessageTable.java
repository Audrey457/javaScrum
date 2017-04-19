package db_interactions;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import edit_json_files.ArrayMessages;
import edit_json_files.Message;

public class MessageTable {
	ScrumDataBase sdb;
	String tableName;
	
	public MessageTable(ScrumDataBase sdb, String tableName){
		this.sdb = sdb;
		this.tableName = tableName;
	}
	
	public void insertMessage(Message message) throws SQLException{
		String insert = "INSERT INTO " + tableName 
				+" (date_msg, msg, topic_id) "
				+ " VALUES (?, ?, ?)";
		PreparedStatement ps = sdb.getConnection().prepareStatement(insert);
		ps.setString(1,  message.getDate_message());
		ps.setString(2, message.getMsg());
		ps.setInt(3, message.getId_post());
		ps.executeUpdate();
		System.out.println("Message : " + message.getId_post() + " inserted");
		ps.close();
	}
	
	public void insertAll(ArrayMessages messages) throws SQLException{
		Message m;
		for(int i = 0; i < messages.size(); i++){
			m = messages.get(i);
			insertMessage(m);
		}
	}
}
