package FRC_Score_Sys;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTree;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.GridLayout;
import javax.swing.JScrollPane;

public class MainMenu extends JFrame {

	InputWindow inputw;
	
	private void EditMatch(String matchNumber){
		if(inputw == null){
			inputw = new InputWindow(matchNumber);
			inputw.pack();
			inputw.setLocationRelativeTo(null);
			inputw.setVisible(true);
		} else {
			System.out.println("Ignoring Edit Request - Edit already underway!");
		}
	}
	
	private static final long serialVersionUID = 1;
	JTree MatchList;
	
	public MainMenu() {
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.out.println("Main Window is closing. That's all she wrote folks.");
				System.exit(0); 
			}
		});
		setTitle("2013 FRC Scoring Application");
		this.setSize(1000,500);
		
		JPanel menu_panel = new JPanel();
		menu_panel.setLayout(new GridLayout(0, 1, 0, 0));
		getContentPane().add(menu_panel, BorderLayout.WEST);
		
		//// - CREATE MENU BUTTONS
		// Reload Button
		JButton btnReloadMatches = new JButton("Reload Matches");
		// Test Match Screen
		JButton btnNewMatch = new JButton("Open Test Match");
		btnNewMatch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EditMatch("QQ9999");
			}
		});
		// About Button
		JButton btnAbout = new JButton("About App");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AboutWindow about = new AboutWindow();
				about.setLocationRelativeTo(null);
				about.setVisible(true);
			}
		});
		// Import Button
		JButton btnImportMatches = new JButton("Import Match List");
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		///// - ADD MENU BUTTONS TO PANEL
		menu_panel.add(btnImportMatches);
		menu_panel.add(btnNewMatch);
		menu_panel.add(btnReloadMatches);
		menu_panel.add(btnAbout);
		
		
		MatchList = new JTree();
		MatchList.setToggleClickCount(1);
		MatchList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e){
				if(e.getClickCount()==2){
					try {
						DefaultMutableTreeNode SelectedMatch = (DefaultMutableTreeNode)MatchList.getLastSelectedPathComponent();
						if(SelectedMatch.isLeaf()) {
							String leaf = String.valueOf(SelectedMatch);
							System.out.println("Rcvd double click in match list on leaf '"+leaf+"'. Triggering edit function!");
							EditMatch(leaf);
						} else {
							System.out.println("Rcvd double click in match list, but it was not a leaf. Ignored.");
						}
					} catch (NullPointerException err){
						System.out.println("Rcvd double click in match list, but caught a Null Error. Was something selected? Ignored.");
					}
				}
			}
			@Override
			public void mouseEntered(MouseEvent e) {}
			@Override
			public void mouseExited(MouseEvent e) {}
			@Override
			public void mousePressed(MouseEvent e) {}
			@Override
			public void mouseReleased(MouseEvent e) {}
		});
		MatchList.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Matches") {
				private static final long serialVersionUID = 1;

				{
					DefaultMutableTreeNode node;
					node = new DefaultMutableTreeNode("Qualifications");
						node.add(new DefaultMutableTreeNode("QQ01"));
						node.add(new DefaultMutableTreeNode("QQ02"));
						node.add(new DefaultMutableTreeNode("QQ03"));
						node.add(new DefaultMutableTreeNode("QQ04"));
					add(node);
					node = new DefaultMutableTreeNode("QuarterFinals");					
						node.add(new DefaultMutableTreeNode("QF1-1"));
						node.add(new DefaultMutableTreeNode("QF1-2"));
						node.add(new DefaultMutableTreeNode("QF1-3"));
						node.add(new DefaultMutableTreeNode("QF2-1"));
						node.add(new DefaultMutableTreeNode("QF2-2"));
						node.add(new DefaultMutableTreeNode("QF2-3"));
						node.add(new DefaultMutableTreeNode("QF3-1"));
						node.add(new DefaultMutableTreeNode("QF3-2"));
						node.add(new DefaultMutableTreeNode("QF3-3"));
						node.add(new DefaultMutableTreeNode("QF4-1"));
						node.add(new DefaultMutableTreeNode("QF4-2"));
						node.add(new DefaultMutableTreeNode("QF4-3"));
					add(node);
					node = new DefaultMutableTreeNode("SemiFinals");
						node.add(new DefaultMutableTreeNode("SF1-1"));
						node.add(new DefaultMutableTreeNode("SF1-2"));
						node.add(new DefaultMutableTreeNode("SF1-3"));
						node.add(new DefaultMutableTreeNode("SF2-1"));
						node.add(new DefaultMutableTreeNode("SF2-2"));
						node.add(new DefaultMutableTreeNode("SF2-3"));
					add(node);
					node = new DefaultMutableTreeNode("Finals");
						node.add(new DefaultMutableTreeNode("FF1"));
						node.add(new DefaultMutableTreeNode("FF2"));
						node.add(new DefaultMutableTreeNode("FF3"));
					add(node);
				}
			}
		));
		
		JScrollPane MatchScroller = new JScrollPane(MatchList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		getContentPane().add(MatchScroller, BorderLayout.CENTER);
	}

}
