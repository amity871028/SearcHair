package search;
import java.util.ArrayList;
import java.io.*;
import com.google.gson.Gson;

public class StylistDetail{
	//int id;
	String name, job_title,salon, address, picture, service, price,description;
	
    public StylistDetail(String name,String job_title,int salon,String address,String picture) {
    	this.name = name;
		this.job_title = job_title;
    	this.salon = String.valueOf(salon);
		this.address = address;
		this.picture = picture;
    }
}
