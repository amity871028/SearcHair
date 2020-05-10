package search;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AllStylist{ 
	int id;
	String name,job_title;
	int salonId;
	String salon,address, picture;
	ArrayList<Service> service = new ArrayList<Service>();
	
    public AllStylist() {}
    
    public void setID(int id) {
    	this.id = id;
	}
    
    public void setName(String name) {
    	this.name = name;
	}
    
    public void setJobTitle(String job_title) {
    	this.job_title = job_title;
	}
    
    public void setSalon(String salon) {
    	this.salon = salon;
	}   
    
    public void setSalonId(int salonId) {
    	this.salonId = salonId;
	}
    
    public void setAddress(String address) {
    	this.address = address;
	}

    public void setPicture(String picture) {
    	this.picture = picture;
	}    
    
    public void setService(ArrayList<Service> service) {
    	this.service = service;
	}

    public static String convertToJson(ArrayList<AllStylist> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}