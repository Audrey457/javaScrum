package db_interactions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java_objects.ArrayMessages;
import java_objects.Message;
import some_tools.Tools;

public class MessageTable {
	private ScrumDataBase sdb;
	private String tableName;
	
	public MessageTable(ScrumDataBase sdb, String tableName){
		this.sdb = sdb;
		this.tableName = tableName;
	}
	
	public void insertMessage(Message message) throws SQLException{
		String insert = "INSERT INTO " + tableName 
				+" (date_msg, msg, topic_id, author_id) "
				+ " VALUES (?, ?, ?, ?)";
		String formattedDate = Tools.stringDateToDateTimeSql(message.getDate_message());
		PreparedStatement ps = sdb.getConnection().prepareStatement(insert);
		ps.setString(1,  formattedDate);
		ps.setString(2, message.getMsg());
		ps.setInt(3, message.getId_post());
		ps.setInt(4, message.getAuthor_id());
		ps.executeUpdate();
		ps.close();
	}
	
	public void insertAll(ArrayMessages messages) throws SQLException{
		Message m;
		for(int i = 0; i < messages.size(); i++){
			m = messages.get(i);
			insertMessage(m);
		}
	}
	
	public String getLastMessageDate(int topic_id) throws SQLException{
		String sql = "SELECT date_msg"
				+ " FROM messages"
				+ " WHERE messages.topic_id = " + topic_id 
				+ " ORDER BY date_msg DESC";
		Statement stmt = sdb.getConnection().createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		String date = "";
		if(rs.next()){
			date = rs.getString(1);;
		}
		stmt.close();
		
		return date;
	}
}
