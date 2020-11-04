package api;

import java.io.File;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hairMatch.*;
import jdbc.HairMatchMySQL;
import jdbc.SearchMySQL;

public class HairMatchApi {
	GetHairstyle hair = new GetHairstyle();
	ColorHair colorHair = new ColorHair();
	HairMatchMySQL hairMatch = new HairMatchMySQL();
	
	SearchMySQL searchMysql = new SearchMySQL();
	
	public String getAllHairstyles(String url, String hairstyleFolderRealPath, String type) throws IOException {
		return hair.getAllPictures(url, hairstyleFolderRealPath, type);
	}

	public String getColorHairPicutre(String userFolderRealPath, String hairstyleFolderRealPath, String folder,
			String picture, String color, String userName) throws IOException {
		String path = colorHair.createFolder(userFolderRealPath, userName);
		File colorFile = colorHair.getColorPicture(path, color);
		return colorHair.getColorHair(hairstyleFolderRealPath, folder, path, picture, colorFile);
	}

	public String getJsonToImgur(String jsonObject, String userFolderRealPath) throws IOException {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String imgData = jobj.get("img").toString();
		ToImgur toImgur = new ToImgur();
		return toImgur.getImgur(imgData, null, userFolderRealPath);
	}

	public boolean savePicture(String userName, String hairstyle, String color, String url) {
		return hairMatch.savePhoto(userName, hairstyle, color, url);
	}

	public String getRandomPhoto(String hairstyle) {
		return hairMatch.getPhoto(hairstyle);
	}
	
	public String getStyleWorks(String faceShape, int page) {
		return hairMatch.getStylistWorks(faceShape, page);
	}

	public String getSameHairstyle(int stylistWorksId) {
		String stylistWorksJson = searchMysql.searchOneStylistWork(stylistWorksId);
		JsonObject jobj = new Gson().fromJson(stylistWorksJson, JsonObject.class);
		String hashtag = jobj.get("hashtag").toString();
		return hairMatch.getSameHairstyle(hashtag);
	}

	public String getMatchedHairstyle(String keyword) {
		return hairMatch.getMatchedHairstyle(keyword);
	}
}
