package search;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AllStylist{ 

	int id;
	String name,job_title,salon,address, picture;
	ArrayList<Service> service = new ArrayList<Service>();
	
    public AllStylist() {}
    
    public void set_ID(int id) {
    	this.id = id;
	}
    
    public void set_Name(String name) {
    	this.name = name;
	}
    
    public void set_Job_title(String job_title) {
    	this.job_title = job_title;
	}
    
    public void set_Salon(String salon) {
    	this.salon = salon;
	}
    
    public void set_Address(String address) {
    	this.address = address;
	}

    public void set_Picture(String picture) {
    	this.picture = picture;
	}    
    
    public void set_Service(ArrayList<Service> service) {
    	this.service = service;
	}

    public static String convertToJson(ArrayList<AllStylist> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}