package search;

import java.util.ArrayList;

public class StylistInfo {

	int id;
	String name,picture;
	ArrayList<Work> works = new ArrayList<Work>(); 

	public StylistInfo() {}
	
    public StylistInfo(int id,String name,String picture) {
    	this.id = id;
		this.name = name;
		this.picture = picture;
    }
    
    public void set_ID(int id) {
    	this.id = id;
	}
    
    public void set_Name(String name) {
    	this.name = name;
	}
    
    public void set_Picture(String picture) {
    	this.picture = picture;
	}
    
    public void set_Works(ArrayList<Work> works) {
    	this.works = works;
	}    
}