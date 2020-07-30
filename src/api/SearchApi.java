package api;

import jdbc.SearchMySQL;

public class SearchApi {

	SearchMySQL searchMysql = new SearchMySQL();

	public String getAllSalon(int page, String keyword, String[] service) {
		return searchMysql.searchSalon(page, keyword, service);
	}

	public String getAllStylist(int page, String keyword, String[] serviceArray, int[] price) {
		return searchMysql.searchStylist(page, keyword, serviceArray, price);
	}

	public String getAllStylistWorks(int page, String keyword) {
		return searchMysql.searchStylistWorks(page, keyword);
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

	public String getAllProduct(int page, String keyword, String type, int feature) {
		return searchMysql.searchProduct(page, keyword, type, feature);
	}

	public String getOneProduct(int id) {
		return searchMysql.searchOneProduct(id);
	}
}