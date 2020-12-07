package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SettingMySQL {

	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private int rsInt = 0;
	private ResultSet rs = null;

	public String setHairAnalysis(String account, String result) {
		try {
			String updateHairAnalysis = "UPDATE users SET hair_analysis_result = '" + result + "' WHERE account = "
					+ account;
			rsInt = stat.executeUpdate(updateHairAnalysis);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return result;
		else
			return "none";
	}
	
	public String getHairAnalysis(String account) {
		String analysisResult = null;
		try {
			String getHairAnalysis = "SELECT * from users WHERE account = '" + account + "'";
			rs = stat.executeQuery(getHairAnalysis);
			if (rs.next()) {
				analysisResult = rs.getString("hair_analysis_result");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return analysisResult;
	}

}
