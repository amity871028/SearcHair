package search;

public class Work{
	int id;
	String picture;
	
	public Work() {
		
	}
	
    public Work(int id,String picture) {
    	this.id = id;
		this.picture = picture;
    }
    
    public void set_ID(int id) {
    	this.id = id;
	}
    
    public void set_Picture(String picture) {
    	this.picture = picture;
	}
}
