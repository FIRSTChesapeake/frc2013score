package FRC_Score_Sys.InputWindow;

public class ScoreTotal {
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
