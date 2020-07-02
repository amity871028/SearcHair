package api;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.FavoriteMySQL;

public class FavoriteApi {
	FavoriteMySQL favorite = new FavoriteMySQL();

	public String getSalon(String account) {
		return favorite.getSalonId(account);
	}

	public String getStylist(String account) {
		return favorite.getStylistId(account);
	}

	public String getStylistWorks(String account) {
		return favorite.getStylistWorksId(account);
	}

	public String getAlbum(String account) {
		return favorite.getAlbum(account);
	}

	public String getPhoto(String account, int album) {
		return favorite.getPhoto(account, album);
	}

	public boolean addFavorite(String jsonObject) throws IOException {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		String account = jobj.get("account").getAsString();

		boolean result = false;
		if (function.equals("salon")) {
			String id = jobj.get("id").toString();
			result = favorite.addSalon(account, Integer.valueOf(id));
		} else if ((function.equals("stylist"))) {
			String id = jobj.get("id").toString();
			result = favorite.addStylist(account, Integer.valueOf(id));
		} else if ((function.equals("stylist_works"))) {
			String id = jobj.get("id").toString();
			result = favorite.addStylistWorks(account, Integer.valueOf(id));
		} else if ((function.equals("album"))) {
			String albumName = jobj.get("albumName").getAsString();
			result = favorite.addAlbum(account, albumName);
		} else if ((function.equals("photo"))) {
			String id = jobj.get("album").toString();
			String description = jobj.get("description").getAsString();
			String picture = jobj.get("picture").getAsString();
			result = favorite.addPhoto(account, Integer.valueOf(id), description, picture);
		}
		return result;
	}

	public boolean deleteFavorite(String jsonObject) throws IOException {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String function = jobj.get("func").getAsString();
		String account = jobj.get("account").getAsString();
		String id = jobj.get("id").toString();

		boolean result = false;
		if (function.equals("salon")) {
			result = favorite.deleteSalon(account, Integer.valueOf(id));
		} else if ((function.equals("stylist"))) {
			result = favorite.deleteStylist(account, Integer.valueOf(id));
		} else if ((function.equals("stylist_works"))) {
			result = favorite.deleteStylistWorks(account, Integer.valueOf(id));
		} else if ((function.equals("album"))) {
			result = favorite.deleteAlbum(account, Integer.valueOf(id));
		} else if ((function.equals("photo"))) {
			result = favorite.deletePhoto(account, Integer.valueOf(id));
		}
		return result;
	}
}
