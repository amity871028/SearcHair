package search;
import java.util.ArrayList;
import java.io.*;
import com.google.gson.Gson;

public class Stylist{ 
	//String test;
	String id,name, job_title,salon, address, picture;
	ArrayList<String> service = new ArrayList<String>();
	ArrayList<String> price = new ArrayList<String>();
	
    public Stylist(int id,String name,String job_title,int salon,String address,String picture,ArrayList<String> serviceName,ArrayList<String> servicePrice) {
    	//test = String.valueOf(id);
    	this.id = String.valueOf(id);
		this.name = name;
		this.job_title = job_title;
    	this.salon = String.valueOf(salon);
		this.address = address;
		this.picture = picture;
		service = serviceName;
		price = servicePrice;
    }
}