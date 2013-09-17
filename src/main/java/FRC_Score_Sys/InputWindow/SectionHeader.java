package main.java.FRC_Score_Sys.InputWindow;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class SectionHeader extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	public SectionHeader(String Title, String Cols[]) {
		setLayout(new GridLayout(0, 1, 0, 0));

		JLabel lblTitle = new JLabel(Title);
		Font newLabelFont = new Font(lblTitle.getFont().getName(), Font.BOLD, lblTitle.getFont().getSize() + 2);
		lblTitle.setFont(newLabelFont);
		lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
		Color bgColor = new Color(242, 255, 0);
		lblTitle.setBackground(bgColor);
		setBackground(bgColor);
		this.add(lblTitle);

		JPanel allColls = new JPanel();
		allColls.setLayout(new GridLayout(0, Cols.length, 0, 0));
		for (String s : Cols) {
			JLabel a = new JLabel(s);
			a.setHorizontalAlignment(SwingConstants.CENTER);
			allColls.add(a);
		}
		this.add(allColls);
	}
}
