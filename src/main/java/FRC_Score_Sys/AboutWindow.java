package FRC_Score_Sys;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.net.URI;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class AboutWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutWindow() {
		setTitle("About App");
		// this.setSize(1000, 300);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 3, 0, 0));

		JLabel lbl = new JLabel("");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);

		lbl = new JLabel("Matt's FRC 2013 Scoring Application");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);

		lbl = new JLabel("");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);

		lbl = new JLabel("Created by Matt Glennon/FNS Network");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);

		lbl = new JLabel("Please report all bugs/feature requests");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);

		lbl = new JLabel("Created for VirginiaFIRST");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);

		try {
			LinkLabel lnk = new LinkLabel(new URI("https://www.fnsnet.net"), "FNSNetwork");
			lnk.setHorizontalAlignment(SwingConstants.CENTER);
			lnk.init();
			panel.add(lnk);

			lnk = new LinkLabel(new URI("https://bitbucket.org/crazysane/frc2013score/issues"), "Bug Tracker");
			lnk.setHorizontalAlignment(SwingConstants.CENTER);
			lnk.init();
			panel.add(lnk);

			lnk = new LinkLabel(new URI("http://www.virginiafirst.org"), "VirginiaFIRST");
			lnk.setHorizontalAlignment(SwingConstants.CENTER);
			lnk.init();
			panel.add(lnk);
		} catch (Exception e) {

		}
		pack();
	}
}
