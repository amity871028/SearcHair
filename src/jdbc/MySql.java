package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MySql {

	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();

	private ResultSet rs = null;
	private int rsInt = 0;
	private boolean result = false;

	private String selectSQL = "select * from users";

	public MySql() {

	}

	public boolean userInsertion(String account, String password, String name) {
		try {
			String insertdbSQL = "insert into users (account, password, name) VALUES (" + account + ", " + password
					+ ", " + name + ")";
			System.out.println(insertdbSQL);
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

	public boolean userChecking(String account, String password) {
		try {
			String checkDBUser = " where account = " + account;
			password = password.replace("\"", ""); // take out ""
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
			database.close();
		}
		return result;
	}

	public boolean userCertification(String account, String name) {
		try {
			String checkDBUser = " where account = " + account;
			name = name.replace("\"", ""); // take out ""

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

	public boolean userResetPwd(String account, String password) {
		try {
			account = account.replace("\"", ""); // take out ""
			password = password.replace("\"", ""); // take out ""
			String updatePwd = "UPDATE users SET password = '" + password + "'  WHERE account = '" + account + "'";
			rsInt = stat.executeUpdate(updatePwd);
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
