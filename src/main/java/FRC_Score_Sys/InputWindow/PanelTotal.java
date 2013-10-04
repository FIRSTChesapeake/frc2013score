package FRC_Score_Sys.InputWindow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.GridLayout;

public class PanelTotal extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int FinalScore = 0;

	JLabel lblScore = new JLabel("0");
	JLabel lblPenalties = new JLabel("0");
	JLabel lblFinal = new JLabel("0");
	final Logger logger = LoggerFactory.getLogger(PanelTotal.class);

	public PanelTotal() {
		lblScore.setHorizontalAlignment(SwingConstants.CENTER);
		lblPenalties.setHorizontalAlignment(SwingConstants.CENTER);
		lblFinal.setHorizontalAlignment(SwingConstants.CENTER);

		setLayout(new GridLayout(0, 3, 0, 0));
		this.add(lblScore);
		this.add(lblPenalties);
		this.add(lblFinal);
	}

	public int GetFinal(int Score, int Penalties) {
		FinalScore = Score + Penalties;
		lblScore.setText(String.valueOf(Score));
		lblPenalties.setText(String.valueOf(Penalties));
		lblFinal.setText(String.valueOf(FinalScore));
		return FinalScore;
	}
}
