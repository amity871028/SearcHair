package search;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.SearchApi;

/**
 * Servlet implementation class SearchServlet
 */
public class SearchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ALL_SALON = "salon";
	private static final String ALL_STYLIST = "stylist";
	private static final String ALL_STYLIST_WORK = "stylist_works";

	private static final String ALL_PRODUCT = "product";

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchServlet() {
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
		String page = request.getParameter("page");
		String keyword = request.getParameter("keyword");
		String[] service = request.getParameterValues("service");
		String[] priceArr = request.getParameterValues("price");
		String result = null;

		int[] price = null;
		if (priceArr != null) { // have price condition
			price = new int[2];
			price[0] = Integer.parseInt(priceArr[0]);
			price[1] = Integer.parseInt(priceArr[1]);
		}

		String type = request.getParameter("type");
		String feature = request.getParameter("feature");
		if (feature == null)
			feature = "-1";

		SearchApi search = new SearchApi();
		if (function.equals(ALL_SALON))
			result = search.getAllSalon(Integer.parseInt(page), keyword, service);
		else if ((function.equals(ALL_STYLIST)))
			result = search.getAllStylist(Integer.parseInt(page), keyword, service, price);
		else if ((function.equals(ALL_STYLIST_WORK)))
			result = search.getAllStylistWorks(Integer.parseInt(page), keyword);
		else if ((function.equals(ALL_PRODUCT)))
			result = search.getAllProduct(Integer.parseInt(page), keyword, type, Integer.parseInt(feature));
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
