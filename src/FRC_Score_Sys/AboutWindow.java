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

public class AboutWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AboutWindow() {
		setTitle("About App");
		this.setSize(700,300);
		
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lbl = new JLabel("Matt's FRC 2013 Scoring Application");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);
		
		lbl = new JLabel("Version 1.2.2");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);
		
		lbl = new JLabel("---");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);
		
		lbl = new JLabel("This app is designed to keep track of the scores for the 2013 FRC Game.");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);
		
		lbl = new JLabel("Http://www.fnsnet.net");
		lbl.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lbl);
		
		JTree tree = new JTree();
		tree.setRootVisible(false);
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("Changelog") {
				{
					DefaultMutableTreeNode node_1;
				node_1 = new DefaultMutableTreeNode("V 1.2.3");
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
