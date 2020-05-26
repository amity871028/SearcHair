package jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.stream.IntStream;

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
	private String selectActivityNotice = "SELECT * FROM activity_notice";
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
		return (rsInt == 1) ? true : false;
	}

	public boolean deleteCost(String account, int id) {
		try {
			// get user account
			rs = stat.executeQuery("SELECT account FROM cost WHERE id = " + id);
			if (rs.next()) {
				String checkAccount = rs.getString("account");
				if (checkAccount.equals(account)) // Confirm if the user is himself
					rsInt = stat.executeUpdate("DELETE FROM cost WHERE id = " + id);
				else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return (rsInt == 1) ? true : false;
	}

	public boolean updateCost(CostRecord costRecord) {
		try {
			rs = stat.executeQuery("SELECT account FROM cost WHERE id = " + costRecord.getId());
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
		return (rsInt == 1) ? true : false;
	}

	public String getCost(String account, int year, int month) {
		String[] timeArray = getPeriod(year, month);
		ArrayList<CostRecord> allCost = new ArrayList<CostRecord>();
		try {

			// get all cost for specific month
			String getTime = " WHERE (time between '" + timeArray[0] + "' and '" + timeArray[1] + "')";
			String getAccount = "(account = '" + account + "')";
			rs = stat.executeQuery(selectCost + getTime + "AND " + getAccount);

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
			rs = stat.executeQuery("SELECT MAX(id) FROM activity");
			if (rs.next()) {
				id = rs.getInt(1) + 1;
			}
			// convert time type
			Timestamp startTime = time2Timestamp(activityRecord.getStartTime());
			Timestamp endTime = time2Timestamp(activityRecord.getEndTime());

			// insert data
			PreparedStatement pstmt = con.prepareStatement(insertActivitySQL);
			pstmt.setInt(1, id);
			pstmt.setString(2, activityRecord.getAccount());
			pstmt.setString(3, activityRecord.getActivityName());
			pstmt.setTimestamp(4, startTime);
			pstmt.setTimestamp(5, endTime);
			pstmt.setString(6, activityRecord.getColor());

			// insert into the activity notice table
			if (activityRecord.getNoticeTime() > -1) {
				pstmt.setBoolean(7, true);
				rsInt = pstmt.executeUpdate();
				result = newActivityNotice(activityRecord.getAccount(), id, startTime, activityRecord.getNoticeTime());
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
		return (rsInt == 1) ? true : false;
	}

	public boolean deleteActivity(String account, int id) {
		try {
			rs = stat.executeQuery("SELECT account, notice FROM activity WHERE id = " + id);
			if (rs.next()) {
				String checkAccount = rs.getString("account");
				if (checkAccount.equals(account)) {
					if (rs.getBoolean("notice") == true) {
						deleteActivityNotice(account, id);
					}
					rsInt = stat.executeUpdate("DELETE FROM activity WHERE id = " + id);
				} else
					return false;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return (rsInt == 1) ? true : false;
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

				// convert time type
				Timestamp startTime = time2Timestamp(activityRecord.getStartTime());
				Timestamp endTime = time2Timestamp(activityRecord.getEndTime());

				// update
				PreparedStatement pstmt = con.prepareStatement(insertActivitySQL);
				pstmt.setInt(1, activityRecord.getId());
				pstmt.setString(2, activityRecord.getAccount());
				pstmt.setString(3, activityRecord.getActivityName());
				pstmt.setTimestamp(4, startTime);
				pstmt.setTimestamp(5, endTime);
				pstmt.setString(6, activityRecord.getColor());

				// set activity notice
				if (activityRecord.getNoticeTime() > 0) { // if NoticeTime > 0 then add reminding
					pstmt.setBoolean(7, true);
					rsInt = pstmt.executeUpdate();
					// add notice time

					result = newActivityNotice(activityRecord.getAccount(), activityRecord.getId(), startTime,
							activityRecord.getNoticeTime());
					if (result == false)
						return false;
				} else { // if NoticeTime < 0 then delete reminding
					pstmt.setBoolean(7, false);
					rsInt = pstmt.executeUpdate();
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			database.close();
		}
		return (rsInt == 1) ? true : false;
	}

	public String getActivity(String account, int year, int month) {
		String[] timeArray = getPeriod(year, month);
		// get all activity for specific month
		ArrayList<ActivityRecord> allActivity = new ArrayList<ActivityRecord>();
		try {
			String getTime = " where (start_time between '" + timeArray[0] + "' and '" + timeArray[1] + "')";
			String getAccount = "(account = '" + account + "')";
			rs = stat.executeQuery(selectActivity + getTime + "AND " + getAccount);
			while (rs.next()) {
				ActivityRecord activityRecord = new ActivityRecord();
				activityRecord.setId(rs.getInt("id"));
				activityRecord.setActivityName(rs.getString("activity_name"));
				activityRecord.setStartTime(timestamp2Time(rs.getString("start_time")));
				activityRecord.setEndTime(timestamp2Time(rs.getString("end_time")));
				activityRecord.setColor(rs.getString("color"));
				// get activity notice time
				if (rs.getString("notice").equals("1")) {
					Statement stat1 = null;
					ResultSet rs1 = null;
					stat1 = con.createStatement();
					rs1 = stat1.executeQuery(
							"SELECT notice_time FROM activity_notice WHERE activity_id = " + rs.getInt("id"));
					if (rs1.next()) {
						// convert timestamp into minutes
						int minutes = calculateTimeDifference(rs.getTimestamp("start_time"),
								rs1.getTimestamp("notice_time"));
						activityRecord.setNoticeTime(minutes);
					}
				} else {
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

	public String getActivityNotice(String account) {
		ArrayList<ActivityRecord> allActivity = new ArrayList<ActivityRecord>();
		try {
			String getNoticeActivity = " where account = '" + account + "'" + " AND notice = 1";
			rs = stat.executeQuery(selectActivity + getNoticeActivity);
			while (rs.next()) {
				ActivityRecord activityRecord = new ActivityRecord();
				activityRecord.setId(rs.getInt("id"));
				activityRecord.setActivityName(rs.getString("activity_name"));
				activityRecord.setStartTime(timestamp2Time(rs.getString("start_time")));
				activityRecord.setEndTime(timestamp2Time(rs.getString("end_time")));

				Statement stat1 = null;
				ResultSet rs1 = null;
				stat1 = con.createStatement();
				rs1 = stat1.executeQuery(selectActivityNotice + " WHERE activity_id = " + rs.getInt("id"));
				if (rs1.next()) {
					// get activity notice time
					String time = timestamp2Time(rs1.getString("notice_time"));
					activityRecord.setNoticeTimestamp(time);
					int minutes = calculateTimeDifference(rs.getTimestamp("start_time"),
							rs1.getTimestamp("notice_time"));
					activityRecord.setNoticeTime(minutes);
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

	public boolean newActivityNotice(String account, int activityId, Timestamp startTime, int noticeTime) {
		try {
			// insert data
			PreparedStatement pstmt = con.prepareStatement(insertActivityNoticeSQL);
			pstmt.setString(1, account);
			pstmt.setInt(2, activityId);
			pstmt.setTimestamp(3, calculateNoticeTime(startTime, noticeTime));
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
			rsInt = stat.executeUpdate("UPDATE activity SET notice = 0 WHERE id = " + activityId);
			rsInt = stat.executeUpdate("DELETE FROM activity_notice WHERE activity_id = " + activityId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return (rsInt == 1) ? true : false;
	}

	// =================== picture function ==========================//

	public boolean newPicture(PictureRecord pictureRecord) {
		try {
			String timeWhereClause = " WHERE (time = '" + Date.valueOf(pictureRecord.getTime()) + "') ";
			String accountWhereClause = " (account = '" + pictureRecord.getAccount() + "')";
			rs = stat.executeQuery(selectPicture + timeWhereClause + "AND" + accountWhereClause);
			if (rs.next()) {
				/*
				 * if user added picture at that day, return false user can add just one picture
				 * on a day
				 */
				return false;
			}
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
		return (rsInt == 1) ? true : false;
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
		return (rsInt == 1) ? true : false;
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
		return (rsInt == 1) ? true : false;
	}

	public String getPicture(String account, int year, int month) {
		String[] timeArray = getPeriod(year, month);
		ArrayList<PictureRecord> allPicture = new ArrayList<PictureRecord>();
		try {
			String getTime = " where (time between '" + timeArray[0] + "' and '" + timeArray[1] + "')";
			String getAccount = "(account = '" + account + "')";
			rs = stat.executeQuery(selectPicture + getTime + "AND " + getAccount);

			while (rs.next()) {
				PictureRecord pictureRecord = new PictureRecord();
				pictureRecord.setId(rs.getInt("id"));
				pictureRecord.setDescription(rs.getString("description"));
				pictureRecord.setPictrue(rs.getString("picture"));
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

	// =================== time convert function ==========================//

	public String timestamp2Time(String time) {
		return time.replace(" ", "T").replace(".0", "");
	}

	public Timestamp time2Timestamp(String time) {
		String temp = time.replace("T", " ");
		return Timestamp.valueOf(temp);
	}

	// convert minutes to timestamp
	public Timestamp calculateNoticeTime(Timestamp startTime, int noticeTime) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		java.util.Date d = null;
		d = startTime;
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.add(Calendar.MINUTE, noticeTime * (-1));
		String newTime = df.format(cal.getTime());
		return Timestamp.valueOf(newTime);
	}

	public String[] getPeriod(int year, int month) {
		int[] monthHas31Days = { 1, 3, 5, 7, 8, 10, 12 };
		String[] timeArray = new String[2];
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();

		cal.set(year, month - 1, 1);
		timeArray[0] = df.format(cal.getTime());

		if (IntStream.of(monthHas31Days).anyMatch(x -> x == month)) {
			cal.set(year, month - 1, 31);
		} else if (month == 2 && year % 4 != 0) {
			cal.set(year, month - 1, 28);
		} else if (month == 2 && year % 4 == 0) {
			cal.set(year, month - 1, 29);
		} else {
			cal.set(year, month - 1, 30);
		}
		timeArray[1] = df.format(cal.getTime());

		return timeArray;
	}

	// convert timestamp to minutes
	public int calculateTimeDifference(Timestamp d1, Timestamp d2) {
		long diff = d1.getTime() - d2.getTime();
		long min = diff / (1000 * 60);
		return (int) min;
	}
}
