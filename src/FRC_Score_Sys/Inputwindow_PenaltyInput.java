package FRC_Score_Sys;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Inputwindow_PenaltyInput extends JPanel {

	private static final long serialVersionUID = 1;
	JTextField fouls;
	JTextField tfouls;

	private Inputwindow_ScorePanel myParent;

	public DocumentListener dl = new DocumentListener() {
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

	public Inputwindow_PenaltyInput(Inputwindow_ScorePanel parent, int FoulVal,
			int TFoulVal) {
		myParent = parent;
		setLayout(new GridLayout(0, 2, 0, 0));
		fouls = new JTextField();
		tfouls = new JTextField();
		fouls.getDocument().addDocumentListener(dl);
		tfouls.getDocument().addDocumentListener(dl);
		fouls.setText(String.valueOf(FoulVal));
		tfouls.setText(String.valueOf(TFoulVal));
		this.add(fouls);
		this.add(tfouls);
	}

	public int GetPenalties() {
		System.out.println("Penalties Input calculating!");
		int f = ParseField(fouls);
		int tf = ParseField(tfouls);
		return (f * 3) + (tf * 20);
	}

	private int ParseField(JTextField field) {
		int ret = 0;
		try {
			ret = Integer.parseInt(field.getText());
			return ret;
		} catch (NumberFormatException e) {
			// String className = this.getClass().getSimpleName();
			// System.out.println("Number Format Exception.. Not a number in "+className+" Using 0.");
			return 0;
		}
	}

	public void RequestClear() {
		System.out.println("PenaltyInput received Clear Request. Done.");
		fouls.setText("");
		tfouls.setText("");
	}

}
