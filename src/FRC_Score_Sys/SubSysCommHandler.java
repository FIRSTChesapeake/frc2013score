package FRC_Score_Sys;

public class SubSysCommHandler {
	
	public SqlDB SqlTalk;
	
	public SubSysCommHandler (SqlDB SqlComm) {
		SqlTalk = SqlComm;
	}
	public void RequestAppQuit(){
		System.out.println("Quit Request received by comm handler.");
		SqlTalk.close();
	}
}
