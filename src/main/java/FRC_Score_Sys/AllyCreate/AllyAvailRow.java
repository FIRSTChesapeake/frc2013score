package FRC_Score_Sys.AllyCreate;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AllyAvailRow extends JPanel {
	private static final long serialVersionUID = 1;
	
	private AllyCreateWindow myParent;
	private JLabel TeamLbl;
	private int myTeamID;
	private int myRank;
	
	public boolean CanBePicked = true;
	
	public int TeamID(){ return myTeamID; }
	
	public int Rank(){ return myRank; }
	
	public AllyAvailRow(AllyCreateWindow parent, int rank, int id){
		myParent = parent;
		myTeamID = id;
		myRank = rank;
		
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		TeamLbl = new JLabel(String.valueOf(id)+" ("+String.valueOf(rank)+")");
		setLayout(new GridLayout(0, 1, 0, 0));
		
		
		TeamLbl.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						if(CanBePicked){
							if(myParent.SetValue(myTeamID,false)) setCantPick();
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
		
		
		this.add(TeamLbl);
	}
	public void setCantPick(){
		CanBePicked = false;
		this.setBackground(Color.red);
	}
}
