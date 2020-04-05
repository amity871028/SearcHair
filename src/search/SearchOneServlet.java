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

/**
 * Servlet implementation class SearchOneServlet
 */
@WebServlet("/SearchOneServlet")
public class SearchOneServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String ALL_SALON = "salon";
	private static final String ALL_STYLIST = "stylist";
	private static final String ALL_STYLIST_WORK = "stylist_works";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchOneServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.setCharacterEncoding("UTF-8");  
		response.setContentType("application/json; charset=utf-8");
        request.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		reader.close();
		Search search = new Search();
		String result = null;
		String[] doOp = jsonAn.jsonAnaOne(json);
		System.out.println(doOp[1]);
		
		if (doOp[0].equals(ALL_SALON)) {
			System.out.println(ALL_SALON);
			result = search.oneSalon(Integer.parseInt(doOp[1]));
		} else if ((doOp[0].equals(ALL_STYLIST))) {
			System.out.println(ALL_STYLIST);
			result = search.oneStylist(Integer.parseInt(doOp[1]));
		} else if ((doOp[0].equals(ALL_STYLIST_WORK))) {
			System.out.println(ALL_STYLIST_WORK);
			result = search.oneHairstyle(Integer.parseInt(doOp[1]));
		}
		Gson gson = new Gson();
		response.getWriter().append(gson.toJson(result));
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
