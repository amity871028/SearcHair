package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import JDBC.mySql;

public class userApi {
	
	private mySql mysql = new mySql();
	
	public userApi() {
		super();
	}
	
	public int loginJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jobj.get("account").toString();
		String password = jobj.get("password").toString();
		return mysql.checkUser(account, password);
	}
	
	public int registerJsonAnalyzing(String jsonObject) {

		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jsonobj.get("account").toString();
		String name = jsonobj.get("name").toString();
		String password = jsonobj.get("password").toString();

		return mysql.insertUser(account, password, name);
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		userApi test = new userApi();
		String myJSONString = "{'account': 'pegylee112', 'name': 'peipei', 'password': '1234567'}";

		// sign up
		
		// int result = test.registerJsonAnalyzing(myJSONString); 
		// System.out.println(result);
		
		// sign in
		/*
		 * int result = test.loginJsonAnalyzing(myJSONString); System.out.println(result);
		 */
	}
	
}
