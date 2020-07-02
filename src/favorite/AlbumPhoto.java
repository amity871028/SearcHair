package favorite;

import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class AlbumPhoto {
	int id;
	String description, photo;

	public void setID(int id) {
		this.id = id;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public static String convertToJson(ArrayList<AlbumPhoto> Item) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(Item);
		return jsonStr;
	}
}
