package main.java.FRC_Score_Sys;

public class Inputwindow_ScoreTotal {
	public int RawAuto = 0;
	public int RawTele = 0;
	public int ClimbPoints = 0;
	public int RawFouls = 0;
	private int iTotal = 0;

	public void DoTotal() {
		iTotal = RawAuto + RawTele + ClimbPoints + RawFouls;
	}

	public int Total() {
		return iTotal;
	}
}
