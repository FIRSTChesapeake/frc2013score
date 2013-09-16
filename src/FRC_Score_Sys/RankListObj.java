package FRC_Score_Sys;


import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JLabel;

public class RankListObj extends JPanel {
	private static final long	serialVersionUID	= 1L;
	
	JLabel Rank;
	JLabel Team;
	JLabel WTL;
	
	public RankListObj(int inRank, int inTeam, String inWTL){
		setLayout(new GridLayout(0, 3, 5, 5));
		setSize(new Dimension(400, 30));
		
		Rank = new JLabel(String.valueOf(inRank));
		Team = new JLabel(String.valueOf(inTeam));
		WTL = new JLabel(inWTL);
		
		//Rank.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//Team.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		//WTL.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		this.add(Rank);
		this.add(Team);
		this.add(WTL);
	}
	
}
