package chatbot;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


public class ChatbotSalon {
	String name, address, phone, picture, businessTime;
	String func = "salon";

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

	public static String convertToJson(ArrayList<ChatbotSalon> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}
