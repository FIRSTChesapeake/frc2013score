package FRC_Score_Sys;

import java.io.File;
import java.sql.*;


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
			if(doNew) GenerateNewDatabase();
			System.out.println("DB Subsystem up and running!");
		} catch(Exception e) {
			ExceptionHandler(e);
		}
	}
	public void close(){
		System.out.println("SQL Lite DB Closing Gracefully. Goodnight!");
		try{
			c.close();
		} catch(Exception e) {
			ExceptionHandler(e);
		}
	}
	
	public void PerformUpdateQuery(String q){
		try{
			Statement st = c.createStatement();
			st.executeUpdate(q);
			st.close();
		} catch(Exception e){
			ExceptionHandler(e);
		}
	}
	
	private void GenerateNewDatabase(){
		System.out.println("Creating new database tables..");
		String q = "CREATE TABLE MATCHES " +
				"(ID		TEXT	PRIMARY KEY	NOT NULL," +
				"RobotR1 	TEXT	NOT NULL," +
				"RobotR2	TEXT	NOT NULL," +
				"RobotR3	TEXT	NOT NULL," +
				"RobotB1	TEXT	NOT NULL," +
				"RobotB2	TEXT	NOT NULL," +
				"RobotB3	TEXT	NOT NULL," +
				"SurR1		BOOLEAN	NOT NULL," +
				"SurR2		BOOLEAN NOT NULL," +
				"SurR3		BOOLEAN NOT NULL," +
				"SurB1		BOOLEAN NOT NULL," +
				"SurB2		BOOLEAN NOT NULL," +
				"SurB3		BOOLEAN NOT NULL," +
				"DisksR		INT		NOT NULL," +
				"DisksB		INT		NOT NULL," +
				"ClimbR1	INT		NOT NULL," +
				"ClimbR2	INT		NOT NULL," +
				"ClimbR3	INT		NOT NULL," +
				"ClimbB1	INT		NOT NULL," +
				"ClimbB2	INT		NOT NULL," +
				"ClimbB3	INT		NOT NULL," +
				"DqR1		BOOLEAN	NOT NULL," +
				"DqR2		BOOLEAN	NOT NULL," +
				"DqR3		BOOLEAN	NOT NULL," +
				"DqB1		BOOLEAN	NOT NULL," +
				"DqB2		BOOLEAN	NOT NULL," +
				"DqB3		BOOLEAN	NOT NULL," +
				"FoulR		INT		NOT NULL," +
				"FoulB		INT		NOT NULL," +
				"TFoulR		INT		NOT NULL," +
				"TFOULB		INT		NOT NULL," +
				"ScoreR		INT		NOT NULL," +
				"ScoreB		INT		NOT NULL)";
		PerformUpdateQuery(q);
		System.out.println("Done!");
	}
	private void ExceptionHandler(Exception e){
		System.out.println( e.getClass().getName() + ": " + e.getMessage() );
		System.exit(0);
	}
}
