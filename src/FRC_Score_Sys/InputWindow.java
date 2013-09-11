package FRC_Score_Sys;

import javax.swing.*;

import java.awt.GridLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowEvent;
import java.awt.event.WindowAdapter;

public class InputWindow extends JFrame {
	private static final long serialVersionUID = 1;
	
	public void pullThePlug() {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		this.dispatchEvent(wev);
	}
	Inputwindow_ScorePanel RedPanel;
	Inputwindow_ScorePanel BluePanel;
	
	String MatchNumber = "Unk";
	
	JTextField WinnerDisplay;
	
	Color color_red = new Color(255, 106, 0);
	Color color_blue = new Color(30, 144, 255);
	Color color_yellow = new Color(242, 255, 0);
	
	
	
	InputWindow(String MatchNumber){
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				//TODO: When the window is closing, tell parent.
			} 
		});
		System.out.println("Input Window for Match #"+MatchNumber+" Starting.");
		setResizable(false);
		setAlwaysOnTop(true);
		setTitle("Input Match Results - Match "+MatchNumber);
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		getContentPane().setLayout(gridBagLayout);
		
		//TODO: READ SQL AND GET TEAM#'s and Current score if any.
		int[] BlueTeams = {111, 222, 333};
		int[] RedTeams =  {444, 555, 666};
		
		// MAIN PANEL WHERE SCORES TALLY
		JPanel MainPanel = new JPanel();
		BluePanel = new Inputwindow_ScorePanel(color_blue, BlueTeams);
		MainPanel.add(BluePanel);
		RedPanel = new Inputwindow_ScorePanel(color_red, RedTeams);
		MainPanel.add(RedPanel);
		
		GridBagConstraints gbc_MainPanel = new GridBagConstraints();
		gbc_MainPanel.fill = GridBagConstraints.BOTH;
		gbc_MainPanel.insets = new Insets(0, 0, 5, 0);
		gbc_MainPanel.gridx = 0;
		gbc_MainPanel.gridy = 0;
		getContentPane().add(MainPanel, gbc_MainPanel);
		
		
		
		// BUTTONS PANEL
		JPanel BtnsPanel = new JPanel();
		BtnsPanel.setLayout(new GridLayout(0, 3, 0, 0));
		JButton SaveBtn = new JButton("Save & Close");
		SaveBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// BUTTON PRESSED
				// Redo Calc to be sure we're up to date.
				System.out.println("Pending an Input Window Close let's do one more Calculate, in case something changed.");
				DoCalc();
				// Save
				System.out.println("Requesting Save to DB...");
				//TODO: WRITE TO DATABASE!
				//TODO: Need to be sure this error checks so we know if it was actually written or not.
				System.out.println("Nothing was saved because this doesn't work yet.");
				
				// Close Window
				System.out.println("Input Window Close request pending..");
				pullThePlug();

				
			}
		});
		BtnsPanel.add(SaveBtn);
		
		WinnerDisplay = new JTextField();
		WinnerDisplay.setHorizontalAlignment(SwingConstants.CENTER);
		CheckWinner(0,0);
		BtnsPanel.add(WinnerDisplay);
		
		JButton RefBtn = new JButton("Refresh Calculations");
		RefBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// BUTTON PRESSED
				DoCalc();
			}
		});
		BtnsPanel.add(RefBtn);
		
		JButton ResetBtn = new JButton("Reset");
		ResetBtn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				// BUTTON PRESSED
				RedPanel.RequestClear();
				BluePanel.RequestClear();
				DoCalc();
			}
		});
		BtnsPanel.add(ResetBtn);
		
		
		GridBagConstraints gbc_BtnsPanel = new GridBagConstraints();
		gbc_BtnsPanel.fill = GridBagConstraints.CENTER;
		gbc_BtnsPanel.gridx = 0;
		gbc_BtnsPanel.gridy = 1;
		getContentPane().add(BtnsPanel, gbc_BtnsPanel);
	}
	private void DoCalc(){
		System.out.println("Starting Calc Routine..");
		int rPen = RedPanel.GetPenalties();
		int bPen = BluePanel.GetPenalties();
		int rFinal = RedPanel.RequestRefresh(bPen);
		int bFinal = BluePanel.RequestRefresh(rPen);
		CheckWinner(rFinal,bFinal);
		System.out.println("FULL CALC REUTINE COMPLETE.");
	}
	private void CheckWinner(int Red, int Blue){
		System.out.println("CheckWinner Function processing..");
		if(Red==Blue){
			WinnerDisplay.setText("DRAW");
			WinnerDisplay.setBackground(color_yellow);
			System.out.println("  WINNER: DRAW");
		}
		if(Red>Blue){
			WinnerDisplay.setText("RED WINS");
			WinnerDisplay.setBackground(color_red);
			System.out.println("  WINNER: RED");
		}
		if(Blue>Red){
			WinnerDisplay.setText("BLUE WINS");
			WinnerDisplay.setBackground(color_blue);
			System.out.println("  WINNER: BLUE");
		}
	}
}