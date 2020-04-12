package search;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import api.SearchApi;


/**
 * Servlet implementation class SearchServlet
 */
@WebServlet("/api/search")
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ALL_SALON = "salon";
	private static final String ALL_STYLIST = "stylist";
	private static final String ALL_STYLIST_WORK = "stylist_works";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("application/json; charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		reader.close();
		Search search = new Search();
		ArrayList<String> resultList =  new ArrayList<String>();
		String doOp = SearchApi.searchAll(json);
		if (doOp.equals(ALL_SALON)) {
			resultList = search.searchSalon();
		} else if ((doOp.equals(ALL_STYLIST))) {
			resultList = search.searchStylist();
		} else if ((doOp.equals(ALL_STYLIST_WORK))) {
			resultList = search.searchHairstyle();
		}
		Gson gson = new Gson();
		response.getWriter().append(gson.toJson(resultList));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
}
