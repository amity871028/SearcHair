package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import com.google.gson.Gson;
import com.mysql.jdbc.PreparedStatement;

import hairMatch.Photo;

public class HairMatchMySQL {
	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private Connection con = database.getConnection();
	private ResultSet rs = null;

	public boolean savePhoto(String user_name, String hairstyle, String color, String url) {
		int flag = 0; // 正確的輸出1 錯誤輸出0
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from share_photo where user_name =\"" + user_name + "\" and hairstyle=\""
					+ hairstyle + "\" and color=\"" + color + "\" and url=\"" + url + "\"");
			if (rs.next()) { // 已經有這筆資料了
				System.out.println("資料庫已有這張照片");
			} else { // 新增資料
				String insert = "insert into share_photo(user_name,hairstyle,color,url) value(?,?,?,?)";
				PreparedStatement pst = (PreparedStatement) con.prepareStatement(insert);

				pst.setString(1, user_name);
				pst.setString(2, hairstyle);
				pst.setString(3, color);
				pst.setString(4, url);
				pst.executeUpdate();
				flag = 1;
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

	public String getPhoto(String hairstyle) {
		String user_name = null, color = null, url = null;
		String ans = null;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from share_photo where hairstyle =\"" + hairstyle + "\" order by rand() limit 1");
			if (rs.next()) {
				user_name = rs.getString("user_name");
				color = rs.getString("color");
				url = rs.getString("url");
				Photo photo = new Photo(user_name, color, url);
				Gson gson = new Gson();
				ans = gson.toJson(photo);
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;

	}

	public static void main(String args[]) {
		HairMatchMySQL test = new HairMatchMySQL();
		String user_name = "oTATo";
		String hairstyle = "girl3.png";
		String color = "#555555";
		String url = "https://imgur/current.png";

		boolean save = test.savePhoto(user_name, hairstyle, color, url);
		System.out.println("save boolean -> " + save);
		String photo = test.getPhoto(hairstyle);
		System.out.println("photo -> " + photo);

	}
}
