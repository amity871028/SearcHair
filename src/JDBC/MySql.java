package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySql {
	public Connection con = null; // Database objects
	private Statement stat = null;
	private ResultSet rs = null;
	private int rsInt = 0;
	private PreparedStatement pst = null;
	private boolean result = false;

	// private String dropdbSQL = "DROP TABLE User ";

	// private String createdbSQL = "CREATE TABLE User (" + "    id     INTEGER " + "  , name    VARCHAR(20) "
			//+ "  , passwd  VARCHAR(20))";

	// private String insertdbSQL = "insert into User(id,name,passwd) " + "select ifNULL(max(id),0)+1,?,? FROM User";

	private String selectSQL = "select * from users";

	public MySql() {
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

	private void close() {
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

	public boolean userInsertion(String account, String password, String name) {
		try {
			String insertdbSQL = "insert into users (account, password, name) VALUES (" + account + ", " + password + ", " + name + ")";
			System.out.println(insertdbSQL);
			rsInt = stat.executeUpdate(insertdbSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		if (rsInt == 1) return true;
		else return false;
	}

	public boolean userChecking(String account, String password) {
		try {
			String checkDBUser = " where account = " + account;
			password = password.replace("\"", ""); //take out ""
			rs = stat.executeQuery(selectSQL + checkDBUser);
			if (rs.next()) {
				String correctPwd = rs.getString("password");
				if (correctPwd.equals(password)) { // compare password
					// System.out.println("yes");
					result = true;
				} else {
					// System.out.println("no");
					result = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}
	
	public boolean userCertification(String account, String name) {
		try {
			String checkDBUser = " where account = " + account;
			name = name.replace("\"", ""); //take out ""
			
			rs = stat.executeQuery(selectSQL + checkDBUser);
			if (rs.next()) {
				String userName = rs.getString("name");
				if (userName.equals(name)) { // compare user
					System.out.println("yes");
					result = true;
				} else {
					System.out.println("no");
					result = false;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		return result;
	}
	
	
	public boolean userResetPwd(String account, String password) {
		try {
			account = account.replace("\"", ""); //take out ""
			password = password.replace("\"", ""); //take out ""
			String updatePwd = "UPDATE users SET password = '" + password + "'  WHERE account = '" + account + "'";
			rsInt = stat.executeUpdate(updatePwd);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
		if (rsInt == 1) return true;
		else return false;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MySql test = new MySql();
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

