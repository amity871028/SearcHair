package search;
import java.util.ArrayList;
import java.io.*;
import com.google.gson.Gson;

public class Work{
	String id,picture;
	
    public Work(int id,String picture) {
    	this.id = String.valueOf(id);
		this.picture = picture;
    }
}
