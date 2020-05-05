package hairMatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetHairstyle {

	public String getAllPictures(String url, String hairstyleFolderRealPath, String type) throws IOException {
		String path = hairstyleFolderRealPath + "/" + type;
		ArrayList<String> allHairstyle = new ArrayList<String>();
		try {
			File user = new File(path);
			String[] filenames;
			filenames = user.list();
			for (int i = 0; i < filenames.length; i++) {
				allHairstyle.add(url + "/" + filenames[i]);
			}
		} catch (Exception e) {
			System.out.println("此資料夾不存在");
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(allHairstyle); // 轉成JSON檔
		return jsonStr;
	}
}