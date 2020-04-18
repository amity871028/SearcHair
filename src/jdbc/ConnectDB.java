package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectDB {

	private Connection con = null; // Database objects
	private Statement stat = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	
	public ConnectDB() {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/searchair?characterEncoding=utf-8", "root",
					"29118310");
			this.stat = this.con.createStatement();

		} catch (ClassNotFoundException e) {
			System.out.println("DriverClassNotFound :" + e.toString());
		} catch (SQLException x) {
			System.out.println("Exception :" + x.toString());
		}
	}
	
	private void toClose() {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
			if (stat != null) {
				stat.close();
				stat = null;
			}
			if (pst != null) {
				pst.close();
				pst = null;
			}
		} catch (SQLException e) {
			System.out.println("Close Exception :" + e.toString());
		}
	}
	
	public Connection getConnection() {
		return this.con;
	}
	
	public Statement getStatement() {
		return this.stat;
	}
	
	public void close() {
		toClose();
	}
}
