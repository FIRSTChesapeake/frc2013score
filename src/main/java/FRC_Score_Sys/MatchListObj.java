package FRC_Score_Sys;

import java.awt.Color;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MatchListObj {

	public Color color_red = new Color(255, 106, 0);
	public Color color_blue = new Color(30, 144, 255);
	public Color color_yellow = new Color(242, 255, 0);
	public Color color_white = new Color(255, 255, 255);

	public String matchID = "";
	public boolean Played = false;
	public String Score = "";
	public Color Clr = color_yellow;
	
	// variables added for Webserver display only
	public SingleMatch R_score;
	public SingleMatch B_score;
	
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
	public Element XMLVersion(Document doc){
		Element matchnode = doc.createElement("MATCHRESULT");

		Element a = doc.createElement("MATCH");
		a.appendChild(doc.createTextNode(String.valueOf(matchID)));
		matchnode.appendChild(a);

		a = doc.createElement("RED1");
		a.appendChild(doc.createTextNode(String.valueOf(R_score.Robot1)));
		matchnode.appendChild(a);

		a = doc.createElement("RED2");
		a.appendChild(doc.createTextNode(String.valueOf(R_score.Robot2)));
		matchnode.appendChild(a);

		a = doc.createElement("RED3");
		a.appendChild(doc.createTextNode(String.valueOf(R_score.Robot3)));
		matchnode.appendChild(a);

		a = doc.createElement("BLUE1");
		a.appendChild(doc.createTextNode(String.valueOf(B_score.Robot1)));
		matchnode.appendChild(a);

		a = doc.createElement("BLUE2");
		a.appendChild(doc.createTextNode(String.valueOf(B_score.Robot2)));
		matchnode.appendChild(a);

		a = doc.createElement("BLUE3");
		a.appendChild(doc.createTextNode(String.valueOf(B_score.Robot3)));
		matchnode.appendChild(a);		
		
		a = doc.createElement("REDSCORE");
		a.appendChild(doc.createTextNode(String.valueOf(R_score.Score)));
		matchnode.appendChild(a);

		a = doc.createElement("BLUESCORE");
		a.appendChild(doc.createTextNode(String.valueOf(B_score.Score)));
		matchnode.appendChild(a);

		String strPlayed = "0";
		if(Played) strPlayed = "1";
		a = doc.createElement("PLAYED");
		a.appendChild(doc.createTextNode(strPlayed));
		matchnode.appendChild(a);
		
		return matchnode;
	}
}
