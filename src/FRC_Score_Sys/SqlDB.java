package FRC_Score_Sys;

import java.io.File;
import java.sql.*;
import java.text.NumberFormat;
import java.text.ParsePosition;


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
	
	public int AddMatchToDB(String[] matchInfo){
		try{
			PreparedStatement s = c.prepareStatement("INSERT INTO MATCHES VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)");
			int i = 1;
			boolean first = false;
			for(String item : matchInfo){
				System.out.print("Evaluating '"+item+"'.. ");
				if(!first){			
					System.out.println("Match ID");
					s.setString(i, "QQ"+item);
					first = true;
				} else {
					System.out.println("Data");
					s.setInt(i, Integer.parseInt(item));
				}
				i = i + 1;
			}
			System.out.println(s.toString());
			int ret = s.executeUpdate();
			s.close();
			return ret;
		} catch (Exception e){
			ExceptionHandler(e, false);
			return -1;
		}
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
				"RobotR1 	TEXT	NOT NULL," +
				"SurR1		BOOLEAN	NOT NULL DEFAULT(0)," +
				"RobotR2	TEXT	NOT NULL," +
				"SurR2		BOOLEAN NOT NULL DEFAULT(0)," +
				"RobotR3	TEXT	NOT NULL," +
				"SurR3		BOOLEAN NOT NULL DEFAULT(0)," +
				"RobotB1	TEXT	NOT NULL," +
				"SurB1		BOOLEAN NOT NULL DEFAULT(0)," +
				"RobotB2	TEXT	NOT NULL," +
				"SurB2		BOOLEAN NOT NULL DEFAULT(0)," +
				"RobotB3	TEXT	NOT NULL," +
				"SurB3		BOOLEAN NOT NULL DEFAULT(0)," +			
				"DisksR		INT		NOT NULL DEFAULT(0)," +
				"DisksB		INT		NOT NULL DEFAULT(0)," +
				"ClimbR1	INT		NOT NULL DEFAULT(0)," +
				"ClimbR2	INT		NOT NULL DEFAULT(0)," +
				"ClimbR3	INT		NOT NULL DEFAULT(0)," +
				"ClimbB1	INT		NOT NULL DEFAULT(0)," +
				"ClimbB2	INT		NOT NULL DEFAULT(0)," +
				"ClimbB3	INT		NOT NULL DEFAULT(0)," +
				"DqR1		BOOLEAN	NOT NULL DEFAULT(0)," +
				"DqR2		BOOLEAN	NOT NULL DEFAULT(0)," +
				"DqR3		BOOLEAN	NOT NULL DEFAULT(0)," +
				"DqB1		BOOLEAN	NOT NULL DEFAULT(0)," +
				"DqB2		BOOLEAN	NOT NULL DEFAULT(0)," +
				"DqB3		BOOLEAN	NOT NULL DEFAULT(0)," +
				"FoulR		INT		NOT NULL DEFAULT(0)," +
				"FoulB		INT		NOT NULL DEFAULT(0)," +
				"TFoulR		INT		NOT NULL DEFAULT(0)," +
				"TFOULB		INT		NOT NULL DEFAULT(0)," +
				"ScoreR		INT		NOT NULL DEFAULT(0)," +
				"ScoreB		INT		NOT NULL DEFAULT(0))";
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
		System.out.println( e.getClass().getName() + ": " + e.getMessage() );
		if(fatal) System.exit(-1);
	}
}
