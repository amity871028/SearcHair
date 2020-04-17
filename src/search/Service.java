package search;

public class Service{

	String name;
	int min_price,max_price,service_time;
	String description;
	
	public Service() {}
	
    public void set_Name(String name) {
    	this.name = name;
	}
    
    public void set_Min_price(int min_price) {
    	this.min_price = min_price;
	}
    
    public void set_Max_price(int max_price) {
    	this.max_price = max_price;
	}

    public void set_Service_time(int service_time) {
    	this.service_time = service_time;
	}  
    
    public void set_Description(String description) {
    	this.description = description;
	}	
}