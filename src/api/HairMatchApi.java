package api;

import java.io.File;
import java.io.IOException;

import hairMatch.*;

public class HairMatchApi {
	GetHairstyle hair = new GetHairstyle();
	ColorHair colorHair = new ColorHair();

	public String getAllHairstyles(String url, String hairstyleFolderRealPath, String type) throws IOException {
		return hair.getAllPictures(url, hairstyleFolderRealPath, type);
	}

	public String getColorHairPicutre(String userFolderRealPath, String hairstyleFolderRealPath, String folder,
			String picture, String color, String userName) throws IOException {
		String path = colorHair.createFolder(userFolderRealPath, userName);
		File colorFile = colorHair.getColorPicture(path, color);
		return colorHair.getColorHair(hairstyleFolderRealPath, folder, path, picture, colorFile);
	}
}
