package search;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Salon {
	String name, address, phone, picture, businessTime;
	ArrayList<StylistInfo> stylist_info = new ArrayList<StylistInfo>();

	public void setName(String name) {
		this.name = name;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public void setBusinessTime(String businessTime) {
		this.businessTime = businessTime;
	}

	public void setStylistInfo(ArrayList<StylistInfo> stylist_info) {
		this.stylist_info = stylist_info;
	}

	public static String convertToJson(Salon Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}