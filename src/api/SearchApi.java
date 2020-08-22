package api;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import hairMatch.ToImgur;
import jdbc.SearchMySQL;
import search.Product;

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

	public boolean productJsonAnalyzing(String jsonObject) throws IOException {
		JsonObject jobj = new Gson().fromJson(jsonObject, JsonObject.class);
		Product product = new Gson().fromJson(jobj, Product.class);
		String action = jobj.get("action").getAsString();
		if (!action.equals("delete") && product.getPicture().startsWith("data:image/")) {
			String pictureBase64 = product.getPicture();
			ToImgur toImgur = new ToImgur();
			String picture = toImgur.getImgur(pictureBase64, null, "img");
			product.setPicture(picture);
		}
		boolean result = false;
		if (action.equals("new"))
			result = searchMysql.newProduct(product);
		else if (action.equals("update"))
			result = searchMysql.updateProduct(product);
		else if (action.equals("delete"))
			result = searchMysql.deleteProduct(product.getId());

		return result;

	}
}