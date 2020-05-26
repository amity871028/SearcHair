package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.mysql.jdbc.PreparedStatement;

public class FavoriteMySQL {

	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private Connection con = database.getConnection();
	private ResultSet rs = null;

	public boolean addSalon(String account, int num) {
		int salonID;
		int flag = 0; // 正確的輸出1 錯誤輸出0
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from salon where id =" + num);
			if (rs.next()) {
				salonID = rs.getInt("id");

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select * from favorite where account=" + "\"" + account + "\"");
				if (RS.next()) { // 有使用過我的最愛
					String salon = RS.getString("salon");
					if (salon.equals(" ")) // 沒有加過店家在我的最愛
						salon = "[\"" + String.valueOf(salonID) + "\"]";
					else
						salon = salon.substring(0, salon.length() - 1) + ",\"" + String.valueOf(salonID) + "\"]";
					String update = "update favorite set salon=? where account=?";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(update);
					pst.setString(1, salon); // 傳送第1個參數(取代第一個問號)
					pst.setString(2, account); // 傳送第2個參數(取代第一個問號)
					pst.executeUpdate();
				} else { // 第一次加到我的最愛
					String insert = "insert into favorite(account,salon,stylist,stylist_works,product) value(?,?,?,?,?)";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(insert);
					pst.setString(1, account);
					pst.setString(2, "[\"" + String.valueOf(salonID) + "\"]");
					pst.setString(3, " ");
					pst.setString(4, " ");
					pst.setString(5, " ");
					pst.executeUpdate();
				}
				flag = 1;
			} else {
				System.out.println("Salon 不存在");
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		if (flag == 1)
			return true;
		else
			return false;
	}

	public boolean addStylist(String account, int num) {
		int stylistID;
		int flag = 0; // 正確的輸出1 錯誤輸出0
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from stylist where id =" + num);
			if (rs.next()) {
				stylistID = rs.getInt("id");

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select * from favorite where account=" + "\"" + account + "\"");
				if (RS.next()) { // 有使用過我的最愛
					String stylist = RS.getString("stylist");
					if (stylist.equals(" ")) // 沒有加過設計師在我的最愛
						stylist = "[\"" + String.valueOf(stylistID) + "\"]";
					else
						stylist = stylist.substring(0, stylist.length() - 1) + ",\"" + String.valueOf(stylistID) + "\"]";
					String update = "update favorite set stylist=? where account=?";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(update);
					pst.setString(1, stylist); // 傳送第1個參數(取代第一個問號)
					pst.setString(2, account); // 傳送第2個參數(取代第一個問號)
					pst.executeUpdate();
				} else { // 第一次加到我的最愛
					String insert = "insert into favorite(account,salon,stylist,stylist_works,product) value(?,?,?,?,?)";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(insert);
					pst.setString(1, account);
					pst.setString(2, " ");
					pst.setString(3, "[\"" + String.valueOf(stylistID) + "\"]");
					pst.setString(4, " ");
					pst.setString(5, " ");
					pst.executeUpdate();
				}
				flag = 1;
			} else {
				System.out.println("Stylist 不存在");
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		if (flag == 1)
			return true;
		else
			return false;

	}

	public boolean addStylistWorks(String account, int num) {
		int stylist_works_ID;
		int flag = 0; // 正確的輸出1 錯誤輸出0
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from stylist_works where id =" + num);
			if (rs.next()) {
				stylist_works_ID = rs.getInt("id");

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select * from favorite where account=" + "\"" + account + "\"");
				if (RS.next()) { // 有使用過我的最愛
					String stylist_works = RS.getString("stylist_works");
					if (stylist_works.equals(" ")) // 沒有加過設計師作品在我的最愛
						stylist_works = "[\"" + String.valueOf(stylist_works_ID) + "\"]";
					else
						stylist_works = stylist_works.substring(0, stylist_works.length() - 1) + ",\"" + String.valueOf(stylist_works_ID) + "\"]";
					String update = "update favorite set stylist_works=? where account=?";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(update);
					pst.setString(1, stylist_works); // 傳送第1個參數(取代第一個問號)
					pst.setString(2, account); // 傳送第2個參數(取代第一個問號)
					pst.executeUpdate();
				} else { // 第一次加到我的最愛
					String insert = "insert into favorite(account,salon,stylist,stylist_works,product) value(?,?,?,?,?)";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(insert);
					pst.setString(1, account);
					pst.setString(2, " ");
					pst.setString(3, "[\"" + String.valueOf(stylist_works_ID) + "\"]");
					pst.setString(4, " ");
					pst.setString(5, " ");
					pst.executeUpdate();
				}

				flag = 1;
			} else {
				System.out.println("Stylist_Works 不存在");
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		if (flag == 1)
			return true;
		else
			return false;
	}

	public static void main(String args[]) {
		FavoriteMySQL test = new FavoriteMySQL();
		String account = "ohmygod@gmail.com";
		boolean salon = test.addSalon(account, 8);
		System.out.println("salon boolean -> " + salon);
		boolean stylist = test.addStylist(account, 88);
		System.out.println("stylist boolean -> " + stylist);
		boolean stylistWorks = test.addStylistWorks(account, 7);
		System.out.println("stylistWorks boolean -> " + stylistWorks);
	}

}
