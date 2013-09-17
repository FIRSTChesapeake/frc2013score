package main.java.FRC_Score_Sys.InputWindow;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SingleScoreRow extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	String iLabel = "";
	int iWorth = 0;
	private ScorePanel myParent;

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

	private JTextField Auto = new JTextField();
	private JTextField Tele = new JTextField();
	private JLabel Total = new JLabel("0");

	private int OrigAuto;
	private int OrigTele;

	public SingleScoreRow(ScorePanel parent, String lblText, int Worth, boolean hasAuto, int AutoVal, int TeleVal) {
		myParent = parent;
		iLabel = lblText;
		iWorth = Worth;
		setLayout(new GridLayout(0, 4, 0, 0));
		JLabel lbl = new JLabel(lblText);
		Auto.setEditable(hasAuto);
		if (!hasAuto) {
			Auto.setText("0");
		}

		Total.setHorizontalAlignment(SwingConstants.CENTER);

		Auto.getDocument().addDocumentListener(dl);
		Tele.getDocument().addDocumentListener(dl);

		SetValues(AutoVal, TeleVal);

		this.add(lbl);
		this.add(Auto);
		this.add(Tele);
		this.add(Total);
	}

	public int GetAutoCount() {
		int a = ParseField(Auto);
		return a;
	}
	
	public int GetScore(int type) {
		System.out.println("SingleScoreRow received score request. Calculating!");
		int ret = 0;
		int a = ParseField(Auto) * (iWorth * 2);
		int t = ParseField(Tele) * (iWorth);
		int tot = a + t;
		Total.setText(String.valueOf(tot));
		switch (type) {
		case 1:
			ret = a;
			break;
		case 2:
			ret = t;
			break;
		case 3:
			ret = tot;
			break;
		}
		return ret;
	}

	public int GetTeleCount() {
		int t = ParseField(Tele);
		return t;
	}

	private int ParseField(JTextField field) {
		int ret = 0;
		try {
			ret = Integer.parseInt(field.getText());
			field.setBackground(new Color(255,255,255));
			return ret;
		} catch (NumberFormatException e) {
			field.setBackground(new Color(255,0,0));
			return 0;
		}
	}

	public void RequestReset() {
		SetValues(OrigAuto, OrigTele);
		GetScore(-1);
	}

	private void SetValues(int AutoVal, int TeleVal) {
		Auto.setText(String.valueOf(AutoVal));
		Tele.setText(String.valueOf(TeleVal));
		OrigAuto = AutoVal;
		OrigTele = TeleVal;
	}
}