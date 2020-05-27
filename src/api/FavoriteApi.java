package api;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.FavoriteMySQL;

public class FavoriteApi {
	FavoriteMySQL favorite = new FavoriteMySQL();

	public boolean addFavorite(String jsonObject) throws IOException {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		String account = jobj.get("account").getAsString();
		String id = jobj.get("id").toString();

		boolean result = false;
		if (function.equals("salon"))
			result = favorite.addSalon(account, Integer.valueOf(id));
		else if ((function.equals("stylist")))
			result = favorite.addStylist(account, Integer.valueOf(id));
		else if ((function.equals("stylist_works")))
			result = favorite.addStylistWorks(account, Integer.valueOf(id));

		return result;

	}

	public boolean deleteFavorite(String jsonObject) throws IOException {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		String account = jobj.get("account").getAsString();
		String id = jobj.get("id").toString();

		boolean result = false;
		if (function.equals("salon"))
			result = favorite.deleteSalon(account, Integer.valueOf(id));
		else if ((function.equals("stylist")))
			result = favorite.deleteStylist(account, Integer.valueOf(id));
		else if ((function.equals("stylist_works")))
			result = favorite.deleteStylistWorks(account, Integer.valueOf(id));

		return result;

	}
}
