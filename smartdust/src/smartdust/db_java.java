package smartdust;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class db_java {
	private Connection conn;
	private Statement stmt;
	private static String username = "root";
	private static String password = "";
	private static int vid = 1;
	db_java(){
		 
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/smt", db_java.username, db_java.password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void createDb() {
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("show tables like 'bins'");
			if(rs.next()) {
				
			}
			else {
				this.createTable();
				System.out.println("fdf");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void saveStatus(String str) throws ParseException {
		JSONObject jo = null;
		jo = (JSONObject) new JSONParser().parse(str);
		/*if((JSOM) {
			
		}*/
		String binId = (String) jo.get("binId");
		String binStatus = (String) jo.get("binStatus");
		
		String st = "insert into bin_status (bin_id,binStatus,uid,time) values ("+ binId +","+ binStatus +",'',CURRENT_TIMESTAMP)";
		try {
			stmt = conn.createStatement();
			stmt.execute(st);
			st = "update bins set status= '"+ binStatus +"' where id=  1";
			stmt.execute(st);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void createTable() throws SQLException {
		String str = "create table bins ("
				+ "id INT(30) UNSIGNED AUTO_INCREMENT PRIMARY KEY,"
				+ "location varchar(50) NOT NULL,"
				+ "status varchar(10) NOT NULL);";
		stmt = conn.createStatement();
		stmt.execute(str);
		str = "create table user("
				+ "id INT(30) UNSIGNED AUTO_INCREMENT PRIMARY KEY,"
				+ "uid varchar(30) unique,"
				+ "username varchar(50) NOT NULL,"
				+ "mobno varchar(10) unique)";
		stmt.execute(str);
		str = "create table bin_status("
				+ "id INT(30) UNSIGNED AUTO_INCREMENT PRIMARY KEY,"
				+ "bin_id int(30) UNSIGNED,"
				+ "binstatus varchar(5),"
				+ "uid varchar(30),"
				+ "time timestamp)";
		stmt.execute(str);
		str = "create table credit("
				+ "id INT(30) UNSIGNED AUTO_INCREMENT PRIMARY KEY,"
				+ "uid varchar(30),"
				+ "credit varchar(10),"
				+ "FOREIGN KEY (uid) REFERENCES user(uid))";
		stmt.execute(str);
	}
	
	public void changeadd() {
		db_java.vid++;
		if(db_java.vid==3) {
			db_java.vid=1;
		}
		String str = "update updt Set video = "+db_java.vid+" where id=1";
		try {
			stmt = conn.createStatement();
			stmt.execute(str);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
