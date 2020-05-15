package hairMatch;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

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
		response.setContentType("application/json; charset=utf-8");
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "GET");

		String function = request.getParameter("func");
		String type = request.getParameter("type");
		String userName = request.getParameter("userName");
		String folder = request.getParameter("folder");
		String picture = request.getParameter("picture");
		String color = request.getParameter("color");
		String result = null;
		
		ServletContext sc = request.getServletContext();
		String userFolderRealPath = sc.getRealPath("img/hair-match/user");
		String hairstyleFolderRealPath = sc.getRealPath("img/hair-match/hairstyle-source");

		HairMatchApi hairMatch = new HairMatchApi();
		if (function.equals("hairColor")) {
			String url = hairMatch.getColorHairPicutre(userFolderRealPath, hairstyleFolderRealPath, folder, picture, color, userName);			
			Hair hair = new Hair(url);
			Gson gson = new Gson();
			result = gson.toJson(hair); // 轉成JSON檔
		}
		if (function.equals("hairstyle")) {
			String url = UrlHandler.getBaseUrl(request) + "/img/hair-match/hairstyle-source/" + type;
			result = hairMatch.getAllHairstyles(url, hairstyleFolderRealPath, type);
		}
		response.getWriter().append(result);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		reader.close();
		
		HairMatchApi hairMatchApi = new HairMatchApi();
		String result = hairMatchApi.getJsonToImgur(json);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("url", result);
		
		response.getWriter().print(jsonObject);
	}
}
