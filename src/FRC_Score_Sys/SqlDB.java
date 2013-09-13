package FRC_Score_Sys;

import java.io.File;
import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class SqlDB {
	
	private Connection c;
	private String DBfile = "score_data.db";
	
	public SqlDB(){
		System.out.println("Starting SQL Server..");
		File file = new File(DBfile);
		Boolean doNew = false;
		if(!file.exists()) {
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
			c = DriverManager.getConnection("jdbc:sqlite:"+DBfile);
			if(doNew) if(!GenerateNewDatabase()){
				System.out.println("DB Subsystem failed to create DB tables. This is fatal.");
				System.exit(-1);
			}
			System.out.println("DB Subsystem up and running!");
		} catch(Exception e) {
			ExceptionHandler(e,true);
		}
	}
	public void close(){
		System.out.println("SQL Lite DB Closing Gracefully. Goodnight!");
		try{
			c.close();
		} catch(Exception e) {
			ExceptionHandler(e,false);
		}
	}
	
	public List<String> FetchMatchList(String type){
		System.out.println("Match List Fetch Requested for type "+type);
		List<String> WholeList = new ArrayList<String>();
		try{
			PreparedStatement s = c.prepareStatement("SELECT id FROM MATCHES WHERE id LIKE ?");
			s.setString(1, type+"%");
			ResultSet rs = s.executeQuery();
			while(rs.next()){
				WholeList.add(rs.getString("id"));
			}
			
		} catch (Exception e){
			ExceptionHandler(e, false);
		}
		return WholeList;
	}
	
	public List<SingleMatch> FetchMatch(String id){
		System.out.println("Match Fetch Requested for id "+id);
		List<SingleMatch> ScoreList = new ArrayList<SingleMatch>();
		try{
			PreparedStatement s = c.prepareStatement("SELECT * FROM MATCHES WHERE id = ? LIMIT 1");
			s.setString(1, id);
			ResultSet rs = s.executeQuery();
			String[] clrs = {"R", "B"};
			while(rs.next()){
				for(String clr : clrs){
					System.out.println("Gathering "+clr+"'s");
					SingleMatch Scores = new SingleMatch(id, clr);
					Scores.Robot1 = rs.getInt(clr+"1Robot");
					Scores.Robot2 = rs.getInt(clr+"2Robot");
					Scores.Robot3 = rs.getInt(clr+"3Robot");

					Scores.Sur1 = rs.getBoolean(clr+"1Sur");
					Scores.Sur2 = rs.getBoolean(clr+"2Sur");
					Scores.Sur3 = rs.getBoolean(clr+"3Sur");

					Scores.Climb1 = rs.getInt(clr+"1Climb");
					Scores.Climb2 = rs.getInt(clr+"2Climb");
					Scores.Climb3 = rs.getInt(clr+"3Climb");

					Scores.Dq1 = rs.getBoolean(clr+"1Dq");
					Scores.Dq2 = rs.getBoolean(clr+"2Dq");
					Scores.Dq3 = rs.getBoolean(clr+"3Dq");

					Scores.Disks = rs.getInt(clr+"Disks");

					Scores.Foul = rs.getInt(clr+"Foul");
					Scores.TFoul = rs.getInt(clr+"TFoul");

					Scores.Score = rs.getInt(clr+"Score");
					ScoreList.add(Scores);
				}
			}
			
		} catch (Exception e){
			ExceptionHandler(e, false);
		}
		return ScoreList;
	}
	
	public int AddMatchToDB(String[] matchInfo){
		try{
			PreparedStatement s = c.prepareStatement("INSERT INTO MATCHES VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)");
			int i = 1;
			boolean first = false;
			for(String item : matchInfo){
				System.out.print("Evaluating '"+item+"'.. ");
				if(!first){			
					System.out.println("Match ID");
					item = PadString(item);
					s.setString(i, "QQ"+item);
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
		} catch (Exception e){
			ExceptionHandler(e, false);
			return -1;
		}
	}
	
	private String PadString(String inStr){
		if(inStr.length() >= 4) return inStr;
		String out = inStr;
		while(out.length() != 4) out = "0"+out;
		return out;
	}
	
	@SuppressWarnings("unused")
	private int CountArray(Object[] Arr){
		int i = 0;
		for(Object item : Arr){
			i = i +1;
		}
		return i;
	}
	
	public int PerformInternalUpdateQuery(String q){
		try{
			Statement st = c.createStatement();
			int ret = st.executeUpdate(q);
			st.close();
			return ret;
		} catch(Exception e){
			ExceptionHandler(e,false);
			return -1;
		}
	}
	
	private boolean GenerateNewDatabase(){
		System.out.println("Creating new database tables..");
		String q = "CREATE TABLE MATCHES " +
				"(ID		TEXT	PRIMARY KEY	NOT NULL," +
				"R1Robot 	INT		NOT NULL," +
				"R1Sur		BOOLEAN	NOT NULL DEFAULT(0)," +
				"R2Robot	INT		NOT NULL," +
				"R2Sur		BOOLEAN NOT NULL DEFAULT(0)," +
				"R3Robot	INT		NOT NULL," +
				"R3Sur		BOOLEAN NOT NULL DEFAULT(0)," +
				"B1Robot	INT		NOT NULL," +
				"B1Sur		BOOLEAN NOT NULL DEFAULT(0)," +
				"B2Robot	INT		NOT NULL," +
				"B2Sur		BOOLEAN NOT NULL DEFAULT(0)," +
				"B3Robot	INT		NOT NULL," +
				"B3Sur		BOOLEAN NOT NULL DEFAULT(0)," +			
				"RDisks		INT		NOT NULL DEFAULT(0)," +
				"BDisks		INT		NOT NULL DEFAULT(0)," +
				"R1Climb	INT		NOT NULL DEFAULT(0)," +
				"R2Climb	INT		NOT NULL DEFAULT(0)," +
				"R3Climb	INT		NOT NULL DEFAULT(0)," +
				"B1Climb	INT		NOT NULL DEFAULT(0)," +
				"B2Climb	INT		NOT NULL DEFAULT(0)," +
				"B3Climb	INT		NOT NULL DEFAULT(0)," +
				"R1Dq		BOOLEAN	NOT NULL DEFAULT(0)," +
				"R2Dq		BOOLEAN	NOT NULL DEFAULT(0)," +
				"R3Dq		BOOLEAN	NOT NULL DEFAULT(0)," +
				"B1Dq		BOOLEAN	NOT NULL DEFAULT(0)," +
				"B2Dq		BOOLEAN	NOT NULL DEFAULT(0)," +
				"B3Dq		BOOLEAN	NOT NULL DEFAULT(0)," +
				"RFoul		INT		NOT NULL DEFAULT(0)," +
				"BFoul		INT		NOT NULL DEFAULT(0)," +
				"RTFoul		INT		NOT NULL DEFAULT(0)," +
				"BTFoul		INT		NOT NULL DEFAULT(0)," +
				"RScore		INT		NOT NULL DEFAULT(0)," +
				"BScore		INT		NOT NULL DEFAULT(0))";
		int cre = PerformInternalUpdateQuery(q);
		if(cre != 0){
			System.out.println("Table Create failed!");
			return false;
		} else {
			System.out.println("Table Create successful!");
			return true;
		}
	}
	private void ExceptionHandler(Exception e, boolean fatal){
		System.out.println("SQL EXCEPTION: " + e.getClass().getName() + ": " + e.getMessage() );
		if(fatal) System.exit(-1);
	}
}
