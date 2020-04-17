package JDBC;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import com.google.gson.Gson;

public class mySql {
	private Connection con=null;
	private Statement stat=null;
	private ResultSet rs=null;
	private PreparedStatement pst=null;
	
	public mySql() {
		try {
			Class.forName("com.mysql.jdbc.Driver"); //register driver
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/searchair?characterEncoding=utf-8", "root","29118310");
			System.out.println("===連線資料庫成功 ! ===");
		}catch(ClassNotFoundException e) {
			System.out.println("ClassNotFoundException:"+e.toString());
		} catch (SQLException e) {
			System.out.println("SQLException:"+e.toString());
		}
	}

	public void searchSalon() {
		System.out.println("輸出所有店家");
		int salonID,stylistID;
		ArrayList<AllSalon> AllSalon_List = new ArrayList<AllSalon>();
		
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from salon");
			while(rs.next()) {
				AllSalon allSalon = new AllSalon();
				ArrayList<String> service_List = new ArrayList<String>();
				salonID = rs.getInt("id");
				allSalon.set_ID(rs.getInt("id"));
				allSalon.set_Name(rs.getString("name"));
				allSalon.set_Address(rs.getString("address"));
				allSalon.set_Phone(rs.getString("phone"));
				allSalon.set_Picture(rs.getString("picture"));
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select id from stylist where salon="+salonID);
				while(RS.next()) { //搜尋某店家裡的所有設計師
					stylistID=RS.getInt("id");

			        Statement stt=null;
					ResultSet rst = null;
					stt=con.createStatement();
					rst=stt.executeQuery("select name from service where stylist="+stylistID);
					while(rst.next()) //搜尋某設計師有提供的服務
						service_List.add(rst.getString("name"));
				}
				AllSalon_List.add(allSalon);
			}
			AllSalon output = new AllSalon();
			String ans = output.convertToJson(AllSalon_List); 
			System.out.println(ans); //======這裡輸出JSON======
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			Close();
		}		
	}
	
	public void searchStylist() { 
		System.out.println("輸出所有設計師");
		int stylistID,salonID;
		ArrayList<AllStylist> AllStylist_List = new ArrayList<AllStylist>(); 
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from stylist");
			while(rs.next()) {
				AllStylist allStylist = new AllStylist();
				salonID = rs.getInt("salon");
				stylistID = rs.getInt("id");
				allStylist.set_ID(rs.getInt("id"));
				allStylist.set_Name(rs.getString("name"));
				allStylist.set_Job_title(rs.getString("job_title"));
				allStylist.set_Picture(rs.getString("picture"));
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select * from salon where id="+salonID);
				if(RS.next()) {
					allStylist.set_Salon(RS.getString("name"));
					allStylist.set_Address(RS.getString("address"));
				}

				Statement stt=null;
				ResultSet rst = null;
				stt=con.createStatement();
				rst=stt.executeQuery("select * from service where stylist="+stylistID);
				ArrayList<Service> Service_List = new ArrayList<Service>(); 
				while(rst.next()) { //搜尋設計師有提供的服務
					Service service = new Service();
			        service.set_Name(rst.getString("name"));
			        service.set_Min_price(rst.getInt("min_price"));
			        service.set_Max_price(rst.getInt("max_price"));
			        service.set_Service_time(rst.getInt("service_time"));
			        service.set_Description(rst.getString("description"));
			        Service_List.add(service);
				}
				allStylist.set_Service(Service_List);
				AllStylist_List.add(allStylist);
			}
			AllStylist output = new AllStylist();
			String ans = output.convertToJson(AllStylist_List); 
			System.out.println(ans); //======這裡輸出JSON======
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			Close();
		}
	}

	public void searchHairstyle() { 
		System.out.println("輸出所有髮型");
		int stylistID;
		ArrayList<AllHairstyle> AllHairstyle_List = new ArrayList<AllHairstyle>(); 
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from stylist_works");
			while(rs.next()) {
				AllHairstyle allHairstyle = new AllHairstyle();
				stylistID = rs.getInt("stylist");
				allHairstyle.set_ID(rs.getInt("id"));
				allHairstyle.set_Picture(rs.getString("picture"));
				allHairstyle.set_Description(rs.getString("description"));
				allHairstyle.set_Hashtag(rs.getString("hashtag"));
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select * from stylist where id="+stylistID);
				if(RS.next()) {
					allHairstyle.set_Stylist(RS.getString("name"));
					allHairstyle.set_Stylist_job_title(RS.getString("job_title"));
				}
				AllHairstyle_List.add(allHairstyle);
			}
			AllHairstyle output = new AllHairstyle();
			String ans = output.convertToJson(AllHairstyle_List); 
			System.out.println(ans); //======這裡輸出JSON======
			
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			Close();
		}
	}

	public void oneSalon(int num) {
		System.out.println("輸出單一店家");
		int id=num;
		ArrayList<StylistInfo> StylistInfo_List = new ArrayList<StylistInfo>(); 

		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from salon where id ="+ num);
			if(rs.next()) {
				Salon salon = new Salon();
				salon.set_Name(rs.getString("name"));
				salon.set_Address(rs.getString("address"));
				salon.set_Phone(rs.getString("phone"));
				salon.set_Picture(rs.getString("picture"));
				salon.set_BusinessTime(rs.getString("businessTime"));
				
		        Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				String stylist = "select * from stylist where salon="+num;
				RS=ST.executeQuery(stylist); //找出某店家的所有設計師
				while(RS.next()) {
					ArrayList<Work> Work_List = new ArrayList<Work>(); 
					StylistInfo stylistInfo = new StylistInfo();
					id=RS.getInt("id");	
					stylistInfo.set_ID(RS.getInt("id"));
					stylistInfo.set_Name(RS.getString("name"));
					stylistInfo.set_Picture(RS.getString("picture"));
					
					Statement stt=null;
					ResultSet rst = null;
					stt=con.createStatement();
					rst=stt.executeQuery("select * from stylist_works where stylist="+id); //找出某設計師的所有作品
					while(rst.next()) {
						Work work = new Work();
						work.set_ID(rst.getInt("id"));
						work.set_Picture(rst.getString("picture"));
						Work_List.add(work);
					}
					stylistInfo.set_Works(Work_List);
					StylistInfo_List.add(stylistInfo);
				}
				salon.set_Stylist_info(StylistInfo_List);
				String ans = salon.convertToJson(salon);
				System.out.println(ans); //======這裡輸出JSON======
			}
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			Close();
		}
	}

	public void oneStylist(int num) { //num為要找的設計師id號碼
		System.out.println("輸出單一設計師");
		int salonID;
		Stylist stylist = new Stylist();
		ArrayList<Service> Service_List = new ArrayList<Service>(); 
		ArrayList<Work> Work_List = new ArrayList<Work>(); 
		try {
			stat=con.createStatement();
			rs=stat.executeQuery("select * from stylist where id ="+ num);
			if(rs.next()) {
				salonID = rs.getInt("salon");
				stylist.set_Name(rs.getString("name"));
				stylist.set_Job_title(rs.getString("job_title"));
				stylist.set_Picture(rs.getString("picture"));
				
				Statement ST=null;
				ResultSet RS = null;
				ST=con.createStatement();
				RS=ST.executeQuery("select * from salon where id="+salonID);
				if (RS.next()) {
					stylist.set_Salon(RS.getString("name"));
					stylist.set_Address(RS.getString("address"));					
				}
			
				Statement STT=null;
				ResultSet RSS = null;
				STT=con.createStatement();
				RSS=STT.executeQuery("select * from service where stylist="+num);
				while(RSS.next()) {
					Service service = new Service();
			        service.set_Name(RSS.getString("name"));
			        service.set_Min_price(RSS.getInt("min_price"));
			        service.set_Max_price(RSS.getInt("max_price"));
			        service.set_Service_time(RSS.getInt("service_time"));
			        service.set_Description(RSS.getString("description"));
			        Service_List.add(service);
				}
				
				Statement stt=null;
				ResultSet rst = null;
				stt=con.createStatement();
				rst=stt.executeQuery("select * from stylist_works where stylist="+num); //找出某設計師的所有作品
				while(rst.next()) {
					Work work = new Work();
					work.set_ID(rst.getInt("id"));
					work.set_Picture(rst.getString("picture"));
					Work_List.add(work);
				}
				stylist.set_Service(Service_List);
				stylist.set_Work(Work_List);
			}
			Stylist output = new Stylist();
			String ans = output.convertToJson(stylist); 
			System.out.println(ans); //======這裡輸出JSON======
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			Close();
		}
	}

	public void oneHairstyle(int num) { 
		System.out.println("輸出單一髮型");
		int id,stylistID;
		String picture,description,hashtag;
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
				Hairstyle hairstyle = new Hairstyle(id,picture,stylist,job_title,description,hashtag);
				Gson gson = new Gson();
				String ans = gson.toJson(hairstyle);
				System.out.println(ans); //======這裡輸出JSON======	
			}
		}catch(SQLException e) {
			System.out.println("select table SQLException:"+e.toString());
		}
		finally {
			Close();
		}
	}

	public void Close(){
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
		mySql test = new mySql();
		test.searchSalon();
		test.oneSalon(5);
		test.searchStylist();
		test.oneStylist(88);
		test.searchHairstyle();
		test.oneHairstyle(1);
	}
}