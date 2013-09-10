package FRC_Score_Sys;

import java.awt.GridLayout;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.EventListenerList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PenaltyInput extends JPanel{
	
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
		public ActionListener al = new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				fireMyEvent(new MessageCap(this, "refresh","Iv'e Changed!"));
			}
		};
		public DocumentListener dl = new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
			    // Nothing?
			  }
			  public void removeUpdate(DocumentEvent e) {
				  fireMyEvent(new MessageCap(this, "refresh","Iv'e Changed!"));
			  }
			  public void insertUpdate(DocumentEvent e) {
				  fireMyEvent(new MessageCap(this, "refresh","Iv'e Changed!"));
			  }
		};
		/// END EVENT CODE
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1;
	JTextField fouls;
	JTextField tfouls;
	
	public PenaltyInput(){
		this.setLayout(new GridLayout(0, 2, 0, 0));
		fouls = new JTextField();
		tfouls = new JTextField();
		fouls.addActionListener(al);
		tfouls.addActionListener(al);
		fouls.getDocument().addDocumentListener(dl);
		tfouls.getDocument().addDocumentListener(dl);
		this.add(fouls);
		this.add(tfouls);
	}
	
	public int GetPenalties(){
		System.out.println("Penalties Input calculating!");
		int f  = ParseField(fouls);
		int tf = ParseField(tfouls);
		return (f*3)+(tf*20);
	}
	public void RequestClear(){
		System.out.println("PenaltyInput received Clear Request. Done.");
		fouls.setText("");
		tfouls.setText("");
	}
	private int ParseField(JTextField field){
		int ret = 0;
		try{
			ret = Integer.parseInt(field.getText());
			return ret;
		} catch(NumberFormatException e){
			//String className = this.getClass().getSimpleName();
			//System.out.println("Number Format Exception.. Not a number in "+className+" Using 0.");
			return 0;
		}
	}
	
}
