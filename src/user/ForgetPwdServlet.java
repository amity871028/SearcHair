package user;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import api.UserApi;
import user.SendMail;

/**
 * Servlet implementation class ForgetPwdServlet
 */
@WebServlet("/ForgetPassword")
public class ForgetPwdServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ForgetPwdServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST");
		request.getRequestDispatcher("forget-password.html").forward(request, response);
		// read json
		BufferedReader reader = request.getReader();
		String json = reader.readLine();
		reader.close();
		System.out.println(json);
		UserApi user = new UserApi();
		String result = user.forgetPasswordJsonAnalyzing(json); // this result will be user account
		// System.out.println(result);
		try {
			if (result.equals("fail")) {
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			} else {
				SendMail.sendMail(result);
				response.setStatus(HttpServletResponse.SC_OK);
			}
		} catch (Exception desError) {
			desError.printStackTrace();
		}
	}

}
