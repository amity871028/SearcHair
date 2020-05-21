package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import chatbot.ChatbotQA;
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
			// find salon names which contain keyword
			stat = con.createStatement();
			rs = stat.executeQuery("select * from salon where name like '%" + salonName + "%'");
			while (rs.next()) {
				ChatbotSalon salon = new ChatbotSalon();
				salon.setID(rs.getInt("id"));
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

	// find stylist names which contain keyword
	public String getStylist(String StylistName) {
		ArrayList<ChatbotStylist> allStylist = new ArrayList<ChatbotStylist>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from stylist where name like '%" + StylistName + "%'");
			while (rs.next()) {
				ChatbotStylist stylist = new ChatbotStylist();
				int salonID = rs.getInt("salon");
				stylist.setID(rs.getInt("id"));
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

	// find answer names which contain keyword
	public String getAnswer(String keyword) {
		String[] keywordArray = keyword.split(" "); // keyword may have more than one
		ArrayList<ChatbotQA> allQA = new ArrayList<ChatbotQA>();
		try {
			for (int i = 0; i < keywordArray.length; i++) {
				stat = con.createStatement();
				rs = stat.executeQuery("select * from chatbot_qa where keyword like '%" + keywordArray[i] + "%'");
				while (rs.next()) {
					ChatbotQA QA = new ChatbotQA();
					int priority = compareKeyword(keywordArray, rs.getString("keyword"));
					QA.setID(rs.getInt("id"));
					QA.setPriority(priority);
					QA.setQuestion(rs.getString("question"));
					QA.setAnswer(rs.getString("answer"));
					QA.setAnswerWeb(rs.getString("answer_web"));
					allQA.add(QA);
				}
			}

			allQAHandler(allQA);

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return ChatbotQA.convertToJson(allQA);
	}

	public ArrayList<ChatbotQA> allQAHandler(ArrayList<ChatbotQA> allQA) {
		boolean shuffleFlag = true;
		for (int i = 0; i < allQA.size() - 1; i++) {
			if (allQA.get(i).getPriority() != allQA.get(i + 1).getPriority()) {
				shuffleFlag = false; // to know if priority of all question are same
			}
			for (int j = i + 1; j < allQA.size(); j++) {
				if (allQA.get(i).getID() == allQA.get(j).getID()) {
					allQA.remove(j); // remove the repeat question
				}
			}
		}
		if (shuffleFlag == true) { // if priority of all question are same the shuffle
			Collections.shuffle(allQA);
		}
		return allQA;
	}

	// count the priority of this question
	public int compareKeyword(String[] keywordArray, String keyword) {
		int count = 0;
		for (int i = 0; i < keywordArray.length; i++) {
			if (keyword.contains(keywordArray[i])) {
				count++;
			}
		}
		return count;
	}
}
