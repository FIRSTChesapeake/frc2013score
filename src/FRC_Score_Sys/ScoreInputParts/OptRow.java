package FRC_Score_Sys.ScoreInputParts;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.event.EventListenerList;

public class OptRow extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	
	JComboBox Climb = new JComboBox();
	JCheckBox DQ = new JCheckBox();
	
	public ActionListener al = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO: Let parent know we've refreshed.
		}
	};
	
	public OptRow(String Title){
		this.setLayout(new GridLayout(0, 3, 0, 0));
		JLabel lblTitle = new JLabel(Title);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		this.add(lblTitle);
		String[] Opts = {"0", "1", "2", "3"};
		Climb = new JComboBox(Opts);
		Climb.addActionListener(al);
		this.add(Climb);
		DQ.setHorizontalAlignment(SwingConstants.CENTER);
		DQ.addActionListener(al);
		this.add(DQ);
	}
	
	public int GetClimb(){
		int a = 0;
		try{
			if(!DQ.isSelected()) {
				a = Integer.parseInt((String)Climb.getSelectedItem())*10;
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
