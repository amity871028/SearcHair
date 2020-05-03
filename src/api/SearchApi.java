package api;


import jdbc.SearchMySQL;

public class SearchApi {
	
	SearchMySQL searchMysql = new SearchMySQL();
	
	public String getAllSalon(int page) {
		return searchMysql.searchSalon(page);
	}
	
	public String getAllStylist(int page) {
		return searchMysql.searchStylist(page);
	}
	
	public String getAllStylistWorks(int page) {
		return searchMysql.searchStylistWorks(page);
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
