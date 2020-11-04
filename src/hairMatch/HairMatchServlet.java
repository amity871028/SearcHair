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

		String faceShape = request.getParameter("shape");
		String page = request.getParameter("page");
		
		String stylistWorkId = request.getParameter("stylistWorkId");

		String keyword = request.getParameter("keyword");

		ServletContext sc = request.getServletContext();
		String userFolderRealPath = sc.getRealPath("img/hair-match/user");
		String hairstyleFolderRealPath = sc.getRealPath("img/hair-match/hairstyle-source");

		HairMatchApi hairMatch = new HairMatchApi();
		if (function.equals("hairColor")) {
			String fileName = hairMatch.getColorHairPicutre(userFolderRealPath, hairstyleFolderRealPath, folder,
					picture, color, userName);
			String url = UrlHandler.getBaseUrl(request) + "/img/hair-match/user/" + userName + "/" + fileName;
			Hair hair = new Hair(url);
			Gson gson = new Gson();
			result = gson.toJson(hair); // convert to JSON file
		}
		if (function.equals("hairstyle")) {
			String url = UrlHandler.getBaseUrl(request) + "/img/hair-match/hairstyle-source/" + type;
			result = hairMatch.getAllHairstyles(url, hairstyleFolderRealPath, type);
		}
		if (function.equals("stylistworks")) {
			result = hairMatch.getStyleWorks(faceShape, Integer.parseInt(page));
		}
		if (function.equals("sameHairstyle")) {
			result = hairMatch.getSameHairstyle(Integer.parseInt(stylistWorkId));
		}
		if (function.equals("matchHairstyle")) {
			result = hairMatch.getMatchedHairstyle(keyword);
		}
		response.getWriter().append(result);
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

		ServletContext sc = request.getServletContext();
		String userFolderRealPath = sc.getRealPath("img/hair-match/user");
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		reader.close();

		JsonObject jobj = new Gson().fromJson(json, JsonObject.class);
		String function = jobj.get("func").getAsString();

		HairMatchApi hairMatchApi = new HairMatchApi();
		if (function.equals("store")) {
			String result = hairMatchApi.getJsonToImgur(json, userFolderRealPath);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("url", result);
			response.getWriter().print(jsonObject);
		}
		if (function.equals("share")) {
			String userName = jobj.get("userName").getAsString();
			String hairstyle = jobj.get("hairstyle").getAsString();
			String color = jobj.get("color").getAsString();
			String url = jobj.get("url").getAsString();
			boolean result = hairMatchApi.savePicture(userName, hairstyle, color, url);
			if (result == true)
				response.setStatus(HttpServletResponse.SC_OK);
			else
				response.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		if (function.equals("userPhoto")) {
			String hairstyle = jobj.get("hairstyle").getAsString();
			String result = hairMatchApi.getRandomPhoto(hairstyle);
			response.getWriter().append(result);
		}

	}
}
