package hairMatch;

import java.io.File;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetHairstyle {

	public void getPath(String type) {
		String path = "WebContent/static/img/hair-match/�Y�v����/" + type;
		File file = new File(path);
		String[] filelist = file.list();
		ArrayList<String> allHairstyle = new ArrayList<String>();
		for (int i = 0; i < filelist.length; i++) {
			File readfile = new File(file + "//" + filelist[i]);
			allHairstyle.add(readfile.getPath());
		}
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String jsonStr = gson.toJson(allHairstyle);
		System.out.println(jsonStr); // ======�o�̿�XJSON=====
	}

	public static void main(String args[]) {
		String type = "�k�͵u�v"; // �i��J���P���v������
		GetHairstyle hair = new GetHairstyle();
		hair.getPath(type);
	}
}