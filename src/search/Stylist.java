package search;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Stylist{
	String name, job_title,salon, address,picture,description;
	ArrayList<Service> service = new ArrayList<Service>();
	ArrayList<Work> works = new ArrayList<Work>(); 

	public Stylist() {}

    public void setName(String name) {
    	this.name = name;
	}
    
    public void setJobTitle(String job_title) {
    	this.job_title = job_title;
	}
    
    public void setSalon(String salon) {
    	this.salon = salon;
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
    
    public void setWork(ArrayList<Work> works) {
    	this.works = works;
	}
    
    public static String convertToJson(Stylist Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}