package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.CalendarMySQL;

public class CalendarApi {

	private CalendarMySQL calendar = new CalendarMySQL();

	// =================== to know what action will do ======================//

	public boolean newJsonAnalyzing(String jsonObject) {
		
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		if (function.equals("cost"))
			return costNewJsonAnalyzing(jsonObject);
		else if (function.equals("activity"))
			return activityNewJsonAnalyzing(jsonObject);
		else if (function.equals("picture"))
			return pictureNewJsonAnalyzing(jsonObject);
		else
			return false;

	}

	public boolean deleteJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		if (function.equals("cost"))
			return costDeleteJsonAnalyzing(jsonObject);
		else if (function.equals("activity"))
			return activityDeleteJsonAnalyzing(jsonObject);
		else if (function.equals("picture"))
			return pictureDeleteJsonAnalyzing(jsonObject);
		else
			return false;

	}

	public boolean updateJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		if (function.equals("cost"))
			return costUpdateJsonAnalyzing(jsonObject);
		else if (function.equals("activity"))
			return activityUpdateJsonAnalyzing(jsonObject);
		else if (function.equals("picture"))
			return pictureUpdateJsonAnalyzing(jsonObject);
		else
			return false;

	}
	
	public String getJsonAnalyzing(String function, String account, int year, int month) {
		if (function.equals("cost"))
			return calendar.getCost(account, year, month);
		else if (function.equals("activity"))
			return calendar.getActivity(account, year, month);
		else if (function.equals("picture"))
			return calendar.getPicture(account, year, month);
		else
			return null;
	}

	// =================== cost function ======================//

	public boolean costNewJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		CostRecord costRecord = new Gson().fromJson(jobj, CostRecord.class);
		
		return calendar.newCost(costRecord);
	}

	public boolean costUpdateJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		CostRecord costRecord = new Gson().fromJson(jobj, CostRecord.class);
		
		return calendar.updateCost(costRecord);
	}

	public boolean costDeleteJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jobj.get("account").getAsString();
		String id = jobj.get("id").toString();

		return calendar.deleteCost(account, Integer.parseInt(id));
	}

	// =================== activity function ======================//

	public boolean activityNewJsonAnalyzing(String jsonObject) {
		
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		ActivityRecord activityRecord = new Gson().fromJson(jobj, ActivityRecord.class);

		return calendar.newActivity(activityRecord);
	}

	public boolean activityUpdateJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		ActivityRecord activityRecord = new Gson().fromJson(jobj, ActivityRecord.class);

		return calendar.updateActivity(activityRecord);
	}

	public boolean activityDeleteJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jobj.get("account").getAsString();
		String id = jobj.get("id").toString();

		return calendar.deleteActivity(account, Integer.parseInt(id));
	}

	// =================== picture function ======================//

	public boolean pictureNewJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		PictureRecord pictureRecord  = new Gson().fromJson(jobj, PictureRecord.class);

		return calendar.newPicture(pictureRecord);
	}

	public boolean pictureUpdateJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		PictureRecord pictureRecord  = new Gson().fromJson(jobj, PictureRecord.class);

		return calendar.updatePicture(pictureRecord);
	}

	public boolean pictureDeleteJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jobj.get("account").getAsString();
		String id = jobj.get("id").toString();

		return calendar.deletePicture(account, Integer.parseInt(id));
	}
}
