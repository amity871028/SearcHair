package search;
import java.util.ArrayList;
import java.io.*;
import com.google.gson.Gson;

public class Service{
	String name,min_price,max_price,service_time,description;
	
    public Service(String name,String min_price,String max_price,String service_time,String description) {
		this.name = name;
		this.min_price = min_price;
		this.max_price = max_price;
		this.service_time = service_time;
		this.description = description;
    }
}
