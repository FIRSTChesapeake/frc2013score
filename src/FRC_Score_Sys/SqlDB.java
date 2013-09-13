package FRC_Score_Sys;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlDB {

	private Connection	c;
	private String		DBfile	= "score_data.db";

	public SqlDB() {
		System.out.println("Starting SQL Server..");
		File file = new File(DBfile);
		Boolean doNew = false;
		if (!file.exists()) {
			doNew = true;
			System.out.println("DB File not found. Will create.");
		} else {
			System.out.println("DB File found. using existing DB.");
		}
		file = null;
		try {
			System.out.println("Looking for SQLLite Driver..");
			Class.forName("org.sqlite.JDBC");
			System.out.println("Found. Connecting to Database..");
			c = DriverManager.getConnection("jdbc:sqlite:" + DBfile);
			if (doNew) {
				if (!GenerateNewDatabase()) {
					System.out.println("DB Subsystem failed to create DB tables. This is fatal.");
					System.exit(-1);
				}
			}
			System.out.println("DB Subsystem up and running!");
		} catch (Exception e) {
			ExceptionHandler("Constructor", e, true);
		}
	}

	public int AddMatchToDB(String[] matchInfo) {
		try {
			PreparedStatement s = c
					.prepareStatement("INSERT INTO MATCHES VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)");
			int i = 1;
			boolean first = false;
			System.out.println("==== AddingMatchToDB ====");
			for (String item : matchInfo) {
				System.out.print("Evaluating '" + item + "'.. ");
				if (!first) {
					System.out.println("Match ID");
					item = PadString(item);
					s.setString(i, "QQ" + item);
					first = true;
				} else {
					System.out.println("Data");
					s.setInt(i, Integer.parseInt(item));
				}
				i = i + 1;
			}
			int ret = s.executeUpdate();
			s.close();
			return ret;
		} catch (Exception e) {
			ExceptionHandler("AddMatchToDB Function", e, false);
			return 0;
		}
	}

	public void close() {
		System.out.println("SQL Lite DB Closing Gracefully. Goodnight!");
		try {
			c.close();
		} catch (Exception e) {
			ExceptionHandler("Close function", e, false);
		}
	}

	private void ExceptionHandler(String title, Exception e, boolean fatal) {
		System.out.println("SQL EXCEPTION in `" + title + "` : " + e.getClass().getName() + ": " + e.getMessage());
		if (fatal) {
			System.exit(-1);
		}
	}

	public List<SingleMatch> FetchMatch(String id) {
		System.out.println("Match Fetch Requested for id " + id);
		List<SingleMatch> ScoreList = new ArrayList<SingleMatch>();
		try {
			PreparedStatement s = c.prepareStatement("SELECT * FROM MATCHES WHERE id = ? LIMIT 1");
			s.setString(1, id);
			ResultSet rs = s.executeQuery();
			String[] clrs = { "R", "B" };
			while (rs.next()) {
				for (String clr : clrs) {
					System.out.println("Gathering " + clr + "'s");
					SingleMatch Scores = new SingleMatch(id, clr);
					Scores.Robot1 = rs.getInt(clr + "1Robot");
					Scores.Robot2 = rs.getInt(clr + "2Robot");
					Scores.Robot3 = rs.getInt(clr + "3Robot");

					Scores.Sur1 = rs.getBoolean(clr + "1Sur");
					Scores.Sur2 = rs.getBoolean(clr + "2Sur");
					Scores.Sur3 = rs.getBoolean(clr + "3Sur");

					Scores.Climb1 = rs.getInt(clr + "1Climb");
					Scores.Climb2 = rs.getInt(clr + "2Climb");
					Scores.Climb3 = rs.getInt(clr + "3Climb");

					Scores.Dq1 = rs.getBoolean(clr + "1Dq");
					Scores.Dq2 = rs.getBoolean(clr + "2Dq");
					Scores.Dq3 = rs.getBoolean(clr + "3Dq");

					Scores.DisksLA = rs.getInt(clr + "DisksLA");
					Scores.DisksLT = rs.getInt(clr + "DisksLT");
					Scores.DisksMA = rs.getInt(clr + "DisksMA");
					Scores.DisksMT = rs.getInt(clr + "DisksMT");
					Scores.DisksHA = rs.getInt(clr + "DisksHA");
					Scores.DisksHT = rs.getInt(clr + "DisksHT");
					Scores.DisksP = rs.getInt(clr + "DisksP");

					Scores.Foul = rs.getInt(clr + "Foul");
					Scores.TFoul = rs.getInt(clr + "TFoul");

					Scores.Score = rs.getInt(clr + "Score");

					ScoreList.add(Scores);
				}
			}

		} catch (Exception e) {
			ExceptionHandler("FetchMatch Function", e, false);
		}
		System.out.println("Match Fetched. Here ya go!");
		return ScoreList;
	}

	public List<MatchListObj> FetchMatchList(String type) {
		System.out.println("Match List Fetch Requested for type " + type);
		List<MatchListObj> WholeList = new ArrayList<MatchListObj>();
		try {
			PreparedStatement s = c.prepareStatement("SELECT id,Saved,RScore,BScore FROM MATCHES WHERE id LIKE ?");
			s.setString(1, type + "%");
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				MatchListObj MLO = new MatchListObj(rs.getString("ID"));
				int BScore = rs.getInt("BScore");
				int RScore = rs.getInt("RScore");
				if (rs.getBoolean("Saved")) {
					MLO.Played = true;
					if (BScore == RScore) {
						MLO.Clr = MLO.color_yellow;
						MLO.Score = BScore + " to " + RScore;
					}
					if (BScore > RScore) {
						MLO.Clr = MLO.color_blue;
						MLO.Score = BScore + " to " + RScore;
					}
					if (BScore < RScore) {
						MLO.Clr = MLO.color_red;
						MLO.Score = RScore + " to " + BScore;
					}
				}
				WholeList.add(MLO);
			}

		} catch (Exception e) {
			ExceptionHandler("Fetch Match List Function", e, false);
		}
		return WholeList;
	}

	private String FormatSQLBuild(String PrevString, String field, String Type, String Clr) {
		return FormatSQLBuild(PrevString, field, Type, Clr, "");

	}

	private String FormatSQLBuild(String PrevString, String field, String Type, String Clr, String Options) {
		String out = PrevString;
		out = out + " " + Clr + field;
		out = out + " " + Type;
		if (Options != "") {
			out = out + " " + Options;
		}
		out = out + " NOT NULL,";
		return out;
	}

	//@formatter:off
	private boolean GenerateNewDatabase() {
		System.out.println("Creating new database tables..");
		String[] clrs = {"R", "B"};
		
		String q = "CREATE TABLE MATCHES (";
		q = FormatSQLBuild(q, "ID", "TEXT", "", "PRIMARY KEY NOT NULL");
		
		for(String clr : clrs){
			q = FormatSQLBuild(q, "1Robot",		"INT", clr);
			q = FormatSQLBuild(q, "1Sur",		"BOOLEAN", clr);
			q = FormatSQLBuild(q, "2Robot",		"INT", clr);
			q = FormatSQLBuild(q, "2Sur",		"BOOLEAN", clr);
			q = FormatSQLBuild(q, "3Robot",		"INT", clr);
			q = FormatSQLBuild(q, "3Sur",		"BOOLEAN", clr);
		}
		
		for(String clr : clrs){
			q = FormatSQLBuild(q, "DisksLA",	"INT", clr);
			q = FormatSQLBuild(q, "DisksLT",	"INT", clr);
			q = FormatSQLBuild(q, "DisksMA",	"INT", clr);
			q = FormatSQLBuild(q, "DisksMT",	"INT", clr);
			q = FormatSQLBuild(q, "DisksHA",	"INT", clr);
			q = FormatSQLBuild(q, "DisksHT",	"INT", clr);
			q = FormatSQLBuild(q, "DisksP",		"INT", clr);
			q = FormatSQLBuild(q, "1Climb",		"INT", clr);
			q = FormatSQLBuild(q, "2Climb",		"INT", clr);
			q = FormatSQLBuild(q, "3Climb",		"INT", clr);
			q = FormatSQLBuild(q, "1Dq",		"BOOLEAN", clr);
			q = FormatSQLBuild(q, "2Dq",		"BOOLEAN", clr);
			q = FormatSQLBuild(q, "3Dq",		"BOOLEAN", clr);
			q = FormatSQLBuild(q, "Foul",		"INT", clr);
			q = FormatSQLBuild(q, "TFoul",		"INT", clr);
			q = FormatSQLBuild(q, "Score",		"INT", clr);
		}
		q = FormatSQLBuild(q, "Saved", "BOOLEAN", "");
		
		q = q.substring(0, q.length() - 1);
		q = q + ")";
		int cre = PerformInternalUpdateQuery(q);
		if (cre != 0) {
			System.out.println("Table Create failed!");
			return false;
		} else {
			System.out.println("Table Create successful!");
			return true;
		}
	}
	//@formatter:on
	private String PadString(String inStr) {
		if (inStr.length() >= 4) {
			return inStr;
		}
		String out = inStr;
		while (out.length() != 4) {
			out = "0" + out;
		}
		return out;
	}

	public int PerformInternalUpdateQuery(String q) {
		try {
			Statement st = c.createStatement();
			int ret = st.executeUpdate(q);
			st.close();
			return ret;
		} catch (Exception e) {
			ExceptionHandler("PerformInternalUpdt Function", e, false);
			return -1;
		}
	}
}
