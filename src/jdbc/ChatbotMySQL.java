package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import chatbot.ChatbotSalon;
import chatbot.ChatbotStylist;


public class ChatbotMySQL {
	
	private ConnectDB database = new ConnectDB();
	private Connection con = database.getConnection();
	private Statement stat = database.getStatement();
	private ResultSet rs = null;
	
	public String getSalon(String salonName) {
		ArrayList<ChatbotSalon> allSalon = new ArrayList<ChatbotSalon>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from salon where name like '%" + salonName + "%'");
			while(rs.next()) {
				ChatbotSalon salon = new ChatbotSalon();
				salon.setName(rs.getString("name"));
				salon.setAddress(rs.getString("address"));
				salon.setPhone(rs.getString("phone"));
				salon.setPicture(rs.getString("picture"));
				salon.setBusinessTime(rs.getString("businessTime"));
				allSalon.add(salon);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return ChatbotSalon.convertToJson(allSalon);
	}
	public String getStylist(String salonName) {
		ArrayList<ChatbotStylist> allStylist = new ArrayList<ChatbotStylist>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from stylist where name like '%" + salonName + "%'");
			while(rs.next()) {
				ChatbotStylist stylist = new ChatbotStylist();
				int salonID = rs.getInt("salon");
				stylist.setName(rs.getString("name"));
				stylist.setJobTitle(rs.getString("job_title"));
				stylist.setPicture(rs.getString("picture"));

				Statement ST = null;
				ResultSet RS = null;
				ST = con.createStatement();
				RS = ST.executeQuery("select * from salon where id=" + salonID);
				if (RS.next()) {
					stylist.setSalon(RS.getString("name"));
					stylist.setAddress(RS.getString("address"));
				}
				allStylist.add(stylist);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return ChatbotStylist.convertToJson(allStylist);
	}

}
