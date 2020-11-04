package jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

import com.google.gson.Gson;
import com.mysql.jdbc.PreparedStatement;

import hairMatch.Photo;
import search.AllStylistWorks;

public class HairMatchMySQL {
	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private Connection con = database.getConnection();
	private ResultSet rs = null;

	private String selectStylistWorks = "SELECT * FROM stylist_works";
	private String selectHairstyle = "SELECT * FROM hairstyle";
	
	public boolean savePhoto(String userName, String hairstyle, String color, String url) {
		int flag = 0; // 正確的輸出1 錯誤輸出0
		try {
			stat = con.createStatement();
			rs = stat.executeQuery("select * from share_photo where user_name =\"" + userName + "\" and hairstyle=\""
					+ hairstyle + "\" and color=\"" + color + "\" and url=\"" + url + "\"");
			if (rs.next()) { // 已經有這筆資料了
				System.out.println("資料庫已有這張照片");
			} else { // 新增資料
				String insert = "insert into share_photo(user_name,hairstyle,color,url) value(?,?,?,?)";
				PreparedStatement pst = (PreparedStatement) con.prepareStatement(insert);

				pst.setString(1, userName);
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

	public String getStylistWorks(String faceShape, int page) {
		int count = 0;
		String[] faceShapeStrings = { "oval","rectangular","oblong","square","round","diamond","triangle" };

		
		ArrayList<AllStylistWorks> allStylistWorks = new ArrayList<AllStylistWorks>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(selectStylistWorks + " WHERE face_shape LIKE '%" + faceShape + "%'");
			
			while (rs.next()) {
				if (count < page * 50 && count >= (page - 1) * 50) {
					AllStylistWorks tmp = new AllStylistWorks();
					tmp.setID(rs.getInt("id"));
					tmp.setDescription(rs.getString("description"));
					tmp.setHashtag(rs.getString("hashtag"));
					tmp.setPicture(rs.getString("picture"));
					
					int stylistId = rs.getInt("stylist");
					Statement ST = null;
					ResultSet RS = null;
					ST = con.createStatement();
					RS = ST.executeQuery("select * from stylist where id=" + stylistId);
					if (RS.next()) {
						tmp.setStylist(RS.getString("name"));
						tmp.setStylistJobTitle(RS.getString("job_title"));
					}
					allStylistWorks.add(tmp);
				} else if (count == page * 100)
					break;
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		
		return AllStylistWorks.convertToJson(allStylistWorks);
	}
	
	public String getSameHairstyle(String hashtag) {
		ArrayList<String> allSameHairstyle = new ArrayList<String>();
		String stylistWorkhashtag[] = hashtag.split("\\\\\"");
		
        HashMap<String, Integer> count = new HashMap();
        int maxMatchCount = 0;
        int sumMatch = 0;
		for(int i = 0; i < stylistWorkhashtag.length; i++) {
			if(stylistWorkhashtag[i].equals("") || stylistWorkhashtag[i].equals("\"[") || stylistWorkhashtag[i].equals(",") || stylistWorkhashtag[i].equals("]\"")) continue;
			try {
				stat = con.createStatement();
				rs = stat.executeQuery(selectHairstyle + " where hashtag LIKE '%" + stylistWorkhashtag[i] + "%'");
				
				while (rs.next()) {
					String name = rs.getString("name");
					if(count.containsKey(name)) {
						int nowCount = count.get(name) + 1;
						count.put(name, nowCount);
						
						if(maxMatchCount < nowCount) {
							maxMatchCount = nowCount;
							sumMatch=1;
						}
						else {
							sumMatch++;
						}
					}
					else count.put(name, 1);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				database.close();
			}
		}
	    if(maxMatchCount == 0 && sumMatch == 0) {
	    	maxMatchCount = 1;
	    	sumMatch = count.size();
	    }
	    ArrayList<Integer> whichToSelect = new ArrayList<Integer>();
	    if(sumMatch > 6) {
		    Random r = new Random();
		    while(whichToSelect.size() != 6) {
			    int random =  r.nextInt(sumMatch)+1;
			    while(whichToSelect.contains(random)) {
			    	random =  r.nextInt(sumMatch)+1;
			    }
			    whichToSelect.add(random);
		    }
		    Collections.sort(whichToSelect);
	    }
	    else {
	    	for(int i = 0; i < sumMatch; i++) {
	    		whichToSelect.add(i);
	    	}
	    }
	    int now = 0;
	    int i = 0;
	    Iterator iterator = count.keySet().iterator();
	    while (iterator.hasNext()){
	        String key = (String)iterator.next();
	        if(count.get(key) == maxMatchCount) {
		        if(whichToSelect.size() == 0) {
		        	allSameHairstyle.add("\"" + key + "\"");
		        }
		        else if(now == whichToSelect.get(i)-1) {
		        	allSameHairstyle.add("\"" + key + "\"");
		        	i++;
		        	if(i == whichToSelect.size()) break;
		        }
		        now++;
	        } 
	    }
		String ans = allSameHairstyle.toString();
		return ans;
	}

	public String getMatchedHairstyle(String keyword) {
		System.out.println("keyword: " + keyword);
		ArrayList<String> allMatchHairstyle = new ArrayList<String>();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(selectHairstyle + " where hashtag LIKE '%" + keyword + "%'");
			
			while (rs.next()) {
				String name = rs.getString("name");
				allMatchHairstyle.add(name);
				System.out.println(name);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		String ans;
		if(allMatchHairstyle.size() == 0) ans = "";
		else {
			Random r = new Random();
			int random =  r.nextInt(allMatchHairstyle.size());
			ans = allMatchHairstyle.get(random);
		}
		return "{ \"hairstyle\":\"" + ans + "\"}";
	}
}
