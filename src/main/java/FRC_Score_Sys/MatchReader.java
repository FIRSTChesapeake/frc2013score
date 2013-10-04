package FRC_Score_Sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MatchReader {

	private PopupGenerator Pops = new PopupGenerator();
	
	final Logger logger = LoggerFactory.getLogger(MatchReader.class);

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
				long timeStart = System.nanoTime();
				BufferedReader br = new BufferedReader(new FileReader(f));
				String line;
				int RetAdd = 0;
				List<String> WholeFile = new ArrayList<String>();
				logger.info("Reading File..");
				while ((line = br.readLine()) != null) {
					if (line.length() > 0) {
						WholeFile.add(line);
					}
				}
				br.close();
				logger.info("File Closed.");
				logger.info("Importing {} matches.. Please wait..", WholeFile.size());
				for (String match : WholeFile) {
					String[] spl = match.split(" ");
					int ret = myParent.CommHandle.SqlTalk.AddMatchToDB(spl);
					if(ret == -1){
						String msg = "We encountered an error reading that match file into the DB, so to prevent a million error messages, I'll abort if you like?";
						int answer = JOptionPane.showConfirmDialog(null, msg, "Import Matches", JOptionPane.YES_NO_OPTION);
						if(answer == JOptionPane.YES_OPTION) break;
					}
					logger.debug("Result: {}",ret);
					RetAdd = RetAdd + ret;
					tot = tot + 1;
				}
				DoneFlag = true;
				int leftover = tot - RetAdd;
				if (leftover == 1) {
					logger.info("All but 1 line was imported ({} matches). This is normal if you didn't edit the file.", RetAdd);
					logger.info("(Usually a blank line at the bottom.)");
				} else if (leftover == 0) {
					logger.info("All line were imported.");
				} else {
					logger.info("Imported {} lines out of {}.", RetAdd, tot);
					logger.info("(Remember: There is usually a blank line at the bottom.)");
				}
				long timeStop = System.nanoTime();
				long duration = ((timeStop - timeStart)/1000000000);
				logger.info("Match Import took: "+duration+" seconds.");
			} catch (Exception e) {
				Pops.Exception("Run", e, "Woah. Major error while reading the file. Is this really output from MatchMaker?", false);
				DoneFlag = true;
				
			}
		}
	}

	private MainMenu myParent;

	public MatchReader(MainMenu parent) {
		myParent = parent;
		logger.info("FileReader Created");
	}

	public int DoFileLoad() {
		JFileChooser fc = new JFileChooser();
		logger.info("Asking user to find file..");
		int ret = fc.showOpenDialog(myParent);
		if (ret == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fc.getSelectedFile();
				logger.info("User choose: {}", file.toString());
				if(file.exists()){
					DataLoader DL = new DataLoader(file);
					DL.run();
					while (!DL.DoneFlag) {
						Thread.sleep(50);
					}
					return 0;
				} else {
					String msg = "I don't mean to call you a liar or anything - but I really can't find that file. Sorry!";
					JOptionPane.showMessageDialog(null, msg);
					return -2;
				}
			} catch (Exception e) {
				Pops.Exception("FileLoad", e, "Exception handled while trying to load the selected file. Did it exist? Was it Readable?", false);
				return -2;
			}
		} else {
			logger.info("User canceled file open request");
			return -1;
		}
	}
}
