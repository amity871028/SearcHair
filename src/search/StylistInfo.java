package search;
import java.util.ArrayList;
import java.io.*;
import com.google.gson.Gson;

public class StylistInfo {

	String id,name,picture;
	
    public StylistInfo(int id,String name,String picture) {
    	this.id = String.valueOf(id);
		this.name = name;
		this.picture = picture;
    }
}
