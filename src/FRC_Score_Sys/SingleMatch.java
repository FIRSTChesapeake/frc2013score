package FRC_Score_Sys;

public class SingleMatch {
	private boolean iHappy = false;
	private String ID = "";
	private String alColor = "";
	public int Robot1 = 0;
	public boolean Sur1 = false;
	public int Robot2 = 0;
	public boolean Sur2 = false;
	public int Robot3 = 0;
	public boolean Sur3 = false;
	public int DisksLA = 0;
	public int DisksLT = 0;
	public int DisksMA = 0;
	public int DisksMT = 0;
	public int DisksHA = 0;
	public int DisksHT = 0;
	public int DisksP = 0;
	public int Climb1 = 0;
	public int Climb2 = 0;
	public int Climb3 = 0;
	public boolean Dq1 = false;
	public boolean Dq2 = false;
	public boolean Dq3 = false;
	public int Foul = 0;
	public int TFoul = 0;
	public int QS = 0;
	public int AP = 0;
	public int CP = 0;
	public int TP = 0;
	public int Score = 0;

	public SingleMatch() {
	}
	
	public SingleMatch(String inId, String inColor) {
		ID = inId;
		alColor = inColor.toUpperCase();
		iHappy = true;
	}

	public String aColor() {
		return alColor;
	}

	public boolean isHappy() {
		return iHappy;
	}

	public String MatchID() {
		return ID;
	}
}