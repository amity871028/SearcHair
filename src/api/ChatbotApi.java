package api;

import java.util.Random;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import jdbc.ChatbotMySQL;
import jdbc.SearchMySQL;
import jdbc.SettingMySQL;

public class ChatbotApi {

	public String jsonAnalyize(String intent, String input, String user) {

		ChatbotMySQL chatBot = new ChatbotMySQL();
		SettingMySQL setting = new SettingMySQL();
		SearchMySQL search = new SearchMySQL();

		String result = null;
		if (intent.equals("salon")) {
			System.out.println("salon");
			result = chatBot.getSalon(input);
		} else if (intent.equals("stylist")) {
			System.out.println("stylist");
			result = chatBot.getStylist(input);
		} else if (intent.equals("question")) {

		} else if (intent.equals("shampoo")) {
			String analysisResult = setting.getHairAnalysis(user);
			if (analysisResult.equals("oily")) {
				result = search.searchProduct(1, null, "洗髮乳", 4);
			} else if (analysisResult.equals("normal")) {
				result = search.searchProduct(1, null, "洗髮乳", -1);
			} else if (analysisResult.equals("dry")) {
				result = search.searchProduct(1, null, "洗髮乳", 1);
			} else if (analysisResult.equals("damaged")) {
				result = search.searchProduct(1, null, "洗髮乳", 0);
			} else if (analysisResult.equals("combination")) {
				result = search.searchProduct(1, null, "洗髮乳", 2);
			} else {
				result = search.searchProduct(1, null, "洗髮乳", -1);
			}

			JsonArray jobArray = new Gson().fromJson(result, JsonArray.class);

			Random r = new Random();
			int random = r.nextInt(jobArray.size());
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			result = gson.toJson(jobArray.get(random));
		}
		return result;
	}

}
