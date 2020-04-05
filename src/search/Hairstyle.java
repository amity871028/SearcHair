package search;
import java.util.ArrayList;
import java.io.*;
import com.google.gson.Gson;

public class Hairstyle{ 

	String id,picture,stylist,stylist_job_title,description,hashtag;
	
    public Hairstyle(int id,String picture,int stylist,String stylist_job_title,String description,String hashtag) {
		this.id = String.valueOf(id);
    	this.picture = picture;
		this.stylist = String.valueOf(stylist);
		this.stylist_job_title = stylist_job_title;
		this.description = description;
		this.hashtag = hashtag;
    }
}