package search;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AllHairstyle{ 
	int id;
	String picture,stylist,stylist_job_title,description,hashtag;

    public void set_ID(int id) {
    	this.id = id;
	}
    
    public void set_Picture(String picture) {
    	this.picture = picture;
	}
    
    public void set_Stylist(String stylist) {
    	this.stylist = stylist;
	}
    
    public void set_Stylist_job_title(String stylist_job_title) {
    	this.stylist_job_title = stylist_job_title;
	}
    
    public void set_Description(String description) {
    	this.description = description;
	}
    
    public void set_Hashtag(String hashtag) {
    	this.hashtag = hashtag;
	}
    
    public String convertToJson(ArrayList<AllHairstyle> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}