package api;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.AddFavorite;

public class FavoriteApi {
	AddFavorite favorite = new AddFavorite();

	public boolean checkSalon(String jsonObject) throws IOException {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").toString();
		function = function.substring(1, function.length() - 1); // 刪掉前後的"字元
		String account = jobj.get("account").toString();
		account = account.substring(1, account.length() - 1); // 刪掉前後的"字元
		String id = jobj.get("id").toString();

		boolean result = false;
		if (function.equals("salon")) {
			result = favorite.addSalon(account, Integer.valueOf(id));
		} else if ((function.equals("stylist")))
			result = favorite.addStylist(account, Integer.valueOf(id));
		else if ((function.equals("stylist_works")))
			result = favorite.addStylistWorks(account, Integer.valueOf(id));

		return result;

	}
}
