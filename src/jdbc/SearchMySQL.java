package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import com.google.gson.Gson;

import search.*;

public class SearchMySQL {
	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private Connection con = database.getConnection();
	private ResultSet rs = null;

	public String searchSalon(int page) {
		System.out.println("輸出所有店家");
		int salonID, stylistID;
		int num = 1; // 計算有幾筆資料
		String ans = null;
		ArrayList<AllSalon> AllSalon_List = new ArrayList<AllSalon>();

		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from salon");
			while (rs.next()) {
				AllSalon allSalon = new AllSalon();
				ArrayList<String> all_Service_List = new ArrayList<String>();
				salonID = rs.getInt("id");
				allSalon.setID(rs.getInt("id"));
				allSalon.setName(rs.getString("name"));
				allSalon.setAddress(rs.getString("address"));
				allSalon.setPhone(rs.getString("phone"));
				allSalon.setPicture(rs.getString("picture"));

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select id from stylist where salon=" + salonID);
				while (RS.next()) { // 搜尋某店家裡的所有設計師
					stylistID = RS.getInt("id");

					Statement stt = null;
					ResultSet rst = null;
					stt = con.createStatement();
					rst = stt.executeQuery("select name from service where stylist=" + stylistID);
					while (rst.next()) // 搜尋某設計師有提供的服務
						all_Service_List.add(rst.getString("name"));

					LinkedHashSet<String> set = new LinkedHashSet<String>(all_Service_List); // 刪除重複的值(service)
					ArrayList<String> service_List = new ArrayList<String>(set);
					allSalon.setService(service_List);
				}
				if (num <= page * 100 && num > (page - 1) * 100)
					AllSalon_List.add(allSalon);
				num++;
			}
			AllSalon output = new AllSalon();
			ans = output.convertToJson(AllSalon_List);
			System.out.println(ans); // ======這裡輸出JSON======
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			// database.close();
		}
		return ans;
	}

	public String searchStylist(int page) {
		System.out.println("輸出所有設計師");
		int stylistID, salonID;
		int num = 1; // 計算有幾筆資料
		String ans = null;
		ArrayList<AllStylist> AllStylist_List = new ArrayList<AllStylist>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from stylist");
			while (rs.next()) {
				AllStylist allStylist = new AllStylist();
				salonID = rs.getInt("salon");
				stylistID = rs.getInt("id");
				allStylist.setID(rs.getInt("id"));
				allStylist.setSalonId(rs.getInt("salon"));
				allStylist.setName(rs.getString("name"));
				allStylist.setJobTitle(rs.getString("job_title"));
				allStylist.setPicture(rs.getString("picture"));

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select * from salon where id=" + salonID);
				if (RS.next()) {
					allStylist.setSalon(RS.getString("name"));
					allStylist.setAddress(RS.getString("address"));
				}

				Statement stt = null;
				ResultSet rst = null;
				stt = con.createStatement();
				rst = stt.executeQuery("select * from service where stylist=" + stylistID);
				ArrayList<Service> Service_List = new ArrayList<Service>();
				while (rst.next()) { // 搜尋設計師有提供的服務
					Service service = new Service();
					service.setName(rst.getString("name"));
					service.setMinPrice(rst.getInt("min_price"));
					service.setMaxPrice(rst.getInt("max_price"));
					service.setServiceTime(rst.getInt("service_time"));
					service.setDescription(rst.getString("description"));
					Service_List.add(service);
				}
				allStylist.setService(Service_List);
				if (num <= page * 100 && num > (page - 1) * 100)
					AllStylist_List.add(allStylist);
				num++;
			}
			AllStylist output = new AllStylist();
			ans = output.convertToJson(AllStylist_List);
			System.out.println(ans); // ======這裡輸出JSON======
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			// database.close();
		}
		return ans;
	}

	public String searchStylistWorks(int page) {
		System.out.println("輸出所有髮型");
		int stylistID;
		int num = 1; // 計算有幾筆資料
		String ans = null;
		ArrayList<AllStylistWorks> allStylistWorksList = new ArrayList<AllStylistWorks>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from stylist_works");
			while (rs.next()) {
				AllStylistWorks allStylistWorks = new AllStylistWorks();
				stylistID = rs.getInt("stylist");
				allStylistWorks.setID(rs.getInt("id"));
				allStylistWorks.setPicture(rs.getString("picture"));
				allStylistWorks.setDescription(rs.getString("description"));
				allStylistWorks.setHashtag(rs.getString("hashtag"));

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select * from stylist where id=" + stylistID);
				if (RS.next()) {
					allStylistWorks.setStylist(RS.getString("name"));
					allStylistWorks.setStylistJobTitle(RS.getString("job_title"));
				}
				if (num <= page * 100 && num > (page - 1) * 100)
					allStylistWorksList.add(allStylistWorks);
				num++;
			}
			AllStylistWorks output = new AllStylistWorks();
			ans = output.convertToJson(allStylistWorksList);
			System.out.println(ans); // ======�ㄐ頛詨JSON======

		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			// database.close();
		}
		return ans;
	}

	public String searchOneSalon(int num) {
		System.out.println("輸出單一店家");
		int id = num;
		int count = 0;
		String ans = null;
		ArrayList<StylistInfo> StylistInfo_List = new ArrayList<StylistInfo>();

		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from salon where id =" + num);
			if (rs.next()) {
				Salon salon = new Salon();
				salon.setName(rs.getString("name"));
				salon.setAddress(rs.getString("address"));
				salon.setPhone(rs.getString("phone"));
				salon.setPicture(rs.getString("picture"));
				salon.setBusinessTime(rs.getString("businessTime"));

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				String stylist = "select * from stylist where salon=" + num;
				RS = ST.executeQuery(stylist); // 找出某店家的所有設計師
				while (RS.next()) {
					ArrayList<Work> Work_List = new ArrayList<Work>();
					StylistInfo stylistInfo = new StylistInfo();
					Work work = new Work();
					id = RS.getInt("id");
					stylistInfo.setID(RS.getInt("id"));
					stylistInfo.setName(RS.getString("name"));
					stylistInfo.setPicture(RS.getString("picture"));
					stylistInfo.setStylistJobTitle(RS.getString("job_title"));

					Statement stt = null;
					ResultSet rst = null;
					stt = con.createStatement();
					rst = stt.executeQuery("select * from stylist_works where stylist=" + id); // 找出某設計師的所有作品
					while (rst.next()) {
						work.setID(rst.getInt("id"));
						work.setPicture(rst.getString("picture"));
						work.setDescription(rst.getString("description"));
						work.setHashtag(rst.getString("hashtag"));
						if (count < 3)
							Work_List.add(work);
						else
							break;
						count++;
					}
					stylistInfo.setWorks(Work_List);
					StylistInfo_List.add(stylistInfo);
				}
				salon.setStylistInfo(StylistInfo_List);
				ans = salon.convertToJson(salon);
				System.out.println(ans); // ======這裡輸出JSON======
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public String searchOneStylist(int num) { // num為要找的設計師id號碼
		System.out.println("輸出單一設計師");
		int salonID;
		String ans = null;
		Stylist stylist = new Stylist();
		ArrayList<Service> Service_List = new ArrayList<Service>();
		ArrayList<Work> Work_List = new ArrayList<Work>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from stylist where id =" + num);
			if (rs.next()) {
				salonID = rs.getInt("salon");
				stylist.setName(rs.getString("name"));
				stylist.setJobTitle(rs.getString("job_title"));
				stylist.setSalonId(rs.getInt("salon"));
				stylist.setPicture(rs.getString("picture"));

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select * from salon where id=" + salonID);
				if (RS.next()) {
					stylist.setSalon(RS.getString("name"));
					stylist.setAddress(RS.getString("address"));
				}

				Statement STT = null;
				ResultSet RSS = null;
				STT = con.createStatement();
				RSS = STT.executeQuery("select * from service where stylist=" + num);
				while (RSS.next()) {
					Service service = new Service();
					service.setName(RSS.getString("name"));
					service.setMinPrice(RSS.getInt("min_price"));
					service.setMaxPrice(RSS.getInt("max_price"));
					service.setServiceTime(RSS.getInt("service_time"));
					service.setDescription(RSS.getString("description"));
					Service_List.add(service);
				}

				Statement stt = null;
				ResultSet rst = null;
				stt = con.createStatement();
				rst = stt.executeQuery("select * from stylist_works where stylist=" + num); // 找出某設計師的所有作品
				while (rst.next()) {
					Work work = new Work();
					work.setID(rst.getInt("id"));
					work.setPicture(rst.getString("picture"));
					work.setDescription(rst.getString("description"));
					work.setHashtag(rst.getString("hashtag"));
					Work_List.add(work);
				}
				stylist.setService(Service_List);
				stylist.setWork(Work_List);
			}
			Stylist output = new Stylist();
			ans = output.convertToJson(stylist);
			System.out.println(ans); // ======這裡輸出JSON======
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public String searchOneStylistWork(int num) {
		System.out.println("輸出單一髮型");
		int id, stylistID;
		String picture, description, hashtag;
		String ans = null;
		String stylist = null, job_title = null;
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from stylist_works where id =" + num);
			if (rs.next()) {
				id = rs.getInt("id");
				picture = rs.getString("picture");
				stylistID = rs.getInt("stylist");
				description = rs.getString("description");
				hashtag = rs.getString("hashtag");

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select * from stylist where id=" + stylistID);
				if (RS.next()) {
					stylist = RS.getString("name");
					job_title = RS.getString("job_title");
				}
				StylistWorks stylistWorks = new StylistWorks(id, picture, stylist, job_title, description, hashtag);
				Gson gson = new Gson();
				ans = gson.toJson(stylistWorks);
				System.out.println(ans);// ======這裡輸出JSON======
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public static void main(String args[]) {
		SearchMySQL test = new SearchMySQL();
		test.searchSalon(1);
		test.searchOneSalon(5);
		test.searchStylist(1);
		test.searchOneStylist(88);
		test.searchStylistWorks(1);
		test.searchOneStylistWork(1);
	}
}
