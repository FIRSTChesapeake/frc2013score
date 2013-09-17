package main.java.FRC_Score_Sys;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import main.java.FRC_Score_Sys.InputWindow.InputWindow;

public class MainMenu extends JFrame {

	public SubSysCommHandler CommHandle;

	private InputWindow inputw;

	private static final long serialVersionUID = 1;

	private JTree MatchList;
	
	customTableModel RankTableModel = new customTableModel();
	JTable RankTable = new JTable(RankTableModel);
	
	public String EventName = "Unknown";

	public MainMenu(SubSysCommHandler CH) {
		CommHandle = CH;
		// TODO: Update the event name after options save?
		EventName = CH.SqlTalk.FetchOption("EVENTNAME");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.out.println("Main Window is closing. Let's tell the Comm Handler to close everything out.");
				CommHandle.RequestAppQuit();
			}
		});
		setTitle("2013 FRC Scoring Application - Event: " + EventName);
		this.setSize(1000, 500);

		JPanel menu_panel = new JPanel();
		menu_panel.setLayout(new GridLayout(0, 1, 0, 0));
		getContentPane().add(menu_panel, BorderLayout.WEST);

		// // - CREATE MENU BUTTONS
		// Reload Button
		JButton btnReloadMatches = new JButton("Reload Matches");
		btnReloadMatches.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				LoadMatchList();
				RefreshRanks();
			}
		});
		// About Button
		JButton btnAbout = new JButton("About App");
		btnAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutWindow about = new AboutWindow();
				about.setLocationRelativeTo(null);
				about.setVisible(true);
			}
		});
		// Options Button
		JButton btnOpts = new JButton("Sys Options");
		btnOpts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				EditSysOptions();
			}
		});
		// Import Button
		JButton btnImportMatches = new JButton("Import Match List");
		btnImportMatches.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int currentCount = CommHandle.SqlTalk.CountRows("MATCHES");
				int perform = -1;
				if(currentCount > 0){
					String msg = "You already have matches in the database!\nDo you want to drop them and lose ALL data?\nTHIS WILL HAPPEN RIGHT NOW AND ALL DATA WILL BE GONE!";
					String tit = "Import Matches";
					perform = JOptionPane.showConfirmDialog(null, msg, tit, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				}
				switch(perform){
					case JOptionPane.YES_OPTION:
						CommHandle.SqlTalk.ScrubDB(); 
					case -1:
						if(TriggerImportFile()==0){
							LoadMatchList();
							RefreshRanks();
						}
						break;
				}
			}
		});
		// Quit Button
		JButton btnQuit = new JButton("Quit");
		btnQuit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				pullThePlug();
			}
		});
		// /// - ADD MENU BUTTONS TO PANEL
		menu_panel.add(btnImportMatches);
		menu_panel.add(btnReloadMatches);
		menu_panel.add(btnOpts);
		menu_panel.add(btnAbout);
		menu_panel.add(btnQuit);

		MatchList = new JTree();
		MatchList.setCellRenderer(new DefaultTreeCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTreeCellRendererComponent(JTree pTree, Object pValue, boolean pIsSelected, boolean pIsExpanded, boolean pIsLeaf, int pRow, boolean pHasFocus) {
				try {
					super.getTreeCellRendererComponent(pTree, pValue, pIsSelected, pIsExpanded, pIsLeaf, pRow, pHasFocus);

					DefaultMutableTreeNode SelectedMatch = (DefaultMutableTreeNode) pValue;
					MatchListObj MLO = (MatchListObj) SelectedMatch.getUserObject();
					if (MLO.Played) {
						setBackgroundNonSelectionColor(MLO.Clr);
					} else {
						setBackgroundNonSelectionColor(MLO.color_white);
					}
				} catch (ClassCastException err) {
					// Nada
					// } catch(Exception e) {

				}
				return (this);
			}
		});
		MatchList.setToggleClickCount(1);
		MatchList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						DefaultMutableTreeNode SelectedMatch = (DefaultMutableTreeNode) MatchList.getLastSelectedPathComponent();
						MatchListObj leaf = (MatchListObj) SelectedMatch.getUserObject();
						System.out.println("Rcvd double click in match list on leaf '" + leaf.matchID + "'. Triggering edit function!");
						MainMenu.this.EditMatch(leaf.matchID);
					} catch (ClassCastException err) {
						System.out.println("Rcvd double click in match list, but caught a Cast Error. Must not have been a match ref.");
					} catch (NullPointerException err) {
						System.out.println("Rcvd double click in match list, but caught a Null Error. Was something selected?");
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		LoadMatchList();
		JScrollPane MatchScroller = new JScrollPane(MatchList, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(MatchScroller, BorderLayout.CENTER);
		
		RankTableModel.addColumn("Rank");
		RankTableModel.addColumn("Team");
		RankTableModel.addColumn("QS");
		RankTableModel.addColumn("AP");
		RankTableModel.addColumn("CP");
		RankTableModel.addColumn("TP");
		RankTableModel.addColumn("WLT");
		
		JScrollPane RankScroller = new JScrollPane(RankTable, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		getContentPane().add(RankScroller, BorderLayout.EAST);
		
		RefreshRanks();
	}

	private void EditMatch(String matchNumber) {
		if (inputw == null) {
			inputw = new InputWindow(this, matchNumber);
			inputw.pack();
			inputw.setLocationRelativeTo(null);
			inputw.setVisible(true);
		} else {
			System.out.println("Ignoring Edit Request - Edit already underway!");
		}
	}

	private void EditSysOptions() {
		EditOptionsWindow opts_wind = new EditOptionsWindow(this);
		opts_wind.setLocationRelativeTo(null);
		opts_wind.setVisible(true);
	}

	private void LoadMatchList() {
		MatchList.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Matches") {
			private static final long serialVersionUID = 1;

			{
				DefaultMutableTreeNode node;
				node = new DefaultMutableTreeNode("Qualifications");
				List<MatchListObj> QualMatches = CommHandle.SqlTalk.FetchMatchList("QQ");
				if (QualMatches.size() > 0) {
					for (MatchListObj item : QualMatches) {
						DefaultMutableTreeNode newMatch = new DefaultMutableTreeNode(item);
						node.add(newMatch);
					}
					this.add(node);
					// TODO: Open path to recently edited mode.

				}
			}
		}));
	}

	public void RefreshRanks(){
		CommHandle.SqlTalk.RefreshRanks();
		List<TeamRankObj> Teams = CommHandle.SqlTalk.FetchTeamlist(true);
		
		int rows=RankTableModel.getRowCount();
		if(rows>0){
			for(int i = rows - 1; i >=0; i--){
			   RankTableModel.removeRow(i); 
			}
		}
		int rnk =0;
		for(TeamRankObj team : Teams){
			rnk++;
			RankTableModel.addRow(new Object[]{rnk,team.ID,team.QS,team.AP,team.CP,team.TP,team.WLT()});
		}
		CommHandle.WebSvr.SetRankData(Teams);
	}
	
	public void pullThePlug() {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		dispatchEvent(wev);
	}

	// handle como from child windows.
	public void RecvChildWindowMsg(Object child, String Msg, Object Datagram) {
		if (child instanceof InputWindow) {
			switch (Msg) {
			case "im_closing_modified":
				RefreshRanks();
				LoadMatchList();
				// No break here, we're moving into the next one. :D
			case "im_closing":
				System.out.println("InputWindow said it's closing. DIE WINDOW DIE!");
				inputw = null;
				break;
			default:
				System.out.println("InputWindow said something we didn't understand? German Perhaps?");
				break;
			}
		} else {
			System.out.println("No child recognized? Hmm...");
		}
	}

	public int TriggerImportFile() {
		MatchReader rdr = new MatchReader(this);
		return rdr.DoFileLoad();
		
	}

}
