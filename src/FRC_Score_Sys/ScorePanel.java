package FRC_Score_Sys;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.event.EventListenerList;

public class ScorePanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	// Std Objects
	private int TotalScore = 0;
	private int FinalScore = 0;
	
	// Input Objects
	SingleScoreRow Low;
	SingleScoreRow Mid;
	SingleScoreRow Hig;
	SingleScoreRow Pyr;
	
	OptRow R1;
	OptRow R2;
	OptRow R3;
	
	PenaltyInput PenRow;
	
	PanelTotal TotPanel;
	public ScorePanel(Color new_color, int[] teams){
		this.setLayout(new GridLayout(0, 1, 0, 0));
		this.setBackground(new_color);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// Header 1
		String[] heads1 = {"", "Auto", "Tele", "Total"};
		SectionHeader Sect1 = new SectionHeader("Disk Points",heads1);
		this.add(Sect1);
		// Score Rows
		Low = new SingleScoreRow("Low",1,true);
		Mid = new SingleScoreRow("Mid",2,true);
		Hig = new SingleScoreRow("High",3,true);
		Pyr = new SingleScoreRow("Pyramid",5,false);
		
		Low.setBackground(new_color);
		Mid.setBackground(new_color);
		Hig.setBackground(new_color);
		Pyr.setBackground(new_color);
		
		this.add(Low);
		this.add(Mid);
		this.add(Hig);
		this.add(Pyr);
		// DQ/Climb Rows
		// Header 2
		String[] heads2 = {"Robot", "Climb", "DQ'ed?"};
		SectionHeader Sect2 = new SectionHeader("Robot Options",heads2);
		this.add(Sect2);
		
		R1 = new OptRow(String.valueOf(teams[0]));
		R2 = new OptRow(String.valueOf(teams[1]));
		R3 = new OptRow(String.valueOf(teams[2]));
		R1.setBackground(new_color);
		R2.setBackground(new_color);
		R3.setBackground(new_color);
		
		this.add(R1);
		this.add(R2);
		this.add(R3);
		
		// Penalties Row
		// Header 3
		String[] heads3 = {"Fouls", "T-Fouls"};
		SectionHeader Sect3 = new SectionHeader("Penalties",heads3);
		this.add(Sect3);
		PenRow = new PenaltyInput();
		this.add(PenRow);
		// Total Rows
		// Header 4
		String[] heads4 = {"Score","Fouls","Final"};
		SectionHeader Sect4 = new SectionHeader("Score Roundup",heads4);
		this.add(Sect4);
		TotPanel = new PanelTotal();
		this.add(TotPanel);
		FinalScore = TotPanel.GetFinal(TotalScore,0);
	}
	public int GetPenalties(){
		return PenRow.GetPenalties();
	}
	public int GetFinalScore(){
		return FinalScore;
	}
	public int RequestRefresh(int Penalties){
		TotalScore = Low.GetScore(3)+Mid.GetScore(3)+Hig.GetScore(3)+Pyr.GetScore(3)+R1.GetClimb()+R2.GetClimb()+R3.GetClimb();
		FinalScore = TotPanel.GetFinal(TotalScore,Penalties);
		return FinalScore;
	}
	public void RequestClear(){
		Low.RequestClear();
		Mid.RequestClear();
		Hig.RequestClear();
		Pyr.RequestClear();
		R1.RequestClear();
		R2.RequestClear();
		R3.RequestClear();
		PenRow.RequestClear();
	}
}
