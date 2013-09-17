package main.java.FRC_Score_Sys;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class OptionSetPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JLabel lbl = new JLabel();
	private JTextField txt = new JTextField();

	private String OrigValue;

	public OptionSetPanel(String Label, String Value) {
		setLayout(new GridLayout(0, 2, 0, 0));

		setPreferredSize(new Dimension(300, 30));

		lbl.setText(Label);
		SetValue(Value);

		this.add(lbl);
		this.add(txt);
	}

	public String GetLabel() {
		return lbl.getText();
	}

	public String GetValue() {
		return txt.getText();
	}

	public void RequestReset() {
		SetValue(OrigValue);
	}

	private void SetValue(String Value) {
		txt.setText(Value);
		OrigValue = Value;
	}
}
