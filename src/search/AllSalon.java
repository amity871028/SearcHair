package search;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AllSalon {
	int id;
	String name, address, phone,picture;
	ArrayList<String> service = new ArrayList<String>();

	public AllSalon() {}
	
    public void setID(int id) {
    	this.id = id;
	}
    
    public void setName(String name) {
    	this.name = name;
	}
    
    public void setAddress(String address) {
    	this.address = address;
	}
    
    public void setPhone(String phone) {
    	this.phone = phone;
	}
    
    public void setPicture(String picture) {
    	this.picture = picture;
	}
    
    public void setService(ArrayList<String> service) {
    	this.service = service;
	}
    
    public static String convertToJson(ArrayList<AllSalon> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}