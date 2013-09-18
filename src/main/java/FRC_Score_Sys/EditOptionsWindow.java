package FRC_Score_Sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class EditOptionsWindow extends JFrame {
	private List<OptionSetPanel> Opts = new ArrayList<OptionSetPanel>();
	private static final long serialVersionUID = 1L;
	private MainMenu myParent;
	private boolean did_save = false;
	final Logger logger = LoggerFactory.getLogger(EditOptionsWindow.class);

	public EditOptionsWindow(MainMenu parent) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				String msg = "im_closing";
				if (did_save) {
					msg = "im_closing_modified";
				}
				TellParent(msg, null);
			}
		});
		setLayout(new GridLayout(0, 1, 0, 0));
		myParent = parent;
		setAlwaysOnTop(true);
		JLabel Head = new JLabel("Below are the options you can change.");
		getContentPane().add(Head);

		Opts = myParent.CommHandle.SqlTalk.FetchPublicOptions();
		for (OptionSetPanel opt : Opts) {
			getContentPane().add(opt);
		}
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (DoSave()) {
					did_save = true;
					pullThePlug();
				} else {
					// TODO: HANDLE THIS
				}
			}
		});
		getContentPane().add(btnSave);
		pack();
	}

	private boolean DoSave() {
		// TODO: Handle Errors better
		for (OptionSetPanel opt : Opts) {
			logger.info("Updating Option '" + opt.GetLabel() + "'..");
			boolean a = myParent.CommHandle.SqlTalk.UpdateOption(opt.GetLabel(), opt.GetValue());
			if (!a) {
				return false;
			}
		}
		return true;
	}

	public void pullThePlug() {
		WindowEvent wev = new WindowEvent(this, WindowEvent.WINDOW_CLOSING);
		dispatchEvent(wev);
	}
	private void TellParent(String Msg, Object Datagram) {
		myParent.RecvChildWindowMsg(this, Msg, Datagram);
	}
}
