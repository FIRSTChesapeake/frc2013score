package FRC_Score_Sys;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class InputWindow_OptRow extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	private Inputwindow_ScorePanel myParent;
	
	JComboBox<Integer> Climb;
	JCheckBox DQ;
	
	public ActionListener al = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			myParent.ForwardRefresh();
		}
	};
	
	public InputWindow_OptRow(Inputwindow_ScorePanel parent, String Title){
		this.myParent = parent;
		this.setLayout(new GridLayout(0, 3, 0, 0));
		JLabel lblTitle = new JLabel(Title);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTitle);
		Climb = new JComboBox<Integer>();
		Climb.addItem(0);
		Climb.addItem(1);
		Climb.addItem(2);
		Climb.addItem(3);
		Climb.addActionListener(al);
		this.add(Climb);
		DQ = new JCheckBox();
		DQ.setHorizontalAlignment(SwingConstants.CENTER);
		DQ.addActionListener(al);
		this.add(DQ);
	}
	
	public int GetClimb(){
		int a = 0;
		try{
			if(!DQ.isSelected()) {
				a = (Integer)Climb.getSelectedItem();
				a = a*10;
				System.out.println("OptRow Found No DQ, Climb of "+Climb.getSelectedItem());
			} else {
				System.out.println("OptRow Found Robot DQ. Using Zero.");
			}
		} catch(NumberFormatException e){
			System.out.println("OptRow failed to parse Climb Integer! Using Zero.");
			a = 0;
		}
		return a;
	}
	
	public boolean isDQ(){
		return DQ.isSelected();
	}
	
	public void actionPerformed(ActionEvent e) {
		
	}
	public void RequestClear(){
		System.out.println("OptRow Received Clear Request. Performed.");
		DQ.setSelected(false);
		Climb.setSelectedIndex(0);
	}
}
