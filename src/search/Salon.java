package search;
import java.util.ArrayList;
import java.io.*;
import com.google.gson.Gson;

public class Salon{
	String id,name, address, phone,businessTime,picture;
	ArrayList<String> service = new ArrayList<String>();
	
    public Salon(int id,String name,String address,String phone,String businessTime,String picture,ArrayList<String> service) {
    	this.id = String.valueOf(id);
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.picture = picture;
		this.service = service;
    }
}