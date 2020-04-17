package search;

public class Work{
	int id;
	String picture;
	
	public Work() {}
	
    public Work(int id,String picture) {
    	this.id = id;
		this.picture = picture;
    }
    
    public void setID(int id) {
    	this.id = id;
	}
    
    public void setPicture(String picture) {
    	this.picture = picture;
	}
}
