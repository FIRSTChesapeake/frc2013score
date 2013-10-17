package FRC_Score_Sys.AllyCreate;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AllyTopRow extends JPanel {
	private static final long serialVersionUID = 1;
	
	final Logger logger = LoggerFactory.getLogger(AllyTopRow.class);
	
	AllyCreateWindow myParent;
	
	public boolean CanBePicked = true;
	
	int ThisSeed = 0;
	int TeamCount = 0;
	int myRank = 0;
	
	int myState = 0;
	
	JLabel RankLabel;
	JTextField Team;
	JTextField Pick1;
	JTextField Pick2;
	
	public int TeamID(){ return ThisSeed; }
	public int Rank(){ return myRank; }
	
	public int GetPick(int id){
		int ret = -1;
		String val = "";
		switch(id){
			case 1:
				val = Pick1.getText();
				break;
			case 2:
				val = Pick2.getText();
				break;
		}
		if(val.equals("")) return -1;
		ret = Integer.parseInt(val);
		if(ret == 0) return -1; 
		return ret;
	}
	public AllyTopRow(int id, int capt, int Team1, int Team2){
		SetCapt(capt);
		SetValue(1, Team1);
		SetValue(2, Team2);
	}
	public AllyTopRow(AllyCreateWindow parent, int ID, int top, int inTeamCount){
		myParent = parent;
		ThisSeed = top;
		TeamCount = inTeamCount;
		myRank = ID;
		
		RankLabel = new JLabel(String.valueOf(ID));
		RankLabel.setToolTipText("Double Click Here to DQ Alliance captain.");
		
		Team =  new JTextField(String.valueOf(top));
		Pick1 = new JTextField();
		Pick2 = new JTextField();
		setLayout(new GridLayout(0, 4, 0, 0));
		Team.setEditable(false);
		Pick1.setEditable(false);
		Pick2.setEditable(false);
		
		Team.setBackground(Color.lightGray);
		Pick1.setBackground(Color.lightGray);
		Pick2.setBackground(Color.lightGray);
		
		RankLabel.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						if(CanBePicked){
							String msg = "Are you sure you want to DQ/Remove "+String.valueOf(ThisSeed)+" as Ally Captain?";
							String tit = "DQ Captain?";
							int perform = JOptionPane.showConfirmDialog(null, msg, tit, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if(perform == JOptionPane.YES_OPTION) myParent.MoveTeamsUp(myRank);
						}
					} catch (Exception err) {
						
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		
		Team.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						if(myState == 0 && CanBePicked) {
							myParent.SetValue(ThisSeed,true);
						}
					} catch (Exception err) {
						
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		
		this.add(RankLabel);
		this.add(Team);
		this.add(Pick1);
		if(TeamCount==3) this.add(Pick2);
	}
	
	public void DoSelect(int FieldID){
		Pick1.setBackground(Color.lightGray);
		Pick2.setBackground(Color.lightGray);
		myState = FieldID;
		switch(FieldID){
			case 1:
				Pick1.setBackground(Color.yellow);
				break;
			case 2:
				Pick2.setBackground(Color.yellow);
				break;
		}
	}
	public void SetValue(int FieldID, int Value){
		CanBePicked = false;
		switch(FieldID){
			case 1:
				Pick1.setText(String.valueOf(Value));
				break;
			case 2:
				Pick2.setText(String.valueOf(Value));
				break;
		}
	}
	public void SetCapt(int id){
		ThisSeed = id;
		Team.setText(String.valueOf(id));
	}
}
