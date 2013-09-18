package FRC_Score_Sys;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

public class TeamWindow extends JFrame {
	private static final long	serialVersionUID	= 1L;
	MainMenu myParent;
	customTableModel TeamTableModel = new customTableModel();
	JTable TeamTable = new JTable(TeamTableModel);
	
	public TeamWindow(MainMenu parent, int Team){
		myParent = parent;
		setAlwaysOnTop(true);
		setTitle("Matches for team " + Team);
		
		TeamTableModel.addColumn("MatchID");
		TeamTableModel.addColumn("Spot");
		TeamTableModel.addColumn("Played");
		TeamTableModel.addColumn("Result");
		
		List<SingleMatch> matches = myParent.CommHandle.SqlTalk.FetchTeamMatches(Team);
		for(SingleMatch m : matches){
			TeamTableModel.addRow(new Object[]{m.MatchID(),FindSpot(m,Team),m.Played,Result(m)});
		}
		
		JScrollPane TeamScroller = new JScrollPane(TeamTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		
		this.getContentPane().add(TeamScroller,BorderLayout.CENTER);
		this.pack();
	}
	private String FindSpot(SingleMatch in, int id){
		if(in.Robot1 == id) return in.aColor()+"1";
		if(in.Robot2 == id) return in.aColor()+"2";
		if(in.Robot3 == id) return in.aColor()+"3";
		return "UNK?";
	}
	private String Result(SingleMatch in){
		if(!in.Played) return "";
		if(in.QS == 2) return "WIN";
		if(in.QS == 1) return "TIE";
		if(in.QS == 0) return "LOSS";
		return "NOT PLAYED";
	}
}
