package user;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import api.UserApi;;

/**
 * Servlet implementation class LoginServlet
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");

		// read json send from front end
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		reader.close();

		// pass json to api
		UserApi user = new UserApi();
		String result = user.loginJsonAnalyzing(json);
		
		
		
		// make json
		JsonObject arrayObj = new JsonObject();
		arrayObj.addProperty("name", result);
		
		
		if(result != null) {
			JsonObject jobj = new Gson().fromJson(json, JsonObject.class);
			String account = jobj.get("account").toString();
			this.getServletContext().setAttribute("user", account);
			// HttpSession session =request.getSession();
		    // session.setAttribute("user", account);
		}
		

		response.getWriter().print(arrayObj);
	}

}
