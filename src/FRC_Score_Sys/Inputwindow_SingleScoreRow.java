package FRC_Score_Sys;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Inputwindow_SingleScoreRow extends JPanel {
	/**
	 * 
	 */
	private static final long		serialVersionUID	= 1;

	String							iLabel				= "";
	int								iWorth				= 0;
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

	JTextField						Auto				= new JTextField();
	JTextField						Tele				= new JTextField();
	JLabel							Total				= new JLabel("0");

	public Inputwindow_SingleScoreRow(Inputwindow_ScorePanel parent, String lblText, int Worth, boolean hasAuto, int AutoVal, int TeleVal) {
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

		Auto.setText(String.valueOf(AutoVal));
		Tele.setText(String.valueOf(TeleVal));

		this.add(lbl);
		this.add(Auto);
		this.add(Tele);
		this.add(Total);
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

	private int ParseField(JTextField field) {
		int ret = 0;
		try {
			ret = Integer.parseInt(field.getText());
			return ret;
		} catch (NumberFormatException e) {
			return 0;
		}
	}

	public void RequestClear() {
		System.out.println("SingleScoreRow received Clear request. Done!");
		Auto.setText("");
		Tele.setText("");
		GetScore(-1);
	}
}