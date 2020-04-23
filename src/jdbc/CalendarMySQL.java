package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CalendarMySQL {
	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private ResultSet rs = null;
	private int rsInt = 0;

	private String selectCost = "select * from cost";

	public boolean newCost(String account, String time, int category, String kind, int cost, String description,
			String color) {
		try {
			// find max id to know what id will this data has
			long id = 0L;
			rs = stat.executeQuery("select MAX(id) from cost");
			if (rs.next()) {
				id = (long)rs.getInt(1) + 1;
			}
			// insert data
			String insertdbSQL = "insert into cost values (" + id + ", '" + account + "', '" + time + "', '" + category
					+ "', '" + kind + "', '" + cost + "', '" + description + "', '" + color + "')";
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

	public boolean deleteCost(int id) {
		try {
			rsInt = stat.executeUpdate("delete from cost where id = " + id);
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

	public boolean updateCost(int id, String account, String time, int category, String kind, int cost,
			String description, String color) {
		try {
			// using delete and insert instead of update each attribute
			String insertdbSQL = "insert into cost values ('" + id + "', '" + account + "', '" + time + "', '"
					+ category + "', '" + kind + "', '" + cost + "', '" + description + "', '" + color + "')";
			rsInt = stat.executeUpdate("delete from cost WHERE id = " + id);
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

	// String
	public void getCost() {
		try {
			rs = stat.executeQuery(selectCost);
			int tableColumn = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				// get all column value for a row
				for (int i = 1; i <= tableColumn; i++) {
					System.out.println(rs.getString(i));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
	}

	public static void main(String[] args) {
		// CalendarMySQL c = new CalendarMySQL();
		// c.newCost("suara1201fxt@gmail.com", "2020-04-26", 1, "a", 300, "我的洗髮精好臭", "red");
		// c.updateCost(5, "123@gmail.com", "2020-04-27", 0, "a", 2500, "我的洗髮精棒", "blue");
		// c.deleteCost(1);
	}

}
