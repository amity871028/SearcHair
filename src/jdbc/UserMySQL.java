package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserMySQL {
	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private ResultSet rs = null;
	private int rsInt = 0;
	private boolean result = false;

	private String selectSQL = "select * from users";

	public boolean userInsertion(String account, String password, String name) {
		try {
			String insertdbSQL = "insert into users (account, password, name) VALUES (" + account + ", " + password
					+ ", " + name + ")";
			rsInt = stat.executeUpdate(insertdbSQL);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public String userChecking(String account, String password) {
		String userName = null;
		try {
			String checkDBUser = " select * from users where account = " + account;
			rs = stat.executeQuery(checkDBUser);
			if (rs.next()) {
				String correctPassword = rs.getString("password");
				if (correctPassword.equals(password)) { // compare password
					userName = rs.getString("name");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return userName;
	}

	public boolean userCertification(String account, String name) {
		try {
			String checkDBUser = " where account = " + account;

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
			database.close();
		}
		return result;
	}

	public boolean userResetPassword(String account, String password) {
		try {
			String updatePassword = "UPDATE users SET password = '" + password + "'  WHERE account = " + account;
			rsInt = stat.executeUpdate(updatePassword);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}
	
	public boolean userSetRemind(String account, int remindFrequency) {
		try {
			String updateRemind = "UPDATE users SET remind_frequency = " + remindFrequency + " WHERE account = " + account;
			rsInt = stat.executeUpdate(updateRemind);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}
}
