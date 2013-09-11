package FRC_Score_Sys;

import javax.swing.JPanel;
import java.awt.GridLayout;
import java.awt.Color;
import javax.swing.BorderFactory;

public class Inputwindow_ScorePanel extends JPanel {
	/**
	 * 
	 */
	private InputWindow myParent;
	private static final long serialVersionUID = 1;
	// Standard Objects
	private int TotalScore = 0;
	private int FinalScore = 0;
	
	// Input Objects
	Inputwindow_SingleScoreRow Low;
	Inputwindow_SingleScoreRow Mid;
	Inputwindow_SingleScoreRow Hig;
	Inputwindow_SingleScoreRow Pyr;
	
	InputWindow_OptRow R1;
	InputWindow_OptRow R2;
	InputWindow_OptRow R3;
	
	Inputwindow_PenaltyInput PenRow;
	
	Inputwindow_PanelTotal TotPanel;
	
	public Inputwindow_ScorePanel(InputWindow parent, Color new_color, int[] teams){
		this.myParent = parent;
		this.setLayout(new GridLayout(0, 1, 0, 0));
		this.setBackground(new_color);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		// Header 1
		String[] heads1 = {"", "Auto", "Tele", "Total"};
		Inputwindow_SectionHeader Sect1 = new Inputwindow_SectionHeader("Disk Points",heads1);
		this.add(Sect1);
		// Score Rows
		Low = new Inputwindow_SingleScoreRow(this,"Low",1,true);
		Mid = new Inputwindow_SingleScoreRow(this,"Mid",2,true);
		Hig = new Inputwindow_SingleScoreRow(this,"High",3,true);
		Pyr = new Inputwindow_SingleScoreRow(this,"Pyramid",5,false);
		
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
		Inputwindow_SectionHeader Sect2 = new Inputwindow_SectionHeader("Robot Options",heads2);
		this.add(Sect2);
		
		R1 = new InputWindow_OptRow(this,String.valueOf(teams[0]));
		R2 = new InputWindow_OptRow(this,String.valueOf(teams[1]));
		R3 = new InputWindow_OptRow(this,String.valueOf(teams[2]));
		R1.setBackground(new_color);
		R2.setBackground(new_color);
		R3.setBackground(new_color);
		
		this.add(R1);
		this.add(R2);
		this.add(R3);
		
		// Penalties Row
		// Header 3
		String[] heads3 = {"Fouls", "T-Fouls"};
		Inputwindow_SectionHeader Sect3 = new Inputwindow_SectionHeader("Penalties",heads3);
		this.add(Sect3);
		PenRow = new Inputwindow_PenaltyInput(this);
		this.add(PenRow);
		// Total Rows
		// Header 4
		String[] heads4 = {"Score","Fouls","Final"};
		Inputwindow_SectionHeader Sect4 = new Inputwindow_SectionHeader("Score Roundup",heads4);
		this.add(Sect4);
		TotPanel = new Inputwindow_PanelTotal();
		this.add(TotPanel);
		FinalScore = TotPanel.GetFinal(TotalScore,0);
	}
	public int GetPenalties(){
		return PenRow.GetPenalties();
	}
	public int GetFinalScore(){
		return FinalScore;
	}
	public void ForwardRefresh(){
		this.myParent.DoCalc();
	}
	public int DoRefresh(int Penalties){
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