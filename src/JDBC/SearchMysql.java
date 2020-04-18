package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.google.gson.Gson;

import search.*;

public class SearchMysql {
	private Connection con=null;
	private Statement stat=null;
	private ResultSet rs=null;
	private PreparedStatement pst=null;
	
	public SearchMysql() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); //register driver
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/searchair?characterEncoding=utf-8", "root","29118310");
			System.out.println("===��蝺��澈���� ! ===");
		}catch(ClassNotFoundException e) {
			System.out.println("ClassNotFoundException:"+e.toString());
		} catch (SQLException e) {
			System.out.println("SQLException:"+e.toString());
		}
	}

	public String searchSalon() {
		System.out.println("頛詨�����振");
		int salonID,stylistID;
		String ans = null; 
		ArrayList<AllSalon> AllSalon_List = new ArrayList<AllSalon>();
		
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from salon");
			while(rs.next()) {
				AllSalon allSalon = new AllSalon();
				ArrayList<String> service_List = new ArrayList<String>();
				salonID = rs.getInt("id");
				allSalon.setID(rs.getInt("id"));
				allSalon.setName(rs.getString("name"));
				allSalon.setAddress(rs.getString("address"));
				allSalon.setPhone(rs.getString("phone"));
				allSalon.setPicture(rs.getString("picture"));
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select id from stylist where salon="+salonID);
				while(RS.next()) { //�����振鋆∠����身閮葦
					stylistID=RS.getInt("id");

			        Statement stt=null;
					ResultSet rst = null;
					stt=con.createStatement();
					rst=stt.executeQuery("select name from service where stylist="+stylistID);
					while(rst.next()) //����身閮葦��������
						service_List.add(rst.getString("name"));
				}
				AllSalon_List.add(allSalon);
			}
			AllSalon output = new AllSalon();
			ans = output.convertToJson(AllSalon_List); 
			System.out.println(ans); //======�ㄐ頛詨JSON======
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			close();
		}
		return ans;
	}
	
	public String searchStylist() { 
		System.out.println("頛詨����身閮葦");
		int stylistID,salonID;
		String ans = null;
		ArrayList<AllStylist> AllStylist_List = new ArrayList<AllStylist>(); 
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from stylist");
			while(rs.next()) {
				AllStylist allStylist = new AllStylist();
				salonID = rs.getInt("salon");
				stylistID = rs.getInt("id");
				allStylist.setID(rs.getInt("id"));
				allStylist.setName(rs.getString("name"));
				allStylist.setJobTitle(rs.getString("job_title"));
				allStylist.setPicture(rs.getString("picture"));
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select * from salon where id="+salonID);
				if(RS.next()) {
					allStylist.setSalon(RS.getString("name"));
					allStylist.setAddress(RS.getString("address"));
				}

				Statement stt=null;
				ResultSet rst = null;
				stt=con.createStatement();
				rst=stt.executeQuery("select * from service where stylist="+stylistID);
				ArrayList<Service> Service_List = new ArrayList<Service>(); 
				while(rst.next()) { //���身閮葦��������
					Service service = new Service();
			        service.setName(rst.getString("name"));
			        service.setMinPrice(rst.getInt("min_price"));
			        service.setMaxPrice(rst.getInt("max_price"));
			        service.setServiceTime(rst.getInt("service_time"));
			        service.setDescription(rst.getString("description"));
			        Service_List.add(service);
				}
				allStylist.setService(Service_List);
				AllStylist_List.add(allStylist);
			}
			AllStylist output = new AllStylist();
			ans = output.convertToJson(AllStylist_List); 
			System.out.println(ans); //======�ㄐ頛詨JSON======
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			close();
		}
		return ans;
	}

	public String searchStylistWorks() { 
		System.out.println("頛詨����垣���");
		int stylistID;
		String ans = null;
		ArrayList<AllStylistWorks> allStylistWorksList = new ArrayList<AllStylistWorks>(); 
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from stylist_works");
			while(rs.next()) {
				AllStylistWorks allStylistWorks = new AllStylistWorks();
				stylistID = rs.getInt("stylist");
				allStylistWorks.setID(rs.getInt("id"));
				allStylistWorks.setPicture(rs.getString("picture"));
				allStylistWorks.setDescription(rs.getString("description"));
				allStylistWorks.setHashtag(rs.getString("hashtag"));
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select * from stylist where id="+stylistID);
				if(RS.next()) {
					allStylistWorks.setStylist(RS.getString("name"));
					allStylistWorks.setStylistJobTitle(RS.getString("job_title"));
				}
				allStylistWorksList.add(allStylistWorks);
			}
			AllStylistWorks output = new AllStylistWorks();
			ans = output.convertToJson(allStylistWorksList); 
			System.out.println(ans); //======�ㄐ頛詨JSON======
			
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			close();
		}
		return ans;
	}

	public String searchOneSalon(int num) {
		System.out.println("頛詨�銝�摨振");
		int id=num;
		String ans = null;
		ArrayList<StylistInfo> StylistInfo_List = new ArrayList<StylistInfo>(); 

		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from salon where id ="+ num);
			if(rs.next()) {
				Salon salon = new Salon();
				salon.setName(rs.getString("name"));
				salon.setAddress(rs.getString("address"));
				salon.setPhone(rs.getString("phone"));
				salon.setPicture(rs.getString("picture"));
				salon.setBusinessTime(rs.getString("businessTime"));
				
		        Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				String stylist = "select * from stylist where salon="+num;
				RS=ST.executeQuery(stylist); //�����振�����身閮葦
				while(RS.next()) {
					ArrayList<Work> Work_List = new ArrayList<Work>(); 
					StylistInfo stylistInfo = new StylistInfo();
					id=RS.getInt("id");	
					stylistInfo.setID(RS.getInt("id"));
					stylistInfo.setName(RS.getString("name"));
					stylistInfo.setPicture(RS.getString("picture"));
					
					Statement stt=null;
					ResultSet rst = null;
					stt=con.createStatement();
					rst=stt.executeQuery("select * from stylist_works where stylist="+id); //����身閮葦��������
					while(rst.next()) {
						Work work = new Work();
						work.setID(rst.getInt("id"));
						work.setPicture(rst.getString("picture"));
						Work_List.add(work);
					}
					stylistInfo.setWorks(Work_List);
					StylistInfo_List.add(stylistInfo);
				}
				salon.setStylistInfo(StylistInfo_List);
				ans = salon.convertToJson(salon);
				System.out.println(ans); //======�ㄐ頛詨JSON======
			}
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			close();
		}
		return ans;
	}

	public String searchOneStylist(int num) { //num�閬��身閮葦id��Ⅳ
		System.out.println("頛詨�銝�閮剛�葦");
		int salonID;
		String ans = null;
		Stylist stylist = new Stylist();
		ArrayList<Service> Service_List = new ArrayList<Service>(); 
		ArrayList<Work> Work_List = new ArrayList<Work>(); 
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from stylist where id ="+ num);
			if(rs.next()) {
				salonID = rs.getInt("salon");
				stylist.setName(rs.getString("name"));
				stylist.setJobTitle(rs.getString("job_title"));
				stylist.setPicture(rs.getString("picture"));
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select * from salon where id="+salonID);
				if (RS.next()) {
					stylist.setSalon(RS.getString("name"));
					stylist.setAddress(RS.getString("address"));					
				}
			
				Statement STT=null;
				ResultSet RSS = null;
				STT=con.createStatement();
				RSS=STT.executeQuery("select * from service where stylist="+num);
				while(RSS.next()) {
					Service service = new Service();
			        service.setName(RSS.getString("name"));
			        service.setMinPrice(RSS.getInt("min_price"));
			        service.setMaxPrice(RSS.getInt("max_price"));
			        service.setServiceTime(RSS.getInt("service_time"));
			        service.setDescription(RSS.getString("description"));
			        Service_List.add(service);
				}
				
				Statement stt=null;
				ResultSet rst = null;
				stt=con.createStatement();
				rst=stt.executeQuery("select * from stylist_works where stylist="+num); //����身閮葦��������
				while(rst.next()) {
					Work work = new Work();
					work.setID(rst.getInt("id"));
					work.setPicture(rst.getString("picture"));
					Work_List.add(work);
				}
				stylist.setService(Service_List);
				stylist.setWork(Work_List);
			}
			Stylist output = new Stylist();
			ans = output.convertToJson(stylist); 
			System.out.println(ans); //======�ㄐ頛詨JSON======
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			close();
		}
		return ans;
	}

	public String searchOneStylistWork(int num) { 
		System.out.println("頛詨�銝�擃桀��");
		int id,stylistID;
		String picture,description,hashtag;
		String ans = null;
		String stylist = null,job_title = null;
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from stylist_works where id ="+ num);
			if(rs.next()) {
				id = rs.getInt("id");
				picture = rs.getString("picture");	
				stylistID = rs.getInt("stylist");
				description = rs.getString("description");
				hashtag = rs.getString("hashtag");			
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select * from stylist where id="+stylistID);
				if(RS.next()) {
					stylist=RS.getString("name");
					job_title=RS.getString("job_title");
				}
				StylistWorks stylistWorks = new StylistWorks(id,picture,stylist,job_title,description,hashtag);
				Gson gson = new Gson();
				ans = gson.toJson(stylistWorks);
				System.out.println(ans); //======�ㄐ頛詨JSON======	
			}
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			close();
		}
		return ans;
	}

	public void close(){
		try{
			if(rs!=null) {
				rs.close();
				rs=null;
			}
			if(stat!=null) {
				stat.close();
				stat=null;
			}
			if(pst!=null){
				pst.close();
				pst=null;
			}
		}catch(SQLException e){
			System.out.println("close SQLException:"+e.toString());
		}
	}
	
	public static void main(String args[]) {
		SearchMysql test = new SearchMysql();
		test.searchSalon();
		test.searchOneSalon(5);
		test.searchStylist();
		test.searchOneStylist(88);
		test.searchStylistWorks();
		test.searchOneStylistWork(1);
	}
}
