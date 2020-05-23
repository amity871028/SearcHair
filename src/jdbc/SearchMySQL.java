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

	public String searchSalon(int page, String keyword, String[] service) {
		int salonID, stylistID;
		int num = 1; // count salon
		String ans = null;
		ArrayList<AllSalon> AllSalon_List = new ArrayList<AllSalon>();

		String serviceCondition = "(";
		String other = "";
		int flag = 0; // count services

		if (service != null) {
			for (int i = 0; i < service.length; i++) {
				if (service[i].contains("髮")) {
					service[i] = service[i].replaceAll("髮", ""); // ignore "髮"
					serviceCondition += "name like '%" + service[i] + "%'" + " or ";
					flag++;
				}
				if (service[i].contains("其他"))
					other = "其他";
			}
			if (flag != 0) // select services
				serviceCondition = serviceCondition.substring(0, serviceCondition.length() - 4) + ")";
			if (service.length == 1 && other.equals("其他")) // only select "Other"
				serviceCondition = "name not like '%洗%' and name not like '%剪%' and name not like '%染%' and name not like '%燙%' and name not like '%護%'";
			else if (service.length > 1 && other.equals("其他")) // select services and "Other"
				serviceCondition += " and name not like '%洗%' and name not like '%剪%' and name not like '%染%' and name not like '%燙%' and name not like '%護%'";
		}
		try {
			stat = con.createStatement();
			if (keyword == null)
				rs = stat.executeQuery("select * from salon");
			else
				rs = stat.executeQuery(
						"select * from salon where name like '%" + keyword + "%' or address like '%" + keyword + "%'");

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
				while (RS.next()) { // search for all stylists in a salon
					stylistID = RS.getInt("id");

					Statement stt = null;
					ResultSet rst = null;
					stt = con.createStatement();
					if (service == null) // service array is null
						rst = stt.executeQuery("select name from service where stylist=" + stylistID);
					else
						rst = stt.executeQuery(
								"select name from service where stylist=" + stylistID + " and " + serviceCondition);
					while (rst.next()) // search services provided by stylist
						all_Service_List.add(rst.getString("name"));
				}

				int count = 0; // count salon service match the user's select service
				if (service != null) {
					for (int i = 0; i < service.length; i++) {
						for (String str : all_Service_List) {
							if (str.contains(service[i])) {
								count++;
								break;
							}
						}
					}
				}
				if (other.equals("其他"))
					count++;
				if (all_Service_List.size() != 0 || (all_Service_List.size() == 0 && service == null)) {
					if (service != null && count >= service.length) {
						LinkedHashSet<String> set = new LinkedHashSet<String>(all_Service_List); // delete duplicate services
						ArrayList<String> service_List = new ArrayList<String>(set);
						allSalon.setService(service_List);
						if (num <= page * 99 && num > (page - 1) * 99) {
							AllSalon_List.add(allSalon);
						}
						num++;
					} else if (service == null) {
						LinkedHashSet<String> set = new LinkedHashSet<String>(all_Service_List); // delete duplicate services
						ArrayList<String> service_List = new ArrayList<String>(set);
						allSalon.setService(service_List);
						if (num <= page * 99 && num > (page - 1) * 99) {
							AllSalon_List.add(allSalon);
						}
						num++;
					}

				}
			}
			LinkedHashSet<AllSalon> set = new LinkedHashSet<AllSalon>(AllSalon_List); // delete duplicate services
			ArrayList<AllSalon> output_List = new ArrayList<AllSalon>(set);
			AllSalon output = new AllSalon();
			ans = output.convertToJson(output_List);
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public String searchStylist(int page, String keyword, String[] serviceArray, int[] price) {
		int stylistID, salonID;
		int num = 1; // // count stylist
		String ans = null;
		ArrayList<AllStylist> AllStylist_List = new ArrayList<AllStylist>();

		String serviceCondition = "";
		String other = "";
		int flag = 0; // count services
		if (serviceArray != null) {
			serviceCondition = "(";
			for (int i = 0; i < serviceArray.length; i++) {
				if (serviceArray[i].contains("髮")) {
					serviceArray[i] = serviceArray[i].replaceAll("髮", ""); // ignore "髮"
					serviceCondition += "name like '%" + serviceArray[i] + "%'" + " or ";
					flag++;
				}
				if (serviceArray[i].contains("其他"))
					other = "其他";
			}
			if (flag != 0) // select services
				serviceCondition = serviceCondition.substring(0, serviceCondition.length() - 4) + ")";
			if (serviceArray.length == 1 && other.equals("其他")) // only select "Other"
				serviceCondition = "name not like '%洗%' and name not like '%剪%' and name not like '%染%' and name not like '%燙%' and name not like '%護%'";
			else if (serviceArray.length > 1 && other.equals("其他")) // select services and "Other"
				serviceCondition += " and name not like '%洗%' and name not like '%剪%' and name not like '%染%' and name not like '%燙%' and name not like '%護%'";
		}
		try {
			stat = con.createStatement();
			if (keyword == null)
				rs = stat.executeQuery("select * from stylist");
			else
				rs = stat.executeQuery("select * from stylist where name like '%" + keyword + "%'");
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
				if (price == null && serviceArray == null)
					rst = stt.executeQuery("select * from service where stylist=" + stylistID);
				else if (price != null && serviceArray == null)
					rst = stt.executeQuery("select * from service where stylist=" + stylistID + " and max_price<"
							+ price[1] + " and min_price<" + price[1] + " and min_price>" + price[0] + " and max_price>"
							+ price[0]);
				else if (price == null && serviceArray != null)
					rst = stt.executeQuery(
							"select * from service where stylist=" + stylistID + " and " + serviceCondition);
				else
					rst = stt.executeQuery("select * from service where stylist=" + stylistID + " and max_price<"
							+ price[1] + " and min_price<" + price[1] + " and min_price>" + price[0] + " and max_price>"
							+ price[0] + " and " + serviceCondition);
				ArrayList<Service> Service_List = new ArrayList<Service>();
				ArrayList<String> Service_Name_List = new ArrayList<String>();

				while (rst.next()) { // search services provided by stylist
					Service service = new Service();
					service.setName(rst.getString("name"));
					service.setMinPrice(rst.getInt("min_price"));
					service.setMaxPrice(rst.getInt("max_price"));
					service.setServiceTime(rst.getInt("service_time"));
					service.setDescription(rst.getString("description"));
					Service_Name_List.add(rst.getString("name"));
					Service_List.add(service);
				}

				int count = 0; // count service by stylist providing match the user's select
				if (serviceArray != null) {
					for (int i = 0; i < serviceArray.length; i++) {
						for (String str : Service_Name_List) {
							if (str.contains(serviceArray[i])) {
								count++;
								break;
							}
						}
					}
				}
				if (other.equals("其他"))
					count++;
				if (Service_List.size() != 0 || (Service_List.size() == 0 && serviceArray == null)) {
					if (serviceArray != null && count >= serviceArray.length) {
						allStylist.setService(Service_List);
						if (num <= page * 99 && num > (page - 1) * 99) {
							AllStylist_List.add(allStylist);
						}
						num++;
					} else if (serviceArray == null) {
						allStylist.setService(Service_List);
						if (num <= page * 99 && num > (page - 1) * 99) {
							AllStylist_List.add(allStylist);
						}
						num++;
					}
				}
			}
			AllStylist output = new AllStylist();
			ans = output.convertToJson(AllStylist_List);
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public String searchStylistWorks(int page, String keyword) {
		int stylistID;
		int num = 1; // count stylistWorks
		String ans = null;
		ArrayList<AllStylistWorks> allStylistWorksList = new ArrayList<AllStylistWorks>();
		if (keyword != null && keyword.contains("髮"))
			keyword = keyword.replaceAll("髮", ""); // ignore "髮"
		try {
			stat = con.createStatement();
			if (keyword == null)
				rs = stat.executeQuery("select * from stylist_works");
			else
				rs = stat.executeQuery("select * from stylist_works where hashtag like '%" + keyword + "%'");
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
				if (num <= page * 99 && num > (page - 1) * 99) {
					allStylistWorksList.add(allStylistWorks);
				}
				num++;
			}
			AllStylistWorks output = new AllStylistWorks();
			ans = output.convertToJson(allStylistWorksList);
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public String searchOneSalon(int num) { // num = salon's ID
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
				RS = ST.executeQuery(stylist); // search for all stylists in a salon
				while (RS.next()) {
					count = 0;
					ArrayList<Work> Work_List = new ArrayList<Work>();
					StylistInfo stylistInfo = new StylistInfo();
					id = RS.getInt("id");
					stylistInfo.setID(RS.getInt("id"));
					stylistInfo.setName(RS.getString("name"));
					stylistInfo.setPicture(RS.getString("picture"));
					stylistInfo.setStylistJobTitle(RS.getString("job_title"));

					Statement stt = null;
					ResultSet rst = null;
					stt = con.createStatement();
					rst = stt.executeQuery("select * from stylist_works where stylist=" + id); // search for all stylist works of the stylist
					while (rst.next()) {
						Work work = new Work();
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
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public String searchOneStylist(int num) { // num = stylist's ID
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
				rst = stt.executeQuery("select * from stylist_works where stylist=" + num); // search for all stylist works of the stylist
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
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

	public String searchOneStylistWork(int num) {
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
			}
		} catch (SQLException e) {
			System.out.println("select table SQLException:" + e.toString());
		} finally {
			database.close();
		}
		return ans;
	}

}
