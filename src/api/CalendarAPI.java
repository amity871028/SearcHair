package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.CalendarMySQL;

public class CalendarAPI {
	
	private CalendarMySQL calendar = new CalendarMySQL();
	
	public boolean costNewJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jobj.get("account").toString();
		String time = jobj.get("time").toString();
		String category = jobj.get("category").toString();
		String kind = jobj.get("kind").toString();
		String cost = jobj.get("cost").toString();
		String description = jobj.get("description").toString();
		String color = jobj.get("color").toString();
		
		return calendar.newCost(account, time, Integer.parseInt(category), kind, Integer.parseInt(cost), description, color);
	}
}
