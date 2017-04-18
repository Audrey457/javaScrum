package db_interactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import edit_json_files.ArrayMessages;
import edit_json_files.ArrayTopics;
import edit_json_files.Message;
import edit_json_files.Topic;
import some_tools.Tools;


public class DemoJdbc {
	
	public static void sauverEnBase(String personne){
		//information to access the database
		String url = "jdbc:mysql://localhost/base_de_test?autoReconnect=true&useSSL=false";
		String login = "root";
		String passwd = "";
		Connection connect = null;
		Statement st = null;
		
		try{
			//Step 1 : load the driver
			Class.forName("com.mysql.jdbc.Driver");
			//step 2 : get the connection
			connect = DriverManager.getConnection(url, login, passwd);
			//step 3 : create the statement
			st = connect.createStatement();
			String sql = "INSERT INTO `personnes` (`nom`) VALUES "
					+ "('" + personne + "')";
			
			//step 4 : request execution
			st.executeUpdate(sql);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		catch (ClassNotFoundException e){
			e.printStackTrace();
		}
		finally{
			try{
				//step 5 : free memory
				connect.close();
				st.close();
			} catch (SQLException e){
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		ScrumDataBase sdb = new ScrumDataBase("jdbc:mysql://localhost/base_de_test?autoReconnect=true&useSSL=false", "root", "");
		ArrayMessages am = new ArrayMessages();
		ArrayTopics at = new ArrayTopics();
		at.add(new Topic(0, "title", "url"));
		am.add(new Message(Tools.stringDateToDateTimeSql("09:57 pm March 8, 2017"), "msg", 0));
		am.add(new Message(Tools.stringDateToDateTimeSql("09:57 pm March 8, 2017"), "msg", 1));
		
		MessageTable mt = new MessageTable(sdb, "messages");
		TopicTable tt = new TopicTable(sdb, "topics");
		
		tt.insertAll(at);
		mt.insertAll(am);
		

	}

}
