package main.java.FRC_Score_Sys;

public class TeamRankObj {
	public int ID;
	public int QS;
	public int AP;
	public int CP;
	public int TP;
	
	public int wins = 0;
	public int ties = 0;
	public int tot  = 0;
	
	public String WLT(){
		int loss = tot-(wins+ties);
		String ret = String.valueOf(wins)+"/"+String.valueOf(loss)+"/"+String.valueOf(ties);
		return ret;
	}
	
	public TeamRankObj(){
		
	}
	public String RankHTMLTableRow(int RankNumber){
		return "<tr><td>"+String.valueOf(RankNumber)+"</td><td>"+ID+"</td><td>"+WLT()+"</td></tr>";
	}
}
