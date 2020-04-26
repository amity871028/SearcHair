package api;

import java.io.File;
import java.io.IOException;

import hairMatch.*;

public class HairMatchApi {
	GetHairstyle hair = new GetHairstyle();
	ColorHair colorHair = new ColorHair();

	public String getAllHairstyles(String type) throws IOException {
		return hair.getAllPictures(type);
	}

	public String getColorHairPicutre(String picture, String color, String userName) throws IOException {
		String path = colorHair.createFolder(userName);
		File colorFile = colorHair.getColorPicture(path, color);
		return colorHair.getColorHair(path, picture, colorFile);
	}
}
