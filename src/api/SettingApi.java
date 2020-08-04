package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import jdbc.SettingMySQL;

public class SettingApi {
	private SettingMySQL setting = new SettingMySQL();

	public String setHairAnalysis(String jsonObject) {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);

		String account = jobj.get("account").toString();
		boolean greasy = jobj.get("greasy").getAsBoolean();
		boolean frizzy = jobj.get("frizzy").getAsBoolean();
		boolean sleek = jobj.get("sleek").getAsBoolean();
		boolean tangled = jobj.get("tangled").getAsBoolean();
		boolean perm = jobj.get("perm").getAsBoolean();

		String result = "none";

		if (greasy == true) {
			if (frizzy == true)
				result = "combination";
			else
				result = "oily";
		} else {
			if (perm == true) {
				if (frizzy == true || sleek == false)
					result = "damaged";
				else {
					if (sleek == true || tangled == false)
						result = "normal";
					else
						result = "dry";
				}
			} else {
				if (sleek == true || tangled == false)
					result = "normal";
				else
					result = "dry";
			}
		}

		return setting.setHairAnalysis(account, result);
	}

}
