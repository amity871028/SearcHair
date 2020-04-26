package hairMatch;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.HairMatchApi;

/**
 * Servlet implementation class HairMatchServlet
 */
public class HairMatchServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet().
	 */
	public HairMatchServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		// response.setContentType("application/json; charset=utf-8");
		response.setContentType("text/html"); // 之後要刪掉 改用上面那個
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");

		String function = request.getParameter("func");
		String type = request.getParameter("type");
		String userName = request.getParameter("userName");
		String picture = request.getParameter("picture");
		String color = request.getParameter("color");
		String result = null;

		HairMatchApi hairMatch = new HairMatchApi();
		if (function.equals("hairColor"))
			result = hairMatch.getColorHairPicutre(picture, color, userName);
		if (function.equals("hairstyle"))
			result = hairMatch.getAllHairstyles(type);

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
