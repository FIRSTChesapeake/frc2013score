package FRC_Score_Sys;

import javax.swing.JOptionPane;

public class ExceptionClass {

	private String Location;

	public ExceptionClass(String loc) {
		Location = loc;
	}

	public void ExceptionHandler(String Funct, Exception e, boolean fatal, boolean alert) {
		ExceptionHandler(Funct, e, fatal, alert, "");
	}

	public void ExceptionHandler(String Funct, Exception e, boolean fatal, boolean alert, String ExtraMsg) {
		String errMsg = "in `" + Location + ":" + Funct + "`";
		String LogMsg = "";
		String msg = "";
		String tit = "";
		if (e != null) {
			errMsg = errMsg + ": " + e.getClass().getName() + ": " + e.getMessage();
		}

		if (fatal) {
			msg = "A Fatal has popped up that means we prolly can't continue.." + "\n However, not wanting to tell you what to do, we'll let you skip this error at your own risk." + "\nIf this happens again, please report it to the bug tracker." + "\n Thanks for your support and assistance";
			tit = "Fatal Error";
			LogMsg = "FATAL Exception Handled: " + errMsg;
		} else {
			msg = "A non-fatal has popped up and it's important enough to tell you about." + "\nIf this happens again, please report it to the bug tracker." + "\n Thanks for your support and assistance";
			tit = "Non-Fatal Error";
			LogMsg = "Exception Handled: " + errMsg;
		}
		System.out.println(LogMsg);
		if (ExtraMsg != "") {
			msg = msg + "\n\n " + ExtraMsg;
		}
		msg = msg + "\n\n " + errMsg;

		if (fatal) {
			msg = msg + "\n\n Do you want to ignore this fatal error?";
			int a = JOptionPane.showConfirmDialog(null, msg, tit, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
			if (a == JOptionPane.NO_OPTION) {
				System.exit(-1);
			}
		} else if (alert && !fatal) {
			JOptionPane.showMessageDialog(null, msg, tit, 0);
		}
	}

}
