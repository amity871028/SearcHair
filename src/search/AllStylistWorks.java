package search;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AllStylistWorks {
	int id;
	String picture, stylist, stylist_job_title, description, hashtag;

	public void setID(int id) {
		this.id = id;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setStylist(String stylist) {
		this.stylist = stylist;
	}

	public void setStylistJobTitle(String stylist_job_title) {
		this.stylist_job_title = stylist_job_title;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public static String convertToJson(ArrayList<AllStylistWorks> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}