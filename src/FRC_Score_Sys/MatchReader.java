package FRC_Score_Sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;

public class MatchReader {

	class DataLoader implements Runnable {
		public boolean DoneFlag = false;
		public int max = -1;
		public int tot = 0;
		private File f;

		public DataLoader(File file) {
			f = file;
		}

		@Override
		public void run() {
			try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				int RetAdd = 0;
				List<String> WholeFile = new ArrayList<String>();
				System.out.println("Reading File..");
				while ((line = br.readLine()) != null) {
					if (line.length() > 0) {
						WholeFile.add(line);
					}
				}
				br.close();
				System.out.println("File Closed.");
				for (String match : WholeFile) {
					String[] spl = match.split(" ");
					int ret = myParent.CommHandle.SqlTalk.AddMatchToDB(spl);
					System.out.println("= Result: " + ret);
					RetAdd = RetAdd + ret;
					tot = tot + 1;
				}
				DoneFlag = true;
				int leftover = tot - RetAdd;
				if (leftover == 1) {
					System.out.println("All but 1 line was imported (" + RetAdd + " matches). This is normal if you didn't edit the file.");
					System.out.println("(Usually a blank line at the bottom.)");
				} else if (leftover == 0) {
					System.out.println("All line were imported.");
				} else {
					System.out.println("Imported " + RetAdd + " lines out of " + tot + ".");
					System.out.println("(Remember: There is usually a blank line at the bottom.)");
				}
			} catch (Exception e) {
				ExceptionHandler(e, false);
				DoneFlag = true;
			}
		}
	}

	private MainMenu myParent;

	public MatchReader(MainMenu parent) {
		myParent = parent;
		System.out.println("FileReader Created");
	}

	public int DoFileLoad() {
		ProgWindow pb = new ProgWindow();
		JFileChooser fc = new JFileChooser();
		System.out.println("Asking user to find file..");
		int ret = fc.showOpenDialog(myParent);
		if (ret == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fc.getSelectedFile();
				System.out.println("User choose: " + file.toString());
				DataLoader DL = new DataLoader(file);
				pb.go();
				DL.run();
				while (!DL.DoneFlag) {
					pb.repaint();
					Thread.sleep(50);
				}
				pb.pullThePlug();
				return 0;
			} catch (Exception e) {
				ExceptionHandler(e, false);

				return -2;
			}
		} else {
			System.out.println("User canceled file open request");
			return -1;
		}
	}

	private void ExceptionHandler(Exception e, boolean fatal) {
		System.out.println(e.getClass().getName() + ": " + e.getMessage());
		if (fatal) {
			System.exit(-1);
		}
	}
}
