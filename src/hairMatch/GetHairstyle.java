package hairMatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;

public class GetHairstyle {

	public String getAllPictures(String url, String hairstyleFolderRealPath, String type) throws IOException {
		String path = hairstyleFolderRealPath + "/" + type;
		ArrayList<String> photos = new ArrayList<String>();
		try {
			File user = new File(path);
			String[] filenames;
			filenames = user.list();
			for (int i = 0; i < filenames.length; i++) {
				photos.add(filenames[i]);
			}
		} catch (Exception e) {
			System.out.println("此資料夾不存在");
		}
		HairStyle hairStyle = new HairStyle(url, photos);
		Gson gson = new Gson();
		String jsonStr = gson.toJson(hairStyle);
		return jsonStr;
	}
}