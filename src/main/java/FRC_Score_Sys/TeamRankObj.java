package FRC_Score_Sys;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import FRC_Score_Sys.WebServer.myWebSvr;



public class TeamRankObj {

	private Logger logger = LoggerFactory.getLogger(myWebSvr.class);

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
		// DEPRECIATED
		return "<tr><td>"+String.valueOf(RankNumber)+"</td><td>"+ID+"</td><td>"+WLT()+"</td></tr>";
	}

	public Element XMLVersion(String rankNumber, Document doc){
		Element ranknode = doc.createElement("RANK");

		Element a = doc.createElement("POSITION");
		a.appendChild(doc.createTextNode(rankNumber));
		ranknode.appendChild(a);

		a = doc.createElement("TEAM");
		a.appendChild(doc.createTextNode(String.valueOf(ID)));
		ranknode.appendChild(a);

		a = doc.createElement("QS");
		a.appendChild(doc.createTextNode(String.valueOf(QS)));
		ranknode.appendChild(a);

		a = doc.createElement("AP");
		a.appendChild(doc.createTextNode(String.valueOf(AP)));
		ranknode.appendChild(a);

		a = doc.createElement("CP");
		a.appendChild(doc.createTextNode(String.valueOf(CP)));
		ranknode.appendChild(a);

		a = doc.createElement("TP");
		a.appendChild(doc.createTextNode(String.valueOf(TP)));
		ranknode.appendChild(a);

		a = doc.createElement("WLT");
		a.appendChild(doc.createTextNode(String.valueOf(WLT())));
		ranknode.appendChild(a);

		// DQs are not counted at the moment.
		a = doc.createElement("DQ");
		a.appendChild(doc.createTextNode(String.valueOf("UNK")));
		ranknode.appendChild(a);

		a = doc.createElement("PLAYED");
		a.appendChild(doc.createTextNode(String.valueOf(tot)));
		ranknode.appendChild(a);

		return ranknode;
	}

}
