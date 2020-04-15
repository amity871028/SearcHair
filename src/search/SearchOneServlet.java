package search;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import api.SearchApi;

/**
 * Servlet implementation class SearchOneServlet
 */
@WebServlet("/api/search/detail")
public class SearchOneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ONE_SALON = "salon";
	private static final String ONE_STYLIST = "stylist";
	private static final String ONE_STYLIST_WORK = "stylist_works";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchOneServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("application/json; charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-ONEow-Origin", "*");
		response.setHeader("Access-Control-ONEow-Methods", "GET");
		
		String function = request.getParameter("func");
		String id = request.getParameter("id");
		Search search = new Search();
		
		String result = null;
		if (function.equals(ONE_SALON)) {
			System.out.println(ONE_SALON);
			result = search.oneSalon(Integer.parseInt(id));
		} else if (function.equals(ONE_STYLIST)) {
			System.out.println(ONE_STYLIST);
			result = search.oneStylist(Integer.parseInt(id));
		} else if (function.equals(ONE_STYLIST_WORK)) {
			System.out.println(ONE_STYLIST_WORK);
			result = search.oneHairstyle(Integer.parseInt(id));
		}
		Gson gson = new Gson();
		response.getWriter().append(gson.toJson(result));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
