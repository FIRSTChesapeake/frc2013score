package FRC_Score_Sys;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MatchReader {

	private ExceptionClass Except = new ExceptionClass("FileReader");
	
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
					if(ret == -1){
						String msg = "We encountered an error reading that match file into the DB, so to prevent a million error messages, I'll abort if you like?";
						int answer = JOptionPane.showConfirmDialog(null, msg, "Import Matches", JOptionPane.YES_NO_OPTION);
						if(answer == JOptionPane.YES_OPTION) break;
					}
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
				Except.ExceptionHandler("Run", e, false, true,"Woah. Major error while reading the file. Is this really output from MatchMaker?");
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
		//ProgWindow pb = new ProgWindow();
		JFileChooser fc = new JFileChooser();
		System.out.println("Asking user to find file..");
		int ret = fc.showOpenDialog(myParent);
		if (ret == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fc.getSelectedFile();
				System.out.println("User choose: " + file.toString());
				if(file.exists()){
					DataLoader DL = new DataLoader(file);
					//pb.go();
					DL.run();
					while (!DL.DoneFlag) {
						//pb.repaint();
						Thread.sleep(50);
					}
					//pb.pullThePlug();
					return 0;
				} else {
					String msg = "I don't mean to call you a liar or anything - but I really can't find that file. Sorry!";
					JOptionPane.showMessageDialog(null, msg);
					return -2;
				}
			} catch (Exception e) {
				//pb.pullThePlug();
				Except.ExceptionHandler("FileLoad", e, false,true,"Exception handled while trying to load the selected file. Did it exist? Was it Readable?");
				return -2;
			}
		} else {
			System.out.println("User canceled file open request");
			return -1;
		}
	}
}
