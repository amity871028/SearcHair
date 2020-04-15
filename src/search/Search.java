package search;

import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.net.*;
import java.io.*;
import com.google.gson.Gson;

public class Search {
	private Connection conn = null;
	private Statement stat = null;
	private ResultSet rs = null;
	private PreparedStatement pst = null;
	ArrayList<String> salonList = new ArrayList<String>();
	ArrayList<String> stylists = new ArrayList<String>();
	ArrayList<String> stylistWorks = new ArrayList<String>();
	ArrayList<String> infolist = new ArrayList<String>();
	ArrayList<String> worklist = new ArrayList<String>();
	ArrayList<String> servicelist = new ArrayList<String>();
	ArrayList<String> salonServiceList = new ArrayList<String>();
	ArrayList<String> allServicelist = new ArrayList<String>();

	public Search() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); // register driver
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/searchair?characterEncoding=utf-8", "root",
					"12345678");
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException:" + e.toString());
		} catch (SQLException e) {
			System.out.println("SQLException:" + e.toString());
		}
	}

	public ArrayList<String> searchSalon() {
		System.out.println("輸出所有店家");
		salonList.clear();
		int salonID, stylistID;
		String name, address, phone, businessTime, picture, serName;
		Gson gson = new Gson();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from salon");
			while (rs.next()) {
				salonServiceList.clear();
				// System.out.println(rs.getInt("id")+"\t"+rs.getString("name")+"\t"+rs.getString("address")+"\t"+rs.getString("phone")+"\t"+rs.getString("businessTime")+"\t"+rs.getString("picture"));
				salonID = rs.getInt("id");
				name = rs.getString("name");
				address = rs.getString("address");
				phone = rs.getString("phone");
				businessTime = rs.getString("businessTime");
				picture = rs.getString("picture");

				Statement ST = null;
				ResultSet RS = null;
				ST = conn.createStatement();
				RS = ST.executeQuery("select id from stylist where salon=" + salonID);
				while (RS.next()) { // 搜尋某店家裡的所有設計師
					stylistID = RS.getInt("id");

					Statement stt = null;
					ResultSet rst = null;
					stt = conn.createStatement();
					rst = stt.executeQuery("select name from service where stylist=" + stylistID);
					while (rst.next()) { // 搜尋某設計師有提供的服務
						serName = rst.getString("name");
						salonServiceList.add(serName);
					}
				}
				LinkedHashSet<String> set = new LinkedHashSet<String>(salonServiceList); // 把arraylist內重複的元素刪掉
				ArrayList<String> newServiceList = new ArrayList<String>(set);

				Salon salon = new Salon(salonID, name, address, phone, businessTime, picture, newServiceList);
				String jsonObject = gson.toJson(salon);
				salonList.add(jsonObject);
			}
			System.out.println(salonList);

		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			close();
		}
		return salonList;
	}

	public ArrayList<String> searchStylist() {
		System.out.println("輸出所有設計師");
		int id, salon;
		String name, job_title, address = null, picture;
		String answer = "[";
		Gson gson = new Gson();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from stylist");
			while (rs.next()) {
				allServicelist.clear();
				id = rs.getInt("id");
				name = rs.getString("name");
				job_title = rs.getString("job_title");
				salon = rs.getInt("salon");
				picture = rs.getString("picture");

				Statement ST = null;
				ResultSet RS = null;
				ST = conn.createStatement();
				RS = ST.executeQuery("select address from salon where id=" + salon);
				if (RS.next()) {
					address = RS.getString("address");
				}

				String serName, minPrice, maxPrice, service_time, serDescription;
				Statement stt = null;
				ResultSet rst = null;
				stt = conn.createStatement();
				rst = stt.executeQuery("select * from service where stylist=" + id);
				while (rst.next()) {
					serName = rst.getString("name");
					minPrice = Integer.toString(rst.getInt("min_price"));
					maxPrice = Integer.toString(rst.getInt("max_price"));
					service_time = Integer.toString(rst.getInt("service_time"));
					serDescription = rst.getString("description");
					ServiceDetail sv = new ServiceDetail(serName, minPrice, maxPrice, service_time, serDescription);
					String svObject = gson.toJson(sv);
					allServicelist.add(svObject);
				}
				answer = answer + "{\"id\":\"" + id + "\"," + "\"name\":\"" + name + "\"," + "\"job_title\":\""
						+ job_title + "\"," + "\"salon\":\"" + salon + "\"," + "\"address\":\"" + address + "\","
						+ "\"picture\":\"" + picture + "\"," + "\"service\":" + allServicelist + "},";
			}
			answer = answer.substring(0, answer.length() - 1); // 拿掉最後一個字元
			answer = answer + "]";
			System.out.println(answer);
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			close();
		}
		return allServicelist;
	}

	public ArrayList<String> searchHairstyle() {
		System.out.println("輸出所有髮型");
		stylistWorks.clear();
		int id, stylist;
		String picture, description, hashtag;
		Gson gson = new Gson();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from stylist_works");

			while (rs.next()) {
				// System.out.println(rs.getInt("stylist")+"\t"+rs.getString("description")+"\t"+rs.getString("hashtag")+"\t"+rs.getString("picture"));
				id = rs.getInt("id");
				picture = rs.getString("picture");
				stylist = rs.getInt("stylist");
				description = rs.getString("description");
				hashtag = rs.getString("hashtag");

				Statement ST = null;
				ResultSet RS = null;
				ST = conn.createStatement();
				String stylist_job_title = "select job_title from stylist where id=" + stylist;
				// System.out.println(stylist_job_title);
				RS = ST.executeQuery(stylist_job_title);
				if (RS.next()) {
					// System.out.println(RS.getString("job_title"));
					stylist_job_title = RS.getString("job_title");
				}
				stylistWorks hairstyle = new stylistWorks(id, picture, stylist, stylist_job_title, description,
						hashtag);
				String jsonObject = gson.toJson(hairstyle);
				stylistWorks.add(jsonObject);
				// System.out.println(jsonObject+'\n');
			}
			System.out.println(stylistWorks);
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			close();
		}
		return stylistWorks;
	}

	public String oneSalon(int num) {
		System.out.println("輸出單一店家");
		infolist.clear();
		worklist.clear();
		int id = num;
		String name, address, phone, businessTime, picture;
		String ans = null;
		String stylist_work = "";

		Gson gson = new Gson();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from salon where id =" + num);
			if (rs.next()) {
				name = rs.getString("name");
				address = rs.getString("address");
				phone = rs.getString("phone");
				picture = rs.getString("picture");
				businessTime = rs.getString("businessTime");
				SalonDetail salon = new SalonDetail(name, address, phone, businessTime, picture, infolist);
				String jsonObject = gson.toJson(salon);

				ans = "{\"name\":\"" + rs.getString("name") + "\"," + "\"address\":\"" + rs.getString("address") + "\","
						+ "\"phone\":\"" + rs.getString("phone") + "\"," + "\"picture\":\"" + rs.getString("picture")
						+ "\"," + "\"businessTime\":\"" + rs.getString("businessTime") + "\"," + "\"stylist_info\":[";

				Statement ST = null;
				ResultSet RS = null;
				ST = conn.createStatement();
				String stylist = "select * from stylist where salon=" + num;
				RS = ST.executeQuery(stylist); // 找出某店家的所有設計師
				while (RS.next()) {
					infolist.clear();
					worklist.clear();

					id = RS.getInt("id");
					name = RS.getString("name");
					picture = RS.getString("picture");
					StylistInfo stylistInfo = new StylistInfo(id, name, picture);
					String Object = gson.toJson(stylistInfo);
					infolist.add(Object);

					stylist_work = stylist_work + "{\"id\":\"" + RS.getInt("id") + "\"," + "\"name\":\""
							+ RS.getString("name") + "\"," + "\"picture\":\"" + RS.getString("picture") + "\",";

					Statement stt = null;
					ResultSet rst = null;
					stt = conn.createStatement();
					rst = stt.executeQuery("select * from hairstyle where stylist=" + id); // 找出某設計師的所有作品
					while (rst.next()) {
						id = rst.getInt("id");
						picture = rst.getString("picture");
						Work work = new Work(id, picture);
						String object = gson.toJson(work);
						worklist.add(object);
					}
					stylist_work = stylist_work + "\"works\":" + worklist + "},";
				}

				stylist_work = stylist_work.substring(0, stylist_work.length()); // 拿掉最後一個字元
				ans = ans + stylist_work;
				ans += "]}";
				System.out.println(ans);
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			close();
		}
		return gson.toJson(ans);
	}

	public String oneStylist(int num) {
		System.out.println("輸出單一設計師");
		worklist.clear();
		int id = num, salon, price;
		String name, job_title, address, picture, service, description, works;
		String ans = null;

		Gson gson = new Gson();
		try {
			stat = conn.createStatement();
			rs = stat.executeQuery("select * from stylist where id =" + num);
			if (rs.next()) {
				name = rs.getString("name");
				job_title = rs.getString("job_title");
				salon = rs.getInt("salon");
				picture = rs.getString("picture");

				Statement ST = null;
				ResultSet RS = null;
				ST = conn.createStatement();
				address = "select address from salon where id=" + salon;
				RS = ST.executeQuery(address);
				if (RS.next()) {
					address = RS.getString("address");
				}

				StylistDetail stylist = new StylistDetail(name, job_title, salon, address, picture);
				String jsonObject = gson.toJson(stylist);
				ans = "{\"name\":\"" + rs.getString("name") + "\"," + "\"job_title\":\"" + rs.getString("job_title")
						+ "\"," + "\"salon\":\"" + rs.getInt("salon") + "\"," + "\"address\":\"" + address + "\","
						+ "\"picture\":\"" + rs.getString("picture") + "\",";
			}
			String serName, minPrice, maxPrice, service_time, serDescription;
			Statement ST = null;
			ResultSet RS = null;
			ST = conn.createStatement();
			RS = ST.executeQuery("select * from service where stylist=" + num);
			while (RS.next()) {
				serName = RS.getString("name");
				minPrice = Integer.toString(RS.getInt("min_price"));
				maxPrice = Integer.toString(RS.getInt("max_price"));
				service_time = Integer.toString(RS.getInt("service_time"));
				serDescription = RS.getString("description");
				ServiceDetail sv = new ServiceDetail(serName, minPrice, maxPrice, service_time, serDescription);
				String svObject = gson.toJson(sv);
				servicelist.add(svObject);
			}
			ans = ans + "\"service\":" + servicelist + ",";

			Statement stt = null;
			ResultSet rst = null;
			stt = conn.createStatement();
			rst = stt.executeQuery("select * from hairstyle where stylist=" + num); // 找出某設計師的所有作品
			while (rst.next()) {
				id = rst.getInt("id");
				picture = rst.getString("picture");
				Work work = new Work(id, picture);
				String object = gson.toJson(work);
				worklist.add(object);
			}
			ans = ans + "\"works\":" + worklist + "}";

			System.out.println(ans);

		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			close();
		}
		return gson.toJson(ans);
	}

	public String oneHairstyle(int num) {
		System.out.println("輸出單一髮型");
		int id, stylist;
		String description, hashtag, picture;
		Gson gson = new Gson();
		String jsonObject = null;
		try {
			stat = conn.createStatement();
			String string = "select * from hairstyle where id =" + num;
			rs = stat.executeQuery(string);
			if (rs.next()) {
				id = rs.getInt("id");
				stylist = rs.getInt("stylist");
				description = rs.getString("description");
				hashtag = rs.getString("hashtag");
				picture = rs.getString("picture");

				Statement ST = null;
				ResultSet RS = null;
				ST = conn.createStatement();
				String stylist_job_title = "select job_title from stylist where id=" + stylist;
				RS = ST.executeQuery(stylist_job_title);
				if (RS.next()) {
					stylist_job_title = RS.getString("job_title");
				}

				stylistWorks hairstyle = new stylistWorks(id, picture, stylist, stylist_job_title, description,
						hashtag);
				jsonObject = gson.toJson(hairstyle);
				stylistWorks.add(jsonObject);
				System.out.println(jsonObject);
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			close();
		}
		return gson.toJson(jsonObject);
	}

	public void close() {
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
			System.out.println("close SQLException:" + e.toString());
		}
	}

	public static void main(String args[]) {
		Search selectDB = new Search();
		selectDB.searchSalon();
		selectDB.oneSalon(12);
		selectDB.searchStylist();
		selectDB.oneStylist(1);
		selectDB.searchHairstyle();
		selectDB.oneHairstyle(1);
	}
}