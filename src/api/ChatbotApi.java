package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.ChatbotMySQL;

public class ChatbotApi {

	public String jsonAnalyize(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		ChatbotMySQL chatBot = new ChatbotMySQL();
		String function = jobj.get("func").getAsString();
		String keyword = jobj.get("keyword").getAsString();

		if (function.equals("店家")) {
			return chatBot.getSalon(keyword);
		} else if (function.equals("設計師")) {
			return chatBot.getStylist(keyword);
		} else if (function.equals("詢問")) {
			return chatBot.getAnswer(keyword);
		} else {
			return "[]";
		}
	}
}
