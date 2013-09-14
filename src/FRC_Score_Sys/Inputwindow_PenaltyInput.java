package FRC_Score_Sys;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Inputwindow_PenaltyInput extends JPanel {

	private static final long		serialVersionUID	= 1;
	private JTextField						fouls;
	private JTextField						tfouls;
	
	private int								OrigFouls;
	private int								OrigTFouls;

	private Inputwindow_ScorePanel	myParent;

	public DocumentListener			dl					= new DocumentListener() {
															@Override
															public void changedUpdate(DocumentEvent e) {
																// Nothing?
															}

															@Override
															public void insertUpdate(DocumentEvent e) {
																myParent.ForwardRefresh();
															}

															@Override
															public void removeUpdate(DocumentEvent e) {
																myParent.ForwardRefresh();
															}
														};

	public Inputwindow_PenaltyInput(Inputwindow_ScorePanel parent, int FoulVal, int TFoulVal) {
		myParent = parent;
		setLayout(new GridLayout(0, 2, 0, 0));
		fouls = new JTextField();
		tfouls = new JTextField();
		fouls.getDocument().addDocumentListener(dl);
		tfouls.getDocument().addDocumentListener(dl);
		SetValues(FoulVal, TFoulVal);
		this.add(fouls);
		this.add(tfouls);
	}

	private void SetValues(int FoulVal, int TFoulVal){
		fouls.setText(String.valueOf(FoulVal));
		tfouls.setText(String.valueOf(TFoulVal));
		OrigFouls = FoulVal;
		OrigTFouls = TFoulVal;
	}
	
	public int GetFoulCount() {
		int f = ParseField(fouls);
		return f;
	}

	public int GetPenalties() {
		System.out.println("Penalties Input calculating!");
		int f = ParseField(fouls);
		int tf = ParseField(tfouls);
		return (f * 3) + (tf * 20);
	}

	public int GetTFoulCount() {
		int tf = ParseField(tfouls);
		return tf;
	}

	private int ParseField(JTextField field) {
		int ret = 0;
		try {
			ret = Integer.parseInt(field.getText());
			return ret;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void RequestReset() {
		SetValues(OrigFouls, OrigTFouls);
	}

}
