package search;
import java.util.ArrayList;
import java.io.*;
import com.google.gson.Gson;

public class SalonDetail{
	//int id;
	String id,name, address, phone,picture,businessTime,stylist_info;
	
    public SalonDetail(String name,String address,String phone,String picture,String businessTime,ArrayList<String> infolist) {
    	//this.id = id;
    	//this.id = String.valueOf(id);
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.picture = picture;
		this.businessTime = businessTime;
		//this.stylist_info = infolist;
		
    }
}
