package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.CalendarMySQL;

public class CalendarApi {

	private CalendarMySQL calendar = new CalendarMySQL();

	// =================== to know what action will do ======================//

	public boolean newJsonAnalyzing(String account, String jsonObject) {
		
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		if (function.equals("cost"))
			return costNewJsonAnalyzing(account, jsonObject);
		else if (function.equals("activity")) {
			return activityNewJsonAnalyzing(account, jsonObject);
			}
		else if (function.equals("picture"))
			return pictureNewJsonAnalyzing(account, jsonObject);
		else
			return false;

	}

	public boolean deleteJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		if (function.equals("cost"))
			return costDeleteJsonAnalyzing(account, jsonObject);
		else if (function.equals("activity"))
			return activityDeleteJsonAnalyzing(account, jsonObject);
		else if (function.equals("picture"))
			return pictureDeleteJsonAnalyzing(account, jsonObject);
		else
			return false;

	}

	public boolean updateJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		if (function.equals("cost"))
			return costUpdateJsonAnalyzing(account, jsonObject);
		else if (function.equals("activity"))
			return activityUpdateJsonAnalyzing(account, jsonObject);
		else if (function.equals("picture"))
			return pictureUpdateJsonAnalyzing(account, jsonObject);
		else
			return false;

	}

	// =================== cost function ======================//

	public boolean costNewJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String time = jobj.get("time").getAsString();
		String category = jobj.get("category").getAsString();
		String kind = jobj.get("kind").getAsString();
		String cost = jobj.get("cost").toString();
		String description = jobj.get("description").getAsString();
		String color = jobj.get("color").toString();

		CostRecord costRecord = new CostRecord();
		costRecord.setAccount(account);
		costRecord.setTime(time);
		costRecord.setCategory(category);
		costRecord.setKind(kind);
		costRecord.setCost(Integer.parseInt(cost));
		costRecord.setDescription(description);
		costRecord.setColor(color);

		return calendar.newCost(costRecord);
	}

	public boolean costUpdateJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String id = jobj.get("id").toString();
		String time = jobj.get("time").getAsString();
		String category = jobj.get("category").getAsString();
		String kind = jobj.get("kind").getAsString();
		String cost = jobj.get("cost").toString();
		String description = jobj.get("description").getAsString();
		String color = jobj.get("color").toString();

		CostRecord costRecord = new CostRecord();
		costRecord.setId(Integer.parseInt(id));
		costRecord.setAccount(account);
		costRecord.setTime(time);
		costRecord.setCategory(category);
		costRecord.setKind(kind);
		costRecord.setCost(Integer.parseInt(cost));
		costRecord.setDescription(description);
		costRecord.setColor(color);

		return calendar.updateCost(costRecord);
	}

	public boolean costDeleteJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String id = jobj.get("id").toString();

		return calendar.deleteCost(account, Integer.parseInt(id));
	}
	
	public String getCostJsonAnalyzing() {
		return calendar.getCost();
	}

	// =================== activity function ======================//

	public boolean activityNewJsonAnalyzing(String account, String jsonObject) {
		
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String activityName = jobj.get("activityName").getAsString();
		String startTime = jobj.get("startTime").getAsString();
		String endTime = jobj.get("endTime").getAsString();
		String color = jobj.get("color").toString();
		String noticeTime = jobj.get("noticeTime").getAsString();

		ActivityRecord activityRecord = new ActivityRecord();
		activityRecord.setAccount(account);
		activityRecord.setActivityName(activityName);
		activityRecord.setStartTime(startTime);
		activityRecord.setEndTime(endTime);
		activityRecord.setNoticeTime(Integer.parseInt(noticeTime));
		activityRecord.setColor(color);

		return calendar.newActivity(activityRecord);
	}

	public boolean activityUpdateJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String id = jobj.get("id").toString();
		String activityName = jobj.get("activityName").getAsString();
		String startTime = jobj.get("startTime").getAsString();
		String endTime = jobj.get("endTime").getAsString();
		String color = jobj.get("color").toString();
		String noticeTime = jobj.get("noticeTime").toString();
		
		ActivityRecord activityRecord = new ActivityRecord();
		activityRecord.setId(Integer.parseInt(id));
		activityRecord.setAccount(account);
		activityRecord.setActivityName(activityName);
		activityRecord.setStartTime(startTime);
		activityRecord.setEndTime(endTime);
		activityRecord.setNoticeTime(Integer.parseInt(noticeTime));
		activityRecord.setColor(color);

		return calendar.updateActivity(activityRecord);
	}

	public boolean activityDeleteJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String id = jobj.get("id").toString();

		return calendar.deleteActivity(account, Integer.parseInt(id));
	}

	// =================== picture function ======================//

	public boolean pictureNewJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String pictureBlob = jobj.get("picture").getAsString();
		String description = jobj.get("description").getAsString();
		String time = jobj.get("time").getAsString();

		PictureRecord pictureRecord = new PictureRecord();
		pictureRecord.setAccount(account);
		pictureRecord.setPictrue(pictureBlob);
		pictureRecord.setDescription(description);
		pictureRecord.setTime(time);

		return calendar.newPicture(pictureRecord);
	}

	public boolean pictureUpdateJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String id = jobj.get("id").toString();
		String pictureBlob = jobj.get("picture").getAsString();
		String description = jobj.get("description").getAsString();
		String time = jobj.get("time").getAsString();

		PictureRecord pictureRecord = new PictureRecord();
		pictureRecord.setId(Integer.parseInt(id));
		pictureRecord.setAccount(account);
		pictureRecord.setPictrue(pictureBlob);
		pictureRecord.setDescription(description);
		pictureRecord.setTime(time);

		return calendar.updatePicture(pictureRecord);
	}

	public boolean pictureDeleteJsonAnalyzing(String account, String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String id = jobj.get("id").toString();

		return calendar.deletePicture(account, Integer.parseInt(id));
	}
}
