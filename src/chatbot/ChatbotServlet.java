package chatbot;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import api.ChatbotApi;

/**
 * Servlet implementation class ChatBotServlet
 */
@WebServlet("/ChatbotServlet")
public class ChatbotServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ChatbotServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		reader.close();
		
		// get user account
		String user = (String) this.getServletContext().getAttribute("user");
		// HttpSession session =request.getSession();
		// String user = (String) session.getAttribute("user");
		System.out.println("user: " + user);
		// get intent and input
		JsonObject jobj = new Gson().fromJson(json, JsonObject.class);
		String intent = jobj.get("intent").getAsString();
		String input = jobj.get("input").getAsString();
		
		System.out.println("intents: " + intent);
		System.out.println("inputs: " + input);
		
		String result = "[]";
		if(intent.equals("hairMatch")) {
			// get hairstyle attribute
			this.getServletContext().setAttribute("hairstyle", input);
			//HttpSession session =request.getSession();
			//session.setAttribute("hairstyle", input);
			System.out.println("input: " + input);
			request.getRequestDispatcher("pre-hair-match.jsp").forward(request, response);
		} else {
			ChatbotApi chatBot = new ChatbotApi();
			result = chatBot.jsonAnalyize(intent, input, user.replace("\"", ""));
		}

		response.getWriter().append(result);
	}
}
