package FRC_Score_Sys;

import java.awt.Color;

public class MatchListObj {

	public Color color_red = new Color(255, 106, 0);
	public Color color_blue = new Color(30, 144, 255);
	public Color color_yellow = new Color(242, 255, 0);
	public Color color_white = new Color(255, 255, 255);

	public String matchID = "";
	public boolean Played = false;
	public String Score = "";
	public Color Clr = color_yellow;

	public MatchListObj(String id) {
		matchID = id;
	}

	@Override
	public String toString() {
		if (Played) {
			return matchID + " <Played> " + Score;
		} else {
			return matchID;
		}
	}
}
