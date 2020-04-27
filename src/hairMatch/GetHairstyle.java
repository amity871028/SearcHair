package hairMatch;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetHairstyle {

	public String getAllPictures(String type) throws IOException {
		String project = ColorHair.class.getClassLoader().getResource("").toString();
		project = project.substring(6, project.length() - 90);
		String path = "/SearcHair/WebContent/static/img/hair-match/hairstyle-source/" + type;
		ArrayList<String> allHairstyle = new ArrayList<String>();
		try {
			File file = new File(project + path);
			File[] f1 = file.listFiles(); // 獲取目錄下的所有檔案或資料夾
			for (int i = 0; i < file.listFiles().length; i++) {
				String filePath = f1[i].getPath();
				filePath = filePath.substring(filePath.indexOf("static"), filePath.length()); // 取相對路徑
				allHairstyle.add(filePath);
			}
		} catch (Exception e) {
			System.out.println("此資料夾不存在");
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(allHairstyle); // 轉成JSON檔
		return jsonStr;
	}
}