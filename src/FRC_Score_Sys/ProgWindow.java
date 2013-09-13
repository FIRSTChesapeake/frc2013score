package FRC_Score_Sys;

import java.awt.BorderLayout;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class ProgWindow extends JFrame {
	private static final long	serialVersionUID	= 1L;
	JProgressBar				pb					= new JProgressBar();

	public ProgWindow() {
		setAlwaysOnTop(true);
		setResizable(false);
		setTitle("Please Wait..");
		getContentPane().add(pb, BorderLayout.CENTER);
		pb.setIndeterminate(true);

		JLabel lblPleaseWait = new JLabel("Please wait..");
		getContentPane().add(lblPleaseWait, BorderLayout.NORTH);
		this.setSize(300, 100);
		setLocationRelativeTo(null);
	}

	public void go() {
		setVisible(true);
		this.repaint();
	}

	public void pullThePlug() {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		dispatchEvent(wev);
	}
}
