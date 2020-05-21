package api;

import javax.servlet.http.Cookie;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.UserMySQL;
import user.MakeToken;

public class UserApi {

	private UserMySQL user = new UserMySQL();

	// user login
	public String loginJsonAnalyzing(String jsonObject) {

		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jobj.get("account").toString();
		String password = jobj.get("password").getAsString();
		return user.userChecking(account, password);
	}

	// user register
	public boolean registerJsonAnalyzing(String jsonObject) {

		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jsonobj.get("account").toString();
		String name = jsonobj.get("name").getAsString();
		String password = jsonobj.get("password").toString();

		return user.userInsertion(account, password, name);
	}

	// forget password
	public String forgetPasswordJsonAnalyzing(String jsonObject) {

		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jsonobj.get("account").toString();
		String name = jsonobj.get("name").getAsString();
		if (user.userCertification(account, name) == true)
			return account;
		else
			return "fail";
	}
	
	// reset password
	public boolean resetPasswordJsonAnalyzing(String jsonObject, String userToken) throws Exception {

		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String password = jsonobj.get("password").toString();
		MakeToken token = new MakeToken();
		return user.userResetPassword(token.decrypt(userToken), password);
	}

	public String getValueFromCookie(Cookie[] cookies, String cookieKey) {
		String value = null;
		if (cookies != null) {

			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(cookieKey)) {
					value = cookies[i].getValue();
					break;
				}
			}
		}
		return value;
	}

	// Confirm whether the user is himself
	public boolean checkUser(String jsonObject) throws Exception {

		JsonObject jsonobj = new Gson().fromJson(jsonObject, JsonObject.class);
		String account = jsonobj.get("account").toString();
		String oldPassword = jsonobj.get("oldPassword").getAsString();
		String newPassword = jsonobj.get("newPassword").getAsString();
		
		if (user.userChecking(account, oldPassword) != null) {
			UserMySQL reset = new UserMySQL();
			return reset.userResetPassword(account, newPassword);
		} else {
			return false;
		}
	}
}
