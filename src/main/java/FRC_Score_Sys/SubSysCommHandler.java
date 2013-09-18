package FRC_Score_Sys;

import FRC_Score_Sys.WebServer.myWebSvr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubSysCommHandler {

	public SqlDB SqlTalk;
	public myWebSvr WebSvr;
	final Logger logger = LoggerFactory.getLogger(SubSysCommHandler.class);

	public SubSysCommHandler(SqlDB SqlComm, myWebSvr Web) {
		SqlTalk = SqlComm;
		WebSvr = Web;
		
	}

	public void RequestAppQuit() {
		logger.info("Quit Request received by comm handler.");
		SqlTalk.close();
	}
}
