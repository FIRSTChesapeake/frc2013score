package FRC_Score_Sys;

import javax.swing.JOptionPane;

public class PopupGenerator {

	public int Exception(String title, Exception e, String Message, boolean fatal){
		String f = "Non-Fatal";
		if(fatal) f = "FATAL";
		String s = "Sorry to bother you, but a "+f+" error inside function '"+title+"' has popped up that we needed to tell you about.";
		s = s + "\nHere is some of what we know, but check the logs if you'd like more info:";
		s = s + "\n\n"+Message;
		s = s + "\nClass  : "+e.getClass();
		s = s + "\nMessage: "+e.getMessage();
		String tit = f+" Error in "+title;
		int perform = JOptionPane.YES_OPTION;
		if(fatal){
			s = s + "\n\nShould we continue and ignore this error?";
			perform = JOptionPane.showConfirmDialog(null, s, tit, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, s);
		}
		return perform;
	}
	public void StandardMessage(String title, String msg){
		
	}
}
