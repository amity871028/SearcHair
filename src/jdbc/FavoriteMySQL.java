package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.jdbc.PreparedStatement;

import favorite.Album;
import favorite.AlbumPhoto;
import hairMatch.Photo;
import search.AllSalon;

public class FavoriteMySQL {

	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private Connection con = database.getConnection();
	private ResultSet rs = null;

	public String getSalonId(String account) {
		String salon = null;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select salon from favorite where account =" + "\"" + account + "\"");
			if (rs.next()) {
				salon = rs.getString("salon");
				if (salon.equals(" ")) {
					salon = "[]";
				}
			} else {
				salon = "[]";
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return "{\"id\":" + salon + "}";
	}

	public String getStylistId(String account) {
		String stylist = null;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select stylist from favorite where account =" + "\"" + account + "\"");
			if (rs.next()) {
				stylist = rs.getString("stylist");
				if (stylist.equals(" ")) {
					stylist = "[]";
				}
			} else {
				stylist = "[]";
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return "{\"id\":" + stylist + "}";
	}

	public String getStylistWorksId(String account) {
		String stylist_works = null;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select stylist_works from favorite where account =" + "\"" + account + "\"");
			if (rs.next()) {
				stylist_works = rs.getString("stylist_works");
				if (stylist_works.equals(" ")) {
					stylist_works = "[]";
				}
			} else {
				stylist_works = "[]";
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return "{\"id\":" + stylist_works + "}";
	}

	public String getAlbum(String account) {
		String ans = null;
		ArrayList<Album> albumList = new ArrayList<Album>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from album_names where account =" + "\"" + account + "\"");
			while (rs.next()) {
				Album album = new Album();
				album.setID(rs.getInt("album_id"));
				album.setName(rs.getString("name"));
				albumList.add(album);
			}
			Album output = new Album();
			ans = output.convertToJson(albumList);
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public String getPhoto(String account, int albumId) {
		String ans = null;
		ArrayList<AlbumPhoto> photoList = new ArrayList<AlbumPhoto>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from user_photos where album_id =" + albumId);
			while (rs.next()) {
				AlbumPhoto photo = new AlbumPhoto();
				photo.setID(rs.getInt("id"));
				photo.setDescription(rs.getString("description"));
				photo.setPhoto(rs.getString("photo"));
				photoList.add(photo);
			}
			AlbumPhoto output = new AlbumPhoto();
			ans = output.convertToJson(photoList);
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

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
						salon = "[" + String.valueOf(salonID) + "]";
					else if (salon.equals("[]"))
						salon = salon.substring(0, salon.length() - 1) + String.valueOf(salonID) + "]";
					else
						salon = salon.substring(0, salon.length() - 1) + "," + String.valueOf(salonID) + "]";
					String update = "update favorite set salon=? where account=?";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(update);
					pst.setString(1, salon); // 傳送第1個參數(取代第一個問號)
					pst.setString(2, account); // 傳送第2個參數(取代第一個問號)
					pst.executeUpdate();
				} else { // 第一次加到我的最愛
					String insert = "insert into favorite(account,salon,stylist,stylist_works,product) value(?,?,?,?,?)";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(insert);
					pst.setString(1, account);
					pst.setString(2, "[" + String.valueOf(salonID) + "]");
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
						stylist = "[" + String.valueOf(stylistID) + "]";
					else if (stylist.equals("[]"))
						stylist = stylist.substring(0, stylist.length() - 1) + String.valueOf(stylistID) + "]";
					else
						stylist = stylist.substring(0, stylist.length() - 1) + "," + String.valueOf(stylistID) + "]";
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
					pst.setString(3, "[" + String.valueOf(stylistID) + "]");
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
						stylist_works = "[" + String.valueOf(stylist_works_ID) + "]";
					else if (stylist_works.equals("[]"))
						stylist_works = stylist_works.substring(0, stylist_works.length() - 1)
								+ String.valueOf(stylist_works_ID) + "]";
					else
						stylist_works = stylist_works.substring(0, stylist_works.length() - 1) + ","
								+ String.valueOf(stylist_works_ID) + "]";
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
					pst.setString(3, "[" + String.valueOf(stylist_works_ID) + "]");
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

	public boolean addAlbum(String account, String albumName) {
		int count = 0; // 計算資料庫內有幾筆資料
		int flag = 1; // 正確的輸出1 錯誤輸出0
		int album_id;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from album_names");
			while (rs.next()) {
				if (rs.getInt("album_id") > count)
					count = rs.getInt("album_id");
			}

			Statement ST = null;
			ResultSet RS = null;
			ST = con.createStatement();
			RS = ST.executeQuery("select * from album_names where account=" + "\"" + account + "\"");
			while (RS.next()) { // 找某個使用者擁有的相簿 確認有無重複名稱
				if (RS.getString("name").equals(albumName)) {
					flag = 0; // 有此相簿名稱了
					System.out.println("已經有這個相簿囉");
				}
			}

			if (flag == 1) {
				album_id = count + 1;
				String insert = "insert into album_names(account,album_id,name) value(?,?,?)";
				PreparedStatement pst = (PreparedStatement) con.prepareStatement(insert);
				pst.setString(1, account);
				pst.setInt(2, album_id);
				pst.setString(3, albumName);
				pst.executeUpdate();
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

	public boolean addPhoto(String account, int albumId, String description, String picture) {
		int count = 0; // 計算資料庫內有幾筆資料
		int flag = 1; // 正確的輸出1 錯誤輸出0
		int id;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from user_photos");
			while (rs.next()) {
				if (rs.getInt("id") > count)
					count = rs.getInt("id");
			}

			id = count + 1;
			String insert = "insert into user_photos(id,account,album_id,description,photo) value(?,?,?,?,?)";
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(insert);
			pst.setInt(1, id);
			pst.setString(2, account);
			pst.setInt(3, albumId);
			pst.setString(4, description);
			pst.setString(5, picture);
			pst.executeUpdate();
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

	public boolean deleteSalon(String account, int num) {
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
					salon = salon.replace("" + salonID + "", "");
					salon = salon.replace(",,", ",");
					salon = salon.replace("[,", "[");
					salon = salon.replace(",]", "]");

					String update = "update favorite set salon=? where account=?";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(update);
					pst.setString(1, salon); // 傳送第1個參數(取代第一個問號)
					pst.setString(2, account); // 傳送第2個參數(取代第一個問號)
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

	public boolean deleteStylist(String account, int num) {
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
					stylist = stylist.replace("" + stylistID + "", "");
					stylist = stylist.replace(",,", ",");
					stylist = stylist.replace("[,", "[");
					stylist = stylist.replace(",]", "]");

					String update = "update favorite set stylist=? where account=?";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(update);
					pst.setString(1, stylist); // 傳送第1個參數(取代第一個問號)
					pst.setString(2, account); // 傳送第2個參數(取代第一個問號)
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

	public boolean deleteStylistWorks(String account, int num) {
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
					stylist_works = stylist_works.replace("" + stylist_works_ID + "", "");
					stylist_works = stylist_works.replace(",,", ",");
					stylist_works = stylist_works.replace("[,", "[");
					stylist_works = stylist_works.replace(",]", "]");

					String update = "update favorite set stylist_works=? where account=?";
					PreparedStatement pst = (PreparedStatement) con.prepareStatement(update);
					pst.setString(1, stylist_works); // 傳送第1個參數(取代第一個問號)
					pst.setString(2, account); // 傳送第2個參數(取代第一個問號)
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

	public boolean deleteAlbum(String account, int num) {
		try {
			String delete = "delete from album_names where album_id=?";
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(delete);
			pst.setInt(1, num); // 傳送第1個參數(取代第一個問號)
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return true;
	}

	public boolean deletePhoto(String account, int num) {
		try {
			String delete = "delete from user_photos where id=?";
			PreparedStatement pst = (PreparedStatement) con.prepareStatement(delete);
			pst.setInt(1, num); // 傳送第1個參數(取代第一個問號)
			pst.executeUpdate();
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return true;
	}

	public static void main(String args[]) {
		FavoriteMySQL test = new FavoriteMySQL();
		String account = "711";
		String albumName = "oh";
		int albumId = 1;
		String description = "uh";
		String picture = "jj";
		int id = 2;

//		boolean salon = test.addSalon(account, 1);
//		System.out.println("salon boolean -> " + salon);
//		boolean stylist = test.addStylist(account, 14);
//		System.out.println("stylist boolean -> " + stylist);
//		boolean stylistWorks = test.addStylistWorks(account, 21);
//		System.out.println("stylistWorks boolean -> " + stylistWorks);
//		boolean album = test.addAlbum(account, albumName);
//		System.out.println("album boolean -> " + album);
//		boolean photo = test.addPhoto(account, albumId, description, picture);
//		System.out.println("photo boolean -> " + photo);

//		boolean salon = test.deleteSalon(account, 1);
//		System.out.println("salon boolean -> " + salon);
//		boolean stylist = test.deleteStylist(account, 11);
//		System.out.println("stylist boolean -> " + stylist);
//		boolean stylistWorks = test.deleteStylistWorks(account, 23);
//		System.out.println("stylistWorks boolean -> " + stylistWorks);
//		boolean album = test.deleteAlbum(account, albumId);
//		System.out.println("album boolean -> " + album);
//		boolean photo = test.deletePhoto(account, id);
//		System.out.println("photo boolean -> " + photo);

//		String salon = test.getSalonId(account);
//		System.out.println("salon -> " + salon);
//		String stylist = test.getStylistId(account);
//		System.out.println("stylist -> " + stylist);
//		String stylistWorks = test.getStylistWorksId(account);
//		System.out.println("stylistWorks -> " + stylistWorks);
//		String album = test.getAlbum(account);
//		System.out.println("album -> " + album);
//		String photo = test.getPhoto(account,albumId);
//		System.out.println("photo -> " + photo);
	}

}
