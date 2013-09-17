package main.java.FRC_Score_Sys;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Inputwindow_PanelTotal extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int FinalScore = 0;

	JLabel lblScore = new JLabel("0");
	JLabel lblPenalties = new JLabel("0");
	JLabel lblFinal = new JLabel("0");

	public Inputwindow_PanelTotal() {
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblPenalties.setHorizontalAlignment(SwingConstants.CENTER);
		lblFinal.setHorizontalAlignment(SwingConstants.CENTER);

		setLayout(new GridLayout(0, 3, 0, 0));
		this.add(lblScore);
		this.add(lblPenalties);
		this.add(lblFinal);
	}

	public int GetFinal(int Score, int Penalties) {
		System.out.println("PanelTotal Received GetFinal Request. Performing!");
		FinalScore = Score + Penalties;
		lblScore.setText(String.valueOf(Score));
		lblPenalties.setText(String.valueOf(Penalties));
		lblFinal.setText(String.valueOf(FinalScore));
		return FinalScore;
	}
}
