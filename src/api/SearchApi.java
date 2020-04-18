package api;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import JDBC.SearchMysql;

public class SearchApi {
	
	SearchMysql searchMysql = new SearchMysql();
	
	public String getAllSalon() {
		return searchMysql.searchSalon();
	}
	
	public String getAllStylist() {
		return searchMysql.searchStylist();
	}
	
	public String getAllStylistWorks() {
		return searchMysql.searchStylistWorks();
	}
	
	public String getOneSalon(int id) {
		return searchMysql.searchOneSalon(id);
	}
	
	public String getOneStylist(int id) {
		return searchMysql.searchOneStylist(id);
	}
	
	public String getOneStylistWorks(int id) {
		return searchMysql.searchOneStylistWork(id);
	}

}
