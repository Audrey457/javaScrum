package db_interactions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import edit_json_files.Message;
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
		

	}

}
