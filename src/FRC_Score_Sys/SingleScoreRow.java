package FRC_Score_Sys;

import javax.swing.JPanel;

import java.awt.GridLayout;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.event.EventListenerList;

public class SingleScoreRow extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;

	String iLabel = "";
	int iWorth = 0;
	/// LISTENING TO THE TEXT BOXES
	public ActionListener al = new ActionListener(){
		@Override
		public void actionPerformed(ActionEvent arg0) {
			GetScore(-2);
		}
	};
	public DocumentListener dl = new DocumentListener() {
		  public void changedUpdate(DocumentEvent e) {
		    // Nothing?
		  }
		  public void removeUpdate(DocumentEvent e) {
			  GetScore(-2);
		  }
		  public void insertUpdate(DocumentEvent e) {
			  GetScore(-2);
		  }
	};
	///// SEND THE MESSAGE HOME
	/// EVENT CODE
		protected EventListenerList listenerList = new EventListenerList();
		public void addMyEventListener(MyEventListener listener) {
			listenerList.add(MyEventListener.class, listener);
		}
		public void removeMyEventListener(MyEventListener listener) {
			listenerList.remove(MyEventListener.class, listener);
		}
		public void fireMyEvent(MessageCap evt) {
			Object[] listeners = listenerList.getListenerList();
			for (int i = 0; i < listeners.length; i = i+2) {
				if (listeners[i] == MyEventListener.class) {
					((MyEventListener) listeners[i+1]).myEventOccurred(evt);
				}
			}
		}
		
		/// END EVENT CODE
	
	
	
	JTextField Auto = new JTextField();
	JTextField Tele = new JTextField();
	JLabel Total = new JLabel("0");
	
	public SingleScoreRow(String lblText, int Worth, boolean hasAuto){
		iLabel = lblText;
		iWorth = Worth;
		this.setLayout(new GridLayout(0, 4, 0, 0));
		JLabel lbl = new JLabel(lblText);
		Auto.setEditable(hasAuto);
		if(!hasAuto) Auto.setText("0");
		
		Total.setHorizontalAlignment(SwingConstants.CENTER);
		
		Auto.addActionListener(al);
		Tele.addActionListener(al);
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
			case -2:
				fireMyEvent(new MessageCap(this,"refresh","We've been changed."));
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