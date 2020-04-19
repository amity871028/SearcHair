package api;


import jdbc.SearchMySQL;

public class SearchApi {
	
	SearchMySQL searchMysql = new SearchMySQL();
	
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
