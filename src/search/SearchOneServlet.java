package search;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.SearchApi;

/**
 * Servlet implementation class SearchOneServlet
 */
public class SearchOneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ONE_SALON = "salon";
	private static final String ONE_STYLIST = "stylist";
	private static final String ONE_STYLIST_WORK = "stylist_works";

	private static final String ONE_PRODUCT = "product";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchOneServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");

		String function = request.getParameter("func");
		String id = request.getParameter("id");
		String result = null;

		SearchApi search = new SearchApi();
		if (function.equals(ONE_SALON))
			result = search.getOneSalon(Integer.parseInt(id));
		else if (function.equals(ONE_STYLIST))
			result = search.getOneStylist(Integer.parseInt(id));
		else if (function.equals(ONE_STYLIST_WORK))
			result = search.getOneStylistWorks(Integer.parseInt(id));
		else if (function.equals(ONE_PRODUCT))
			result = search.getOneProduct(Integer.parseInt(id));

		response.getWriter().append(result);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
