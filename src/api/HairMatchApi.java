package api;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hairMatch.*;

public class HairMatchApi {
	GetHairstyle hair = new GetHairstyle();
	ColorHair colorHair = new ColorHair();

	public String getAllHairstyles(String url, String hairstyleFolderRealPath, String type) throws IOException {
		return hair.getAllPictures(url, hairstyleFolderRealPath, type);
	}

	public String getColorHairPicutre(String userFolderRealPath, String hairstyleFolderRealPath, String folder,
			String picture, String color) throws IOException {
		File colorFile = colorHair.getColorPicture(userFolderRealPath, color);
		return colorHair.getColorHair(hairstyleFolderRealPath, folder, userFolderRealPath, picture, colorFile);
	}

	public String getJsonToImgur(String jsonObject, String userFolderRealPath) throws IOException {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String imgData = jobj.get("img").toString();
		ToImgur toImgur = new ToImgur();
		return toImgur.getImgur(imgData, null, userFolderRealPath);
	}

}
