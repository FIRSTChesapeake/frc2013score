package FRC_Score_Sys.ScoreInputParts;

import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PenaltyInput extends JPanel{
		public DocumentListener dl = new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    // Nothing?
			  }
			  public void removeUpdate(DocumentEvent e) {
				  //TODO: Alert parent we've changed
			  }
			  public void insertUpdate(DocumentEvent e) {
				  //TODO: Alert Parent we've changed
			  }
		};
		/// END EVENT CODE
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	JTextField fouls;
	JTextField tfouls;
	
	public PenaltyInput(){
		this.setLayout(new GridLayout(0, 2, 0, 0));
		fouls = new JTextField();
		tfouls = new JTextField();
		fouls.getDocument().addDocumentListener(dl);
		tfouls.getDocument().addDocumentListener(dl);
		this.add(fouls);
		this.add(tfouls);
	}
	
	public int GetPenalties(){
		System.out.println("Penalties Input calculating!");
		int f  = ParseField(fouls);
		int tf = ParseField(tfouls);
		return (f*3)+(tf*20);
	}
	public void RequestClear(){
		System.out.println("PenaltyInput received Clear Request. Done.");
		fouls.setText("");
		tfouls.setText("");
	}
	private int ParseField(JTextField field){
		int ret = 0;
		try{
			ret = Integer.parseInt(field.getText());
			return ret;
		} catch(NumberFormatException e){
			//String className = this.getClass().getSimpleName();
			//System.out.println("Number Format Exception.. Not a number in "+className+" Using 0.");
			return 0;
		}
	}
	
}
