package FRC_Score_Sys;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class EditOptionsWindow extends JFrame {
	private List<OptionSetPanel> Opts = new ArrayList<OptionSetPanel>();
	private static final long serialVersionUID = 1L;
	private MainMenu myParent;
	
	public EditOptionsWindow(MainMenu parent){
		setLayout(new GridLayout(0, 1, 0, 0));
		myParent = parent;
		
		JLabel Head = new JLabel("Below are the options you can change.");
		getContentPane().add(Head);
		
		Opts = myParent.CommHandle.SqlTalk.FetchPublicOptions();
		for(OptionSetPanel opt : Opts){
			getContentPane().add(opt);
		}
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(DoSave()){
					pullThePlug();
				} else {
					//TODO: HANDLE THIS
				}
			}
		});
		getContentPane().add(btnSave);
		this.pack();
	}
	private boolean DoSave(){
		// TODO: Handle Errors better
		for(OptionSetPanel opt : Opts){
			System.out.println("Updating Option '"+opt.GetLabel()+"'..");
			boolean a = myParent.CommHandle.SqlTalk.UpdateOption(opt.GetLabel(), opt.GetValue());
			if(!a) return false;
		}
		return true;
	}
	public void pullThePlug() {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		dispatchEvent(wev);
	}
}
