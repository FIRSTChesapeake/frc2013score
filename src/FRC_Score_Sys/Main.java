package FRC_Score_Sys;

import java.io.File;
import java.io.PrintStream;

public class Main {

	static PrintStream out;
	static MainMenu MM;
	static SqlDB SqlTalk;

	public static void main(String[] args) {
		System.out
				.println("You've started Matt's 2013 FRC Scoring App Version: 1.2.3");
		System.out
				.println("Report Issues at: https://bitbucket.org/crazysane/frc2013score/issues");
		boolean verbose = false;
		for (String item : args) {
			switch (item) {
			case "-loud":
				verbose = true;
				break;
			}
		}

		if (!verbose) {
			try {
				File file = new File("logfile.txt");
				out = new PrintStream(file);
				System.out
						.println("Moving output to logfile. Add '-loud' arguement to override.");
				System.setOut(out);
			} catch (Exception e) {
				System.out
						.println("Tried to go to quiet logging but there was a problem; so you're stuck with me. :P");
				System.out.println(e.getClass().getName() + ": "
						+ e.getMessage());
			}
		}
		SqlTalk = new SqlDB();

		// ProgWindow pb = new ProgWindow();
		// pb.go();

		System.out
				.println("Creating Communications Handler to tie it all together!");
		SubSysCommHandler CH = new SubSysCommHandler(SqlTalk);
		System.out.println("Opening Main menu.");
		MM = new MainMenu(CH);
		// MM.pack();
		MM.setLocationRelativeTo(null);
		MM.setVisible(true);
	}
}