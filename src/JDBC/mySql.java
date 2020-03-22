package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.org.apache.bcel.internal.generic.LNEG;

public class mySql {
	public Connection con = null; // Database objects
	private Statement stat = null;
	private ResultSet rs = null;
	private int rsInt = 0;
	private PreparedStatement pst = null;

	// private String dropdbSQL = "DROP TABLE User ";

	// private String createdbSQL = "CREATE TABLE User (" + "    id     INTEGER " + "  , name    VARCHAR(20) "
			//+ "  , passwd  VARCHAR(20))";

	// private String insertdbSQL = "insert into User(id,name,passwd) " + "select ifNULL(max(id),0)+1,?,? FROM User";

	private String selectSQL = "select * from users";

	public mySql() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			this.con = DriverManager.getConnection("jdbc:mysql://localhost:3306/searchair?characterEncoding=utf-8", "peggy",
					"searchair");

		} catch (ClassNotFoundException e) {
			System.out.println("DriverClassNotFound :" + e.toString());
		} catch (SQLException x) {
			System.out.println("Exception :" + x.toString());
		}
	}

	private void Close() {
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

	public void SelectTable(String question) {
		try {

			String mysql = " where question='" + question + " '";
			stat = con.createStatement();

			rs = stat.executeQuery(selectSQL + mysql);
			// System.out.println("ID\t\tQuestion\t\tAnswer");
			while (rs.next()) {
				/*
				 * System.out.println(rs.getInt("id")+"\t\t"+
				 * rs.getString("question")+"\t\t"+rs.getString("answer"));
				 */
				System.out.println(rs.getString("answer"));
			}
		} catch (SQLException e) {
			System.out.println("DropDB Exception :" + e.toString());
		} finally {
			Close();
		}
	}

	public int insertUser(String account, String password, String name) {
		try {
			String insertdbSQL = "insert into users (account, password, name) VALUES (" + account + ", " + password + ", " + name + ")";
			System.out.println(insertdbSQL);
			stat = con.createStatement();
			rsInt = stat.executeUpdate(insertdbSQL);
			return rsInt;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return rsInt;
		} finally {
			Close();
		}
	}

	public int checkUser(String account, String password) {
		try {
			String checkDBUser = " where account = " + account;
			password = password.replace("\"", ""); //take out ""
			stat = con.createStatement();
			rs = stat.executeQuery(selectSQL + checkDBUser);
			if (rs.next()) {
				String correctPwd = rs.getString("password");
				if (correctPwd.equals(password)) { // compare password
					// System.out.println("yes");
					return 200;
				} else {
					// System.out.println("no");
					rsInt = 401;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			Close();
		}
		return rsInt;
	}


	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		mySql test = new mySql();
		String myJSONString = "{'account': 'pegylee112', 'name': 'peipei', 'password': '1234567'}";

		// sign up
		
		// int result = test.register(myJSONString); 
		// System.out.println(result);
		
		// sign in
		/*
		 * int result = test.signIn(myJSONString); System.out.println(result);
		 */
	}
}

