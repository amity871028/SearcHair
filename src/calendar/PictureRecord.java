package calendar;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class PictureRecord {
	int id;
	String account;
	String description;
	String picture;
	String time;

	public void setId(int id) {
		this.id = id;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPictrue(String picture) {
		this.picture = picture;
	}

	public void setTime(String time) {
		this.time = time;
	}

	// get
	public int getId() {
		return id;
	}

	public String getAccount() {
		return account;
	}

	public String getDescription() {
		return description;
	}

	public String getPicture() {
		return picture;
	}

	public String getTime() {
		return time;
	}

	public static String convertToJson(ArrayList<PictureRecord> allPictureData) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(allPictureData);
		return jsonStr;
	}
}
