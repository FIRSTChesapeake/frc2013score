package FRC_Score_Sys;

import javax.swing.JPanel;

import java.awt.GridLayout;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Inputwindow_SingleScoreRow extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	String iLabel = "";
	int iWorth = 0;
	private Inputwindow_ScorePanel myParent;
	
	public DocumentListener dl = new DocumentListener() {
		  public void changedUpdate(DocumentEvent e) {
		    // Nothing?
		  }
		  public void removeUpdate(DocumentEvent e) {
			  myParent.ForwardRefresh();
		  }
		  public void insertUpdate(DocumentEvent e) {
			  myParent.ForwardRefresh();
		  }
	};
	
	
	JTextField Auto = new JTextField();
	JTextField Tele = new JTextField();
	JLabel Total = new JLabel("0");
	
	public Inputwindow_SingleScoreRow(Inputwindow_ScorePanel parent, String lblText, int Worth, boolean hasAuto){
		this.myParent = parent;
		iLabel = lblText;
		iWorth = Worth;
		this.setLayout(new GridLayout(0, 4, 0, 0));
		JLabel lbl = new JLabel(lblText);
		Auto.setEditable(hasAuto);
		if(!hasAuto) Auto.setText("0");
		
		Total.setHorizontalAlignment(SwingConstants.CENTER);
		
		Auto.getDocument().addDocumentListener(dl);
		Tele.getDocument().addDocumentListener(dl);
		
		this.add(lbl);
		this.add(Auto);
		this.add(Tele);
		this.add(Total);
	}
	private int ParseField(JTextField field){
		int ret = 0;
		try{
			ret = Integer.parseInt(field.getText());
			return ret;
		} catch(NumberFormatException e){
			//String className = this.getClass().getSimpleName();
			//System.out.println("Number Format Exception.. Not a number in "+className+" Labeled: '"+iLabel+"' Using 0.");
			return 0;
		}
	}
	
	public int GetScore(int type){
		System.out.println("SingleScoreRow received score request. Calculating!");
		int ret = 0;
		int a = ParseField(Auto)*(iWorth*2);
		int t = ParseField(Tele)*(iWorth);
		int tot = a+t;
		Total.setText(String.valueOf(tot));
		switch(type){
			case 1:
				ret = a;
				break;
			case 2:
				ret = t;
				break;
			case 3:
				ret = tot;
				break;
		}
		return ret;
	}
	public void RequestClear(){
		System.out.println("SingleScoreRow received Clear request. Done!");
		Auto.setText("");
		Tele.setText("");
		GetScore(-1);
	}
} 