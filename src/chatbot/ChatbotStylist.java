package chatbot;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ChatbotStylist {
	int id;
	String name;
	String jobTitle;
	int salonId;
	String salon;
	String address;
	String picture;
	String func = "stylist";
    
    public void setID(int id) {
    	this.id = id;
	}
    
    public void setName(String name) {
    	this.name = name;
	}
    
    public void setJobTitle(String jobTitle) {
    	this.jobTitle = jobTitle;
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
    
    public static String convertToJson(ArrayList<ChatbotStylist> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}
