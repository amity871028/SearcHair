package search;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AllSalon {
	int id;
	String name, address, phone,picture;
	ArrayList<String> service = new ArrayList<String>();

	public AllSalon() {}
	
    public void set_ID(int id) {
    	this.id = id;
	}
    
    public void set_Name(String name) {
    	this.name = name;
	}
    
    public void set_Address(String address) {
    	this.address = address;
	}
    
    public void set_Phone(String phone) {
    	this.phone = phone;
	}
    
    public void set_Picture(String picture) {
    	this.picture = picture;
	}
    
    public void set_Service(ArrayList<String> service) {
    	this.service = service;
	}
    
    public static String convertToJson(ArrayList<AllSalon> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}