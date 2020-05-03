package jdbc;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.sql.rowset.serial.SerialBlob;

import api.ActivityRecord;
import api.CostRecord;
import api.PictureRecord;

public class CalendarMySQL {

	private ConnectDB database = new ConnectDB();
	private Statement stat = database.getStatement();
	private Connection con = database.getConnection();
	private ResultSet rs = null;
	private int rsInt = 0;

	private String insertCostSQL = "INSERT INTO cost VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
	private String insertActivitySQL = "INSERT INTO activity VALUES (?, ?, ?, ?, ?, ?, ?)";
	private String insertPictureSQL = "INSERT INTO picture_in_calendar VALUES (?, ?, ?, ?, ?)";
	private String insertActivityNoticeSQL = "INSERT INTO activity_notice VALUES (?, ?, ?)";
	private String selectCost = "SELECT * FROM cost";
	private String selectActivity = "SELECT * FROM activity";
	private String selectPicture = "SELECT * FROM picture_in_calendar";

	// =================== cost function ==========================//
	public boolean newCost(CostRecord costRecord) {
		int id = 0;
		try {
			// find max id to know what id will this data has
			rs = stat.executeQuery("select MAX(id) from cost");
			if (rs.next()) {
				id = rs.getInt(1) + 1;
			}
			// insert data
			PreparedStatement pstmt = con.prepareStatement(insertCostSQL);
			pstmt.setInt(1, id);
			pstmt.setString(2, costRecord.getAccount());
			pstmt.setDate(3, Date.valueOf(costRecord.getTime()));
			pstmt.setString(4, costRecord.getCategory());
			pstmt.setString(5, costRecord.getKind());
			pstmt.setInt(6, costRecord.getCost());
			pstmt.setString(7, costRecord.getDescription());
			pstmt.setString(8, costRecord.getColor());
			rsInt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public boolean deleteCost(String account, int id) {
		try {
			rs = stat.executeQuery("SELECT account FROM cost WHERE id = " + id);
			if (rs.next()) {
				String checkAccount = rs.getString("account");
				if (checkAccount.equals(account))
					rsInt = stat.executeUpdate("DELETE FROM cost WHERE id = " + id);
				else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public boolean updateCost(CostRecord costRecord) {
		try {
			rs = stat.executeQuery("SELECT account FROM activity WHERE id = " + costRecord.getId());
			if (rs.next()) {
				String checkAccount = rs.getString("account");
				if (checkAccount.equals(costRecord.getAccount())) {
					// using delete and insert instead of update each attribute
					rsInt = stat.executeUpdate("DELETE FROM cost WHERE id = " + costRecord.getId());
					PreparedStatement pstmt = con.prepareStatement(insertCostSQL);
					pstmt.setInt(1, costRecord.getId());
					pstmt.setString(2, costRecord.getAccount());
					pstmt.setDate(3, Date.valueOf(costRecord.getTime()));
					pstmt.setString(4, costRecord.getCategory());
					pstmt.setString(5, costRecord.getKind());
					pstmt.setInt(6, costRecord.getCost());
					pstmt.setString(7, costRecord.getDescription());
					pstmt.setString(8, costRecord.getColor());
					rsInt = pstmt.executeUpdate();
				} else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public String getCost() {

		ArrayList<CostRecord> allCost = new ArrayList<CostRecord>();
		try {
			rs = stat.executeQuery(selectCost);
			// int tableColumn = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				CostRecord costRecord = new CostRecord();
				costRecord.setId(rs.getInt("id"));
				costRecord.setTime(rs.getString("time"));
				costRecord.setCategory(rs.getString("category"));
				costRecord.setKind(rs.getString("kind"));
				costRecord.setCost(rs.getInt("cost"));
				costRecord.setDescription(rs.getString("description"));
				costRecord.setColor(rs.getString("color"));

				allCost.add(costRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return CostRecord.convertToJson(allCost);
	}

	// =================== activity function ==========================//

	public boolean newActivity(ActivityRecord activityRecord) {
		boolean result = false;
		int id = 0;
		try {
			// find max id to know what id will this data has
			rs = stat.executeQuery("select MAX(id) from activity");
			if (rs.next()) {
				id = rs.getInt(1) + 1;
			}
			// insert data
			PreparedStatement pstmt = con.prepareStatement(insertActivitySQL);
			pstmt.setInt(1, id);
			pstmt.setString(2, activityRecord.getAccount());
			pstmt.setString(3, activityRecord.getActivityName());
			pstmt.setTimestamp(4, Timestamp.valueOf(fixInsertTime(activityRecord.getStartTime())));
			pstmt.setTimestamp(5, Timestamp.valueOf(fixInsertTime(activityRecord.getEndTime())));
			pstmt.setString(6, activityRecord.getColor());

			// insert into the activity notice table
			if (activityRecord.getNoticeTime() > -1) {
				pstmt.setBoolean(7, true);
				rsInt = pstmt.executeUpdate();
				result = newActivityNotice(activityRecord.getAccount(), id, activityRecord.getNoticeTime());
				if (result == false)
					return false;
			} else {
				pstmt.setBoolean(7, false);
				rsInt = pstmt.executeUpdate();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public boolean deleteActivity(String account, int id) {
		try {
			rs = stat.executeQuery("SELECT account, notice FROM activity WHERE id = " + id);
			if (rs.next()) {
				String checkAccount = rs.getString("account");
				if (checkAccount.equals(account)) {
					if(rs.getBoolean("notice") == true) {
						deleteActivityNotice(account, id);
					}
					rsInt = stat.executeUpdate("DELETE FROM activity WHERE id = " + id);
				}
				else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public boolean updateActivity(ActivityRecord activityRecord) {
		boolean result = false;
		try {
			rs = stat.executeQuery("SELECT account FROM activity WHERE id = " + activityRecord.getId());
			if (rs.next()) {
				// check if this data updated by user in person
				String checkAccount = rs.getString("account");
				if (!checkAccount.equals(activityRecord.getAccount())) {
					return false;
				}
				// using delete and insert instead of update each attribute
				deleteActivityNotice(activityRecord.getAccount(), activityRecord.getId());
				rsInt = stat.executeUpdate("DELETE FROM activity WHERE id = " + activityRecord.getId());
				PreparedStatement pstmt = con.prepareStatement(insertActivitySQL);
				pstmt.setInt(1, activityRecord.getId());
				pstmt.setString(2, activityRecord.getAccount());
				pstmt.setString(3, activityRecord.getActivityName());
				pstmt.setTimestamp(4, Timestamp.valueOf(fixInsertTime(activityRecord.getStartTime())));
				pstmt.setTimestamp(5, Timestamp.valueOf(fixInsertTime(activityRecord.getEndTime())));
				pstmt.setString(6, activityRecord.getColor());
				if (activityRecord.getNoticeTime() > 0) {
					pstmt.setBoolean(7, true);
					rsInt = pstmt.executeUpdate();
					result = newActivityNotice(activityRecord.getAccount(), activityRecord.getId(),
							activityRecord.getNoticeTime());
					if (result == false)
						return false;
				} else {
					pstmt.setBoolean(7, false);
					rsInt = pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}
	
	public String fixGetTime (String time) {
		return time.replace(" ", "T").replace(".0", "");
	}
	
	public String fixInsertTime (String time) {
		String temp = time.replace("T", " ");
		return temp+=":00";
	}

	public String getActivity() {

		ArrayList<ActivityRecord> allActivity = new ArrayList<ActivityRecord>();
		try {
			rs = stat.executeQuery(selectActivity);
			while (rs.next()) {
				ActivityRecord activityRecord = new ActivityRecord();
				activityRecord.setId(rs.getInt("id"));
				activityRecord.setActivityName(rs.getString("activity_name"));
				activityRecord.setStartTime(fixGetTime(rs.getString("start_time")));
				activityRecord.setEndTime(fixGetTime(rs.getString("end_time")));
				activityRecord.setColor(rs.getString("color"));
				if(rs.getString("notice").equals("1")) {
					Statement stat1 = null;
					ResultSet rs1 = null;
					stat1 = con.createStatement();
					rs1 = stat1.executeQuery("SELECT notice_time FROM activity_notice WHERE activity_id = " + rs.getInt("id"));
					if(rs1.next()) {
						activityRecord.setNoticeTime(rs1.getInt("notice_time"));
					}
				}
				else {
					activityRecord.setNoticeTime(-1);
				}
				allActivity.add(activityRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return ActivityRecord.convertToJson(allActivity);
	}

	// =================== activity notice function ==========================//
	
	public boolean newActivityNotice(String account, int activityId, int noticeTime) {
		try {
			// insert data
			PreparedStatement pstmt = con.prepareStatement(insertActivityNoticeSQL);
			pstmt.setString(1, account);
			pstmt.setInt(2, activityId);
			pstmt.setInt(3, noticeTime);
			rsInt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}

		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public boolean deleteActivityNotice(String account, int activityId) {
		try {
			// insert data
			rsInt = stat.executeUpdate("DELETE FROM activity_notice WHERE activity_id = " + activityId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (rsInt == 1)
			return true;
		else
			return false;
	}

	// =================== picture function ==========================//

	public boolean newPicture(PictureRecord pictureRecord) {
		try {
			// find max id to know what id will this data has
			int id = 0;
			rs = stat.executeQuery("select MAX(id) from picture_in_calendar");
			if (rs.next()) {
				id = rs.getInt(1) + 1;
			}

			// insert data
			PreparedStatement pstmt = con.prepareStatement(insertPictureSQL);
			pstmt.setInt(1, id);
			pstmt.setString(2, pictureRecord.getAccount());
			pstmt.setString(3, pictureRecord.getDescription());
			pstmt.setString(4, pictureRecord.getPicture());
			pstmt.setDate(5, Date.valueOf(pictureRecord.getTime()));

			rsInt = pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public boolean deletePicture(String account, int id) {
		try {
			rs = stat.executeQuery("SELECT account FROM picture_in_calendar WHERE id = " + id);
			if (rs.next()) {
				String checkAccount = rs.getString("account");
				if (checkAccount.equals(account))
					rsInt = stat.executeUpdate("DELETE FROM picture_in_calendar WHERE id = " + id);
				else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public boolean updatePicture(PictureRecord pictureRecord) {
		try {
			rs = stat.executeQuery("SELECT account FROM picture_in_calendar WHERE id = " + pictureRecord.getId());
			if (rs.next()) {
				String checkAccount = rs.getString("account");
				if (checkAccount.equals(pictureRecord.getAccount())) {
					// using delete and insert instead of update each attribute
					rsInt = stat.executeUpdate("DELETE FROM picture_in_calendar WHERE id = " + pictureRecord.getId());
					PreparedStatement pstmt = con.prepareStatement(insertPictureSQL);
					pstmt.setInt(1, pictureRecord.getId());
					pstmt.setString(2, pictureRecord.getAccount());
					pstmt.setString(3, pictureRecord.getDescription());
					pstmt.setString(4, pictureRecord.getPicture());
					pstmt.setDate(5, Date.valueOf(pictureRecord.getTime()));
					rsInt = pstmt.executeUpdate();
				} else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		if (rsInt == 1)
			return true;
		else
			return false;
	}

	public String getPicture() {
		ArrayList<PictureRecord> allPicture = new ArrayList<PictureRecord>();
		try {
			rs = stat.executeQuery(selectPicture);

			while (rs.next()) {
				PictureRecord pictureRecord = new PictureRecord();
				pictureRecord.setId(rs.getInt("id"));
				pictureRecord.setAccount(rs.getString("account"));
				pictureRecord.setDescription(rs.getString("description"));
				pictureRecord.setPictrue(rs.getBlob("picture").getBinaryStream().toString());
				pictureRecord.setTime(rs.getString("time"));
				allPicture.add(pictureRecord);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return PictureRecord.convertToJson(allPicture);
	}

}


