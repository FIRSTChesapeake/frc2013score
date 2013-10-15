package FRC_Score_Sys;

import FRC_Score_Sys.WebServer.myWebSvr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*
 * 
 * This is a pretty useless class 
 * I thought I was going to do more with it, and didn't.
 * But it's used throughout the code and isn't hurting anything.
 * So it remains. XD
 * 
 */
public class SubSysCommHandler {

	public SqlDB SqlTalk;
	public myWebSvr WebSvr;
	final Logger logger = LoggerFactory.getLogger(SubSysCommHandler.class);

	public SubSysCommHandler(SqlDB SqlComm, myWebSvr web) {
		SqlTalk = SqlComm;
		WebSvr = web;
	}

	public void RequestAppQuit() {
		logger.info("Quit Request received by comm handler.");
		SqlTalk.close();
	}
}
