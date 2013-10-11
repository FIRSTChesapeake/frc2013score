package FRC_Score_Sys.AllyCreate;

import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import FRC_Score_Sys.MainMenu;
import FRC_Score_Sys.TeamRankObj;

public class AllyCreateWindow extends JFrame {
	private static final long	serialVersionUID	= 1L;
	private MainMenu myParent;
	
	DefaultTableModel AllyTableModel = new DefaultTableModel();
	DefaultTableModel TeamTableModel = new DefaultTableModel();
	JTable AllyTable = new JTable(AllyTableModel);
	JTable TeamTable = new JTable(TeamTableModel);
	
	private int TeamCount = 0;
	
	public AllyCreateWindow(MainMenu parent){
		myParent = parent;
		this.setTitle("Create Alliances");
		String s = "Select 'Yes' for 3v3.\nSelect 'No' for 2v2.";
		String tit = "How many teams per alliances?";
		int perform = JOptionPane.showConfirmDialog(null, s, tit, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		if(perform == JOptionPane.YES_OPTION) TeamCount = 2;
		if(perform == JOptionPane.NO_OPTION) TeamCount = 1;
		List<TeamRankObj> teams = myParent.CommHandle.SqlTalk.FetchTeamlist(true, null);
		
	}
}
