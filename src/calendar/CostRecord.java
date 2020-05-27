package calendar;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class CostRecord {
	int id;
	String account;
	String time;
	String category;
	String kind;
	int cost;
	String description;
	String color;

	public void setId(int id) {
		this.id = id;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setColor(String color) {
		this.color = color;
	}

	// get
	public int getId() {
		return id;
	}

	public String getAccount() {
		return account;
	}

	public String getTime() {
		return time;
	}

	public String getCategory() {
		return category;
	}

	public String getKind() {
		return kind;
	}

	public int getCost() {
		return cost;
	}

	public String getDescription() {
		return description;
	}

	public String getColor() {
		return color;
	}

	public static String convertToJson(ArrayList<CostRecord> allCostData) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(allCostData);
		return jsonStr;
	}
}
