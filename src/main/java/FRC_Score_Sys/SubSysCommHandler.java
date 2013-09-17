package main.java.FRC_Score_Sys;

import main.java.FRC_Score_Sys.WebServer.myWebSvr;

public class SubSysCommHandler {

	public SqlDB SqlTalk;
	public myWebSvr WebSvr;

	public SubSysCommHandler(SqlDB SqlComm, myWebSvr Web) {
		SqlTalk = SqlComm;
		WebSvr = Web;
		
	}

	public void RequestAppQuit() {
		System.out.println("Quit Request received by comm handler.");
		SqlTalk.close();
	}
}
