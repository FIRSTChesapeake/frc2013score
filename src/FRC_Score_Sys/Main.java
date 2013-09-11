package FRC_Score_Sys;

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.File;

public class Main {
	static String AppVersion = "1.2.2 (2013-09-10)"; 

	static PrintStream out;
	
	static Scanner in = new Scanner(System.in);
	public static String Ask(String Prompt){
		System.out.print(Prompt+": ");
		String Ans = in.nextLine();
		return Ans;
	}
	
	public static void main(String[] args) {
		System.out.println("You've started Matt's 2013 FRC Scoring App Version: 1.2.3");
		boolean shh = false;
		for(String item : args){
			switch(item){
			case "shh":
				shh = true;
			}
		}
		
		if(shh){
			try{
				File file = new File("logfile.txt");
				out = new PrintStream(file);
				System.out.println("Told to SHH by command line arg. So I guess I have to. Check logfile.txt for verbose data.");
				System.setOut(out);
			} catch (FileNotFoundException e){
				System.out.println("Tried to go to quiet logging but there was a file problem; so you're stuck with me. :P");
			}
		}
		System.out.println("-  If you find my console output annoying, add 'shh' as a command line arguement.");
		System.out.println("   Errors will still print here, however.");
		System.out.println("   This option will likely become default at some point.");
		System.out.println("Opening Main menu.");
		MainMenu MM = new MainMenu();
		//MM.pack();
		MM.setLocationRelativeTo(null);
		MM.setVisible(true);
	}
}