package search;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class jsonAn {
	
	public jsonAn() {
		
	}
	
	public static String[] jsonAnaOne(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String[] doOp = new String[2];
		doOp[0] = jobj.get("function").toString().replace("\"", "");
		doOp[1] = jobj.get("id").toString().replace("\"", "");
		return doOp;
	}
	
	public static String jsonAna(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String doOp = jobj.get("function").toString().replace("\"", "");
		return doOp;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
