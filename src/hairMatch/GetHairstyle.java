package hairMatch;

import java.io.File;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetHairstyle {

	public void getPath(String type) {
		String path = "WebContent/static/img/hair-match/頭髮素材/" + type;
		File file = new File(path);
		String[] filelist = file.list();
		ArrayList<String> allHairstyle = new ArrayList<String>();
		for (int i = 0; i < filelist.length; i++) {
			File readfile = new File(file + "//" + filelist[i]);
			allHairstyle.add(readfile.getPath());
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(allHairstyle);
		System.out.println(jsonStr); // ======這裡輸出JSON=====
	}

	public static void main(String args[]) {
		String type = "女生短髮"; // 可輸入不同的髮型種類
		GetHairstyle hair = new GetHairstyle();
		hair.getPath(type);
	}
}