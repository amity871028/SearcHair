package api;

import javax.servlet.http.Cookie;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import JDBC.mySql;
import user.makeToken;

public class UserApi {
	
	private mySql mysql = new mySql();
	
	public UserApi() {
		super();
	}
	
	public boolean loginJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jobj.get("account").toString();
		String password = jobj.get("password").toString();
		return mysql.userChecking(account, password);
	}
	
	public boolean registerJsonAnalyzing(String jsonObject) {

		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jsonobj.get("account").toString();
		String name = jsonobj.get("name").toString();
		String password = jsonobj.get("password").toString();

		return mysql.userInsertion(account, password, name);
	}
	
	public String forgetPwdJsonAnalyzing(String jsonObject) {

		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jsonobj.get("account").toString();
		String name = jsonobj.get("name").toString();
		if (mysql.userCertification(account, name) == true) return account;
		else return "fail";
	}
	
	public boolean resetPwdJsonAnalyzing(String jsonObject, String userToken) throws Exception {

		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String password = jsonobj.get("password").toString();
		makeToken token = new makeToken();
		/*if (token.decrypt(userToken).equals(userToken)) {
			return mysql.userResetPwd(account, password);
		}
		else {
			return false;
		}*/
		return mysql.userResetPwd(token.decrypt(userToken), password);
	}
	
	public String getValueFromCookie (Cookie[] cookies, String cookieKey) {
		String value = null;
		if(cookies != null)
		{
			
			for(int i = 0; i < cookies.length; i++)
			{
				if(cookies[i].getName().equals(cookieKey))
				{
					value = cookies[i].getValue();
					break;
				}
			}
		}
		return value;
	}
	
	public boolean checkUser(String jsonObject, String userToken) throws Exception {
		
		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String oldPassword = jsonobj.get("oldPassword").toString();
		String newPassword = jsonobj.get("newPassword").toString();
		makeToken token = new makeToken();
		String account = token.decrypt(userToken);
		
		if(mysql.userChecking(account, oldPassword) == true){
			return mysql.userResetPwd(account, newPassword);
		} else {
			return false;
		}
	}
	
	
	public static void main(String[] args) {
		UserApi test = new UserApi();
		String myJSONString = "{'account': 'test', 'name': 'test'}";

		// sign up
		boolean result = test.registerJsonAnalyzing(myJSONString); 
		System.out.println(result);
		
		// sign in
		boolean result1 = test.loginJsonAnalyzing(myJSONString);
		System.out.println(result1);
		
		//forgetPwd
		String result2 = test.forgetPwdJsonAnalyzing(myJSONString);
		System.out.println(result2);
		
	}
	
}
