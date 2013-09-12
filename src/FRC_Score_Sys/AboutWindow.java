package FRC_Score_Sys;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.SwingConstants;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.JScrollPane;
import java.net.URI;

public class AboutWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutWindow() {
		setTitle("About App");
		this.setSize(1000,300);
		
		
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
		
		try{
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
		} catch (Exception e){
			
		}
		
		JTree tree = new JTree();
		tree.setRootVisible(false);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Changelog") {
				private static final long serialVersionUID = 1L;

				{
					DefaultMutableTreeNode node_1;
				node_1 = new DefaultMutableTreeNode("V 1.3.2");
					node_1.add(new DefaultMutableTreeNode("Import Match List from MatchMaker.exe output implemented."));
				add(node_1);
				node_1 = new DefaultMutableTreeNode("V 1.3.1");
					node_1.add(new DefaultMutableTreeNode("Converted from complicated Event Handling to simply passing parent references."));
					node_1.add(new DefaultMutableTreeNode("Implemented SQL Lite DB."));
				add(node_1);
				node_1 = new DefaultMutableTreeNode("V 1.2.3a");
					node_1.add(new DefaultMutableTreeNode("Implemented the editing functions required to edit matches for the listing"));
					node_1.add(new DefaultMutableTreeNode("Extended the implementation of real-time calculations using Event Handling."));
				add(node_1);
					node_1 = new DefaultMutableTreeNode("V 1.2.2");
						node_1.add(new DefaultMutableTreeNode("Started the process required to import matches"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("V 1.2.1");
						node_1.add(new DefaultMutableTreeNode("Fixed non-calc issue from pyramid block."));
					add(node_1);
				}
			}
		));
		JScrollPane Scroller = new JScrollPane(tree,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		getContentPane().add(Scroller, BorderLayout.CENTER);
	}
}
