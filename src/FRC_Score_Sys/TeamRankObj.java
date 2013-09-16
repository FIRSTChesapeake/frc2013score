package FRC_Score_Sys;

public class TeamRankObj {
	public int ID;
	public int QS;
	public int AP;
	public int CP;
	public int TP;
	
	public int wins = 0;
	public int ties = 0;
	public int tot  = 0;
	
	public String WTL(){
		int loss = tot-(wins+ties);
		String ret = String.valueOf(wins)+"/"+String.valueOf(ties)+"/"+String.valueOf(loss);
		return ret;
	}
	
	public TeamRankObj(){
		
	}
	public RankListObj GetObject(int Rank){
		RankListObj ret = new RankListObj(Rank,ID,WTL());
		return ret;
	}
}
