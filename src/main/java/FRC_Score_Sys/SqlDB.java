package FRC_Score_Sys;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import FRC_Score_Sys.AllyCreate.AllyTopRow;

import javax.swing.JOptionPane;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

public class SqlDB {

	private String SQLDBVER = "12";

	private Connection c;
	private String DBfile = "score_data.db";

	private boolean IwasNew = false;
	
	private PopupGenerator Pops = new PopupGenerator();
	final Logger logger = LoggerFactory.getLogger(SqlDB.class);

	public boolean wasNewDB(){ return IwasNew; }
	
	public SqlDB() {
		logger.info("Starting SQL Server..");
		File file = new File(DBfile);
		Boolean doNew = false;
		if (!file.exists()) {
			doNew = true;
			IwasNew = true;
			logger.info("DB File not found. Will create.");
		} else {
			logger.info("DB File found. using existing DB.");
		}
		file = null;
		try {
			logger.info("Looking for SQLLite Driver..");
			Class.forName("org.sqlite.JDBC");
			logger.info("Found. Connecting to Database..");
			c = DriverManager.getConnection("jdbc:sqlite:" + DBfile);
			if (doNew) {
				if (GenerateNewDatabase()) {
					logger.info("Inserting Default Options..");
					CreateOptions();
				} else {
					JOptionPane.showMessageDialog(null, "Oh boy - something went wrong while creating the DB Tables. We can't continue.");
					logger.error("DBTABLE CREATE REPORTED AS FAIL");
					System.exit(-1);
				}
			} else {
				String DBV = FetchOption("SQLDBVER");
				if (!DBV.equals(SQLDBVER)) {
					String msg = "Your DB version is out-dated."
							+ "\nYour Version: '" + DBV + "'"
							+ "\nReq Version : '" + SQLDBVER + "'"
							+ "\nSince SQLite doesn't support ALTER TABLE, we have no choice but to replace the DB entirely."
							+ "\nDo you want to do this now?";
					String tit = "Database Out of Date";
					int perform = JOptionPane.showConfirmDialog(null, msg, tit, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if(perform == JOptionPane.YES_OPTION){
						this.close();
						File f = new File(DBfile);
						if(f.delete()){
							JOptionPane.showMessageDialog(null, "Alright!\nNow restart the application and we'll be good to go.");
							System.exit(0);
						} else {
							JOptionPane.showMessageDialog(null, "Ah.. that didn't work. Go to the working directory and delete teh following file manually.\n"+DBfile);
						}
					}
				}
			}
			logger.info("DB Subsystem up and running!");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Alright!\nNow restart the application and we'll be good to go.");
		}
	}
	public int CountRows(String table){
		int cnt = 0;
		String q = "SELECT COUNT(*) as cnt FROM "+table;
		try {
			PreparedStatement s = c.prepareStatement(q);
			//s.setString(1, table);
			ResultSet rs = s.executeQuery();
			while(rs.next()){
				cnt = rs.getInt("cnt");
			}
		} catch (Exception e){
			Pops.Exception("CountRows", e, "It seems we failed to count the number of rows in a table - which could be a problem..", false);
		}
		return cnt;
	}
	
	public int ScrubDB(){
		String q = "DELETE FROM ";
		String[] tables = {"MATCHES", "TEAMS"};
		try{
			for (String table : tables){
				logger.info("Dumping Table "+table);
				PreparedStatement s = c.prepareStatement(q+table);
				s.executeUpdate();
			}
			UpdateOption("ALLYCOUNT", "N");
			return 1;
		} catch (Exception e){
			Pops.Exception("ScrubDB", e, "Failed to Empty Tables", false);
			return 0;
		}
	}
	
	private void AddTeamToDB(int id){
		try{
			PreparedStatement s = c.prepareStatement("INSERT INTO TEAMS VALUES(?, 0, 0, 0, 0, 0, 0, 0)");
			s.setInt(1, id);
			s.executeUpdate();
		} catch (Exception e){
			// Supress
		}
	}
	
	public void AddAllyToDB(AllyTopRow row){
		try{
			logger.info("Adding Alliance {}", row.Rank());
			PreparedStatement s = c.prepareStatement("INSERT INTO ALLYS VALUES(?, ?, ?, ?, 0, 0, 0)");
			s.setInt(1, row.Rank());
			s.setInt(2, row.TeamID());
			s.setInt(3, row.GetPick(1));
			s.setInt(4, row.GetPick(2));
			s.executeUpdate();
		} catch (Exception e){
			Pops.Exception("Adding Alliance to DB", e, "We failed to add an alliance to the DB!", true);
		}
	}
	
	public List<SingleMatch> FetchTeamMatches(int Team){
		List<SingleMatch> Matches = new ArrayList<SingleMatch>();
		logger.info("Fetching Team {}'s Matches.", Team);
		try{
			String qR = "SELECT id FROM MATCHES WHERE ((R1Robot=?) OR (R2Robot=?) OR (R3Robot=?))";
			String qB = "SELECT id FROM MATCHES WHERE ((B1Robot=?) OR (B2Robot=?) OR (B3Robot=?))";
			PreparedStatement sR = c.prepareStatement(qR);
			PreparedStatement sB = c.prepareStatement(qB);
			sR.setInt(1, Team);
			sR.setInt(2, Team);
			sR.setInt(3, Team);
			sB.setInt(1, Team);
			sB.setInt(2, Team);
			sB.setInt(3, Team);
			ResultSet rsR = sR.executeQuery();
			ResultSet rsB = sB.executeQuery();
			while(rsR.next()){
				String[] clrs = {"R"};
				List<SingleMatch> m = FetchMatch(rsR.getString("id"), clrs);
				Matches.add(m.get(0));
			}
			while(rsB.next()){
				String[] clrs = {"B"};
				List<SingleMatch> m = FetchMatch(rsB.getString("id"), clrs);
				Matches.add(m.get(0));
			}
			Collections.sort(Matches);
			return Matches;
		} catch (Exception e){
			Pops.Exception("FetchteamMatches", e,"There was an issue fetching a team's matches?", false);
			return null;
		}
	}
	
	public void RefreshRanks(List<Integer> TeamNumbers){
		long timeStart = System.nanoTime();
		List<TeamRankObj> Teams = FetchTeamlist(false, TeamNumbers);
		logger.info("Refreshing Rankings.. Please wait..");
		try{
			String qR = "SELECT SUM(case RQS when 2 then 1 else 0 end) as wins, SUM(case RQS when 1 then 1 else 0 end) as ties, COUNT(*) as tot, SUM(RQS) as QS, SUM(RAP) as AP, SUM(RCP) as CP, SUM(RTP) as TP FROM MATCHES WHERE ((R1Robot=? AND R1Sur=0 AND R1Dq=0) OR (R2Robot=? AND R2Sur=0 AND R2Dq=0) OR (R3Robot=? AND R3Sur=0 AND R3Dq=0)) AND Saved=1 AND ID LIKE 'QQ%'";
			String qB = "SELECT SUM(case BQS when 2 then 1 else 0 end) as wins, SUM(case BQS when 1 then 1 else 0 end) as ties, COUNT(*) as tot, SUM(BQS) as QS, SUM(BAP) as AP, SUM(BCP) as CP, SUM(BTP) as TP FROM MATCHES WHERE ((B1Robot=? AND B1Sur=0 AND B1Dq=0) OR (B2Robot=? AND B2Sur=0 AND B2Dq=0) OR (B3Robot=? AND B3Sur=0 AND B3Dq=0)) AND Saved=1 AND ID LIKE 'QQ%'";
			String qT = "UPDATE TEAMS SET QS=?, AP=?, CP=?, TP=?, WINS=?, TIES=?, TOT=? WHERE ID=?";
			PreparedStatement sR = c.prepareStatement(qR);
			PreparedStatement sB = c.prepareStatement(qB);
			PreparedStatement sT = c.prepareStatement(qT);
			for(TeamRankObj team : Teams){
				sR.setInt(1, team.ID);
				sR.setInt(2, team.ID);
				sR.setInt(3, team.ID);
				sB.setInt(1, team.ID);
				sB.setInt(2, team.ID);
				sB.setInt(3, team.ID);
				ResultSet rsR = sR.executeQuery();
				ResultSet rsB = sB.executeQuery();
				TeamRankObj newRank = new TeamRankObj();
				newRank.ID = team.ID;
				while(rsR.next()){
					newRank.QS = rsR.getInt("QS");
					newRank.AP = rsR.getInt("AP");
					newRank.CP = rsR.getInt("CP");
					newRank.TP = rsR.getInt("TP");
					newRank.wins = rsR.getInt("wins");
					newRank.ties = rsR.getInt("ties");
					newRank.tot = rsR.getInt("tot");
				}
				while(rsB.next()){
					newRank.QS = newRank.QS+rsB.getInt("QS");
					newRank.AP = newRank.AP+rsB.getInt("AP");
					newRank.CP = newRank.CP+rsB.getInt("CP");
					newRank.TP = newRank.TP+rsB.getInt("TP");
					newRank.wins = newRank.wins+rsB.getInt("wins");
					newRank.ties = newRank.ties+rsB.getInt("ties");
					newRank.tot  = newRank.tot+rsB.getInt("tot");
				}
				sT.setInt(1, newRank.QS);
				sT.setInt(2, newRank.AP);
				sT.setInt(3, newRank.CP);
				sT.setInt(4, newRank.TP);
				sT.setInt(5, newRank.wins);
				sT.setInt(6, newRank.ties);
				sT.setInt(7, newRank.tot);
				// WHERE
				sT.setInt(8, newRank.ID);
				int ret = sT.executeUpdate();
				if(ret!=1){
					logger.error("failed to save the ranks for '"+newRank.ID+"' (Returned: "+ret+")"+" Canceled the rest of the refresh to be safe.");
					break;
				} else {
					logger.debug("Updated Team '"+newRank.ID+"'");
				}
			}
			logger.info("Ranking Updated");
		} catch (Exception e){
			Pops.Exception("RefreshRanks", e,"The ranks failed to refresh. This is going to be a problem. :/", false);
		}
		long timeStop = System.nanoTime();
		long duration = ((timeStop - timeStart)/1000000000);
		logger.info("Rank Refresh took: "+duration+" seconds.");
	}
	public void DoElims(String MatchMode) throws Exception{
		logger.info("SQL DB Performing Mode Switch to {}..", MatchMode);
		int[] teams_qf1 = {1, 8};
		int[] teams_qf2 = {2, 7};
		int[] teams_qf3 = {3, 6};
		int[] teams_qf4 = {4, 5};

		int[] teams_sf1 = {1, 2, 7, 8};
		int[] teams_sf2 = {3, 4, 5, 6};
		switch (MatchMode){
			case "QF":
				AddElimMatch(teams_qf1, "QF", 1);
				AddElimMatch(teams_qf2, "QF", 2);
				AddElimMatch(teams_qf3, "QF", 3);
				AddElimMatch(teams_qf4, "QF", 4);
			break;
			case "SF":
				try {
					int qf1_winner = FetchWinningAlliance(teams_qf1, "QF");
					logger.info("Found QF1 Winner {}", qf1_winner);
					int qf2_winner = FetchWinningAlliance(teams_qf2, "QF");
					logger.info("Found QF2 Winner {}", qf2_winner);
					int qf3_winner = FetchWinningAlliance(teams_qf3, "QF");
					logger.info("Found QF3 Winner {}", qf3_winner);
					int qf4_winner = FetchWinningAlliance(teams_qf4, "QF");
					logger.info("Found QF4 Winner {}", qf4_winner);
					int[] sf1_players = {qf1_winner, qf2_winner};
					int[] sf2_players = {qf3_winner, qf4_winner};
					AddElimMatch(sf1_players, "SF", 1);
					AddElimMatch(sf2_players, "SF", 2);
					
				} catch (Exception e){
					logger.error("Not all QF's were complete. Failed: {}",e.getMessage());
					throw new Exception("Not complete");
				}
			break;
			case "FF":
				try{
					int sf1_winner = FetchWinningAlliance(teams_sf1, "SF");
					logger.info("Found SF1 Winner {}", sf1_winner);
					int sf2_winner = FetchWinningAlliance(teams_sf2, "SF");
					logger.info("Found SF2 Winner {}", sf2_winner);
					int[] ff_players = {sf1_winner, sf2_winner};
					AddElimMatch(ff_players, "FF", 1);
				} catch (Exception e){
					logger.info("Not all SF's were compelte. Failed.");
					throw new Exception ("Not Complete");
				}
			break;
		}
		UpdateOption("MATCHMODE", MatchMode);
	}
	
	private void AddElimMatch(int[] allys, String type, int MatchID){
		PreparedStatement ps;
		try {
			ps = c.prepareStatement("SELECT * FROM ALLYS WHERE id=? OR id =?");
			int ally1 = allys[0];
			int ally2 = allys[1];
			ps.setInt(1, ally1);
			ps.setInt(2, ally2);
			ResultSet rs = ps.executeQuery();
			String m = "";
			boolean first = true;
			while(rs.next()){
				if(!first) m = m + " ";
				String T1 = String.valueOf(rs.getInt("Team1"));
				String T2 = String.valueOf(rs.getInt("Team2"));
				String T3 = String.valueOf(rs.getInt("Team3"));
				m = m + T1 + " 0 " + T2 + " 0";
				if(!T3.equals("-1")) m = m + " " + T3 + " 0";
				first = false;
			}
			
			String M1 = (MatchID+"1 "+m);
			String M2 = (MatchID+"2 "+m);
			String M3 = (MatchID+"3 "+m);
			AddMatchToDB(M1.split(" "), type);
			AddMatchToDB(M2.split(" "), type);
			AddMatchToDB(M3.split(" "), type);
		} catch (SQLException e) {
			Pops.Exception("Adding Elim Match", e, "We ran into a problem adding an elim match to the DB", false);
		}
	}
	
	public boolean RefreshAllyWins(List<Integer> TeamCapts, String MatchMode){
		if(TeamCapts == null){
			try {
				TeamCapts = new ArrayList<Integer>();
				String q = "SELECT Team1 FROM ALLYS";
				PreparedStatement ps = c.prepareStatement(q);
				ResultSet rs = ps.executeQuery();
				while(rs.next()){
					TeamCapts.add(rs.getInt("Team1"));
				}
			} catch (SQLException e) {
				Pops.Exception("Refreshing All Allys", e, "We tried a full refresh on the Ally table, and it failed!",  false);
			}
			
		}
		for(int team : TeamCapts){
			if(!UpdateAllyWins(team, MatchMode)) return false;
		}
		return true;
	}
	
	private boolean UpdateAllyWins(int TeamCapt, String MatchMode){
		try {
			logger.info("Updating Alliance team captain {}", TeamCapt);
			String qR = "SELECT SUM(case RQS when 2 then 1 else 0 end) as wins FROM MATCHES WHERE R1Robot=? AND Saved=1 AND ID LIKE ?";
			String qB = "SELECT SUM(case BQS when 2 then 1 else 0 end) as wins FROM MATCHES WHERE B1Robot=? AND Saved=1 AND ID LIKE ?";
			String qT = "UPDATE ALLYS SET "+MatchMode+"=? WHERE Team1=?";
			PreparedStatement sR = c.prepareStatement(qR);
			PreparedStatement sB = c.prepareStatement(qB);
			PreparedStatement sT = c.prepareStatement(qT);
		
			sR.setInt(1, TeamCapt);
			sB.setInt(1, TeamCapt);
			
			sR.setString(2, MatchMode+"%");
			sB.setString(2, MatchMode+"%");
			
			ResultSet rsR = sR.executeQuery();
			ResultSet rsB = sB.executeQuery();
			
			int sumR = 0, sumB = 0;
			while(rsR.next()) {
				int cnt = rsR.getInt("wins");
				sumR = sumR + cnt;
			}
			while(rsB.next()) {
				int cnt = rsB.getInt("wins");
				sumB = sumB + cnt;
			}
			
			int tot = sumR + sumB;
			
			logger.info("Found {} total wins in current mode", tot);
			
			sT.setInt(1, tot);
			sT.setInt(2, TeamCapt);
			
			int ret = sT.executeUpdate();
			if(ret==1) return true;
			return false;
		} catch (SQLException e) {
			Pops.Exception("AwardAllyWin", e, "Encountered an issue awarding an Alliance a win for eliminations.", false);
			return false;
		}
	}
	
	public int FetchWinningAlliance(int[] ids, String MatchMode) throws Exception{
		String where_string = "SELECT ID FROM ALLYS WHERE (";
		boolean first = true;
		for(int id : ids){
			if(!first) where_string = where_string + " OR ";
			where_string = where_string + "ID=" + id;
			first = false;
		}
		where_string = where_string + ") AND " + MatchMode + "=2";
		PreparedStatement ps = c.prepareStatement(where_string);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			int id = rs.getInt("ID");
			return id;
		}
		throw new Exception("Winning Alliance not found! Stmt: "+where_string);
	}
	public int AddMatchToDB(String[] matchInfo, String type) {
		try {
			logger.debug("==== AddingMatchToDB ====");
			PreparedStatement s;
			int len = matchInfo.length;
			String spot1 = "?";
			String spot2 = "?";
			switch(len){
			case 9:
				spot1 = "-1";
				spot2 = "1";
				logger.debug("This match is 2v2!");
				break;
			case 13:
				logger.debug("This match is 3v3!");
				break;
			default:
				logger.error("WARNING: Match line not expected length: {}?",len);
			}
			String q = "INSERT INTO MATCHES VALUES(?, ?, ?, ?, ?, "+spot1+", "+spot2+", ?, ?, ?, ?, "+spot1+", "+spot2+", 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)";
			s = c.prepareStatement(q);
			int i = 1;
			int first = 0;
			for (String item : matchInfo) {
				logger.debug(" Evaluating '{}'.. ", item);
				if (first == 0) {
					logger.debug("  Assigned as Match ID");
					item = PadString(item);
					s.setString(i, type + item);
					first = 1;
				} else if(first == 1) {
					logger.debug("  Assigned as TeamNumber");
					int Team = Integer.parseInt(item);
					AddTeamToDB(Team);
					s.setInt(i, Team);
					first = 2;
				} else if(first == 2) {
					logger.debug("  Assigned as Sourogate Flag");
					s.setInt(i, Integer.parseInt(item));
					first = 1;
				}
				i = i + 1;
			}
			int ret = s.executeUpdate();
			s.close();
			return ret;
		} catch (Exception e) {
			Pops.Exception("AddMatchedToDB", e, "Something was really wrong with the match import.",false);
			return -1;
		}
	}

	private String BuildUpdateString(String q, String clr, String field) {
		return q + " " + clr + field + " = ?,";
	}

	public void close() {
		logger.info("SQL Lite DB Closing Gracefully. Goodnight!");
		try {
			c.close();
		} catch (Exception e) {
			logger.error("DB Close threw error?");
		}
	}

	private boolean CreateEachOption(String Name, String Value, boolean Public) throws Exception {
		int Pub = 0;
		if (Public) {
			Pub = 1;
		}
		String q = "INSERT INTO OPTIONS VALUES ('" + Name + "', '" + Value + "', " + Pub + ")";
		int cre = PerformInternalUpdateQuery(q);
		if (cre != 1) {
			return false;
		} else {
			return true;
		}
	}

	private boolean CreateOptions() {
		try{
			CreateEachOption("EVENTNAME", "Mystery Event", true);
			CreateEachOption("EVENTVENUE", "The Acme Center", true);
			CreateEachOption("EVENTLOCATION", "Nowhere, AZ", true);
			
			CreateEachOption("SQLDBVER", SQLDBVER, false);
			CreateEachOption("ALLYCOUNT", "N", false);
			CreateEachOption("MATCHMODE", "QQ", false);
			
			return true;
		} catch (Exception e){
			return false;
		}
		
	}

	public List<SingleMatch> FetchMatch(String id) {
		String[] defaultClrs = {"R", "B"};
		return FetchMatch(id, defaultClrs);
	}
	
	public List<SingleMatch> FetchMatch(String id, String[] clrs) {
		logger.debug("Match Fetch Requested for id {}", id);
		List<SingleMatch> ScoreList = new ArrayList<SingleMatch>();
		try {
			PreparedStatement s = c.prepareStatement("SELECT * FROM MATCHES WHERE id = ? LIMIT 1");
			s.setString(1, id);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				for (String clr : clrs) {
					logger.debug("Gathering {}'s", clr);
					SingleMatch Scores = new SingleMatch(id, clr);
					Scores.Robot1 = rs.getInt(clr + "1Robot");
					Scores.Robot2 = rs.getInt(clr + "2Robot");
					Scores.Robot3 = rs.getInt(clr + "3Robot");

					Scores.Sur1 = rs.getBoolean(clr + "1Sur");
					Scores.Sur2 = rs.getBoolean(clr + "2Sur");
					Scores.Sur3 = rs.getBoolean(clr + "3Sur");

					Scores.Climb1 = rs.getInt(clr + "1Climb");
					Scores.Climb2 = rs.getInt(clr + "2Climb");
					Scores.Climb3 = rs.getInt(clr + "3Climb");

					Scores.Dq1 = rs.getBoolean(clr + "1Dq");
					Scores.Dq2 = rs.getBoolean(clr + "2Dq");
					Scores.Dq3 = rs.getBoolean(clr + "3Dq");

					Scores.DisksLA = rs.getInt(clr + "DisksLA");
					Scores.DisksLT = rs.getInt(clr + "DisksLT");
					Scores.DisksMA = rs.getInt(clr + "DisksMA");
					Scores.DisksMT = rs.getInt(clr + "DisksMT");
					Scores.DisksHA = rs.getInt(clr + "DisksHA");
					Scores.DisksHT = rs.getInt(clr + "DisksHT");
					Scores.DisksP = rs.getInt(clr + "DisksP");

					Scores.Foul = rs.getInt(clr + "Foul");
					Scores.TFoul = rs.getInt(clr + "TFoul");

					Scores.Score = rs.getInt(clr + "Score");
					
					Scores.QS = rs.getInt(clr + "QS");
					Scores.AP = rs.getInt(clr + "AP");
					Scores.CP = rs.getInt(clr + "CP");
					Scores.TP = rs.getInt(clr + "TP");
					//Non-color specific
					Scores.Played = rs.getBoolean("Saved");

					ScoreList.add(Scores);
				}
			}

		} catch (Exception e) {
			Pops.Exception("FetchMatch", e, "We were unable to fetch that match?", false);
		}
		logger.debug("Match Fetched. Here ya go!");
		return ScoreList;
	}
	
	public List<TeamRankObj> FetchTeamlist(boolean rank, List<Integer> TeamNumbers){
		logger.info("TeamList Fetch Requested");
		List<TeamRankObj> WholeList = new ArrayList<TeamRankObj>();
		String rank_order = "ID";
		if(rank) rank_order = "QS DESC,AP DESC,CP DESC,TP DESC";
		try {
			String q = "SELECT * FROM TEAMS";
			if(TeamNumbers != null){
				q = q + " WHERE ";
				boolean first = true;
				for(int Tn : TeamNumbers){
					if(!first) q = q + " OR ";
					first = false;
					q = q + "ID=" + Tn;
				}
			}
			q = q +  " ORDER BY "+rank_order;
			PreparedStatement s = c.prepareStatement(q);
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				TeamRankObj n = new TeamRankObj();
				n.ID = rs.getInt("ID");
				n.QS = rs.getInt("QS");
				n.AP = rs.getInt("AP");
				n.CP = rs.getInt("CP");
				n.TP = rs.getInt("TP");
				n.wins = rs.getInt("WINS");
				n.ties = rs.getInt("TIES");
				n.tot  = rs.getInt("TOT");
				WholeList.add(n);
			}
		} catch (Exception e) {
			Pops.Exception("FetchTeamList", e, "Match list can not be loaded.",false);
		}
		return WholeList;
	}
	public List<MatchListObj> FetchMatchList(String type) {
		String s = "SELECT id,Saved,RScore,BScore FROM MATCHES WHERE id LIKE ?";
		try {
			PreparedStatement ps = c.prepareStatement(s);
			ps.setString(1, type+"%");
			return FetchMatchList(ps);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		return null;
	}
	public List<MatchListObj> FetchMatchList(String type, int top) {
		try {
			PreparedStatement ps = c.prepareStatement("SELECT id,Saved,RScore,BScore FROM MATCHES WHERE id LIKE ? AND Saved=1 ORDER BY id desc LIMIT ?");
			ps.setString(1, type+"%");
			ps.setString(2, String.valueOf(top));
			return FetchMatchList(ps);
		} catch (SQLException e) {
			logger.error("SQL Error Fetching for Web: {}", e.fillInStackTrace());
		}
		return null;
	}
	
	
	private List<MatchListObj> FetchMatchList(PreparedStatement ps) {
		List<MatchListObj> WholeList = new ArrayList<MatchListObj>();
		try {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				MatchListObj MLO = new MatchListObj(rs.getString("ID"));
				int BScore = rs.getInt("BScore");
				int RScore = rs.getInt("RScore");
				
				// Added for webserver
				List<SingleMatch> matchdata = FetchMatch(rs.getString("ID"));
				for(SingleMatch matchdata_single : matchdata){
					if(matchdata_single.aColor().equals("B")) MLO.B_score = matchdata_single;
					if(matchdata_single.aColor().equals("R")) MLO.R_score = matchdata_single;
				}
				
				
				if (rs.getBoolean("Saved")) {
					MLO.Played = true;
					if (BScore == RScore) {
						MLO.Clr = MLO.color_yellow;
						MLO.Score = BScore + " to " + RScore;
					}
					if (BScore > RScore) {
						MLO.Clr = MLO.color_blue;
						MLO.Score = BScore + " to " + RScore;
					}
					if (BScore < RScore) {
						MLO.Clr = MLO.color_red;
						MLO.Score = RScore + " to " + BScore;
					}
				}
				WholeList.add(MLO);
			}

		} catch (Exception e) {
			Pops.Exception("FetchMatchList", e, "Match list can not be loaded.",false);
		}
		return WholeList;
	}

	public String FetchOption(String Name) {
		try {
			String q = "SELECT VAL FROM OPTIONS WHERE ID=?";
			PreparedStatement s = c.prepareStatement(q);
			s.setString(1, Name);
			ResultSet rs = s.executeQuery();
			String ret = "";
			while (rs.next()) {
				ret = rs.getString("VAL");
			}
			return ret;
		} catch (Exception e) {
			logger.error("Fetch option threw error "+e.getMessage());
			return "";
		}

	}

	public List<OptionSetPanel> FetchPublicOptions() {
		List<OptionSetPanel> WholeList = new ArrayList<OptionSetPanel>();
		try {
			PreparedStatement s = c.prepareStatement("SELECT ID,VAL FROM OPTIONS WHERE PUBLIC = 1");
			ResultSet rs = s.executeQuery();
			while (rs.next()) {
				OptionSetPanel newOpt = new OptionSetPanel(rs.getString("ID"), rs.getString("VAL"));
				WholeList.add(newOpt);
			}
		} catch (Exception e) {
			Pops.Exception("FetchOptionList", e, "Option list can not be loaded.", false);
		}
		return WholeList;
	}

	private String FormatSQLBuild(String PrevString, String field, String Type, String Clr) {
		return FormatSQLBuild(PrevString, field, Type, Clr, "");

	}

	private String FormatSQLBuild(String PrevString, String field, String Type, String Clr, String Options) {
		String out = PrevString;
		out = out + " " + Clr + field;
		out = out + " " + Type;
		if (Options != "") {
			out = out + " " + Options;
		}
		out = out + " NOT NULL,";
		return out;
	}

	private boolean GenerateNewDatabase() {
		logger.info("Creating new database tables..");
		String[] clrs = { "R", "B" };

		boolean Table1Success = false;
		boolean Table2Success = false;
		boolean Table3Success = false;

		String q = "CREATE TABLE MATCHES (";
		q = FormatSQLBuild(q, "ID", "TEXT", "", "PRIMARY KEY NOT NULL");

		for (String clr : clrs) {
			q = FormatSQLBuild(q, "1Robot", "INT", clr);
			q = FormatSQLBuild(q, "1Sur", "BOOLEAN", clr);
			q = FormatSQLBuild(q, "2Robot", "INT", clr);
			q = FormatSQLBuild(q, "2Sur", "BOOLEAN", clr);
			q = FormatSQLBuild(q, "3Robot", "INT", clr);
			q = FormatSQLBuild(q, "3Sur", "BOOLEAN", clr);
		}

		for (String clr : clrs) {
			q = FormatSQLBuild(q, "DisksLA", "INT", clr);
			q = FormatSQLBuild(q, "DisksLT", "INT", clr);
			q = FormatSQLBuild(q, "DisksMA", "INT", clr);
			q = FormatSQLBuild(q, "DisksMT", "INT", clr);
			q = FormatSQLBuild(q, "DisksHA", "INT", clr);
			q = FormatSQLBuild(q, "DisksHT", "INT", clr);
			q = FormatSQLBuild(q, "DisksP", "INT", clr);
			q = FormatSQLBuild(q, "1Climb", "INT", clr);
			q = FormatSQLBuild(q, "2Climb", "INT", clr);
			q = FormatSQLBuild(q, "3Climb", "INT", clr);
			q = FormatSQLBuild(q, "1Dq", "BOOLEAN", clr);
			q = FormatSQLBuild(q, "2Dq", "BOOLEAN", clr);
			q = FormatSQLBuild(q, "3Dq", "BOOLEAN", clr);
			q = FormatSQLBuild(q, "Foul", "INT", clr);
			q = FormatSQLBuild(q, "TFoul", "INT", clr);
			q = FormatSQLBuild(q, "QS", "INT", clr);
			q = FormatSQLBuild(q, "AP", "INT", clr);
			q = FormatSQLBuild(q, "CP", "INT", clr);
			q = FormatSQLBuild(q, "TP", "INT", clr);
			q = FormatSQLBuild(q, "Score", "INT", clr);
		}
		q = FormatSQLBuild(q, "Saved", "BOOLEAN", "");

		q = q.substring(0, q.length() - 1);
		q = q + ")";
		int cre = PerformInternalUpdateQuery(q);
		if (cre != 0) {
			logger.info("Matches Table Create failed!");
		} else {
			logger.info("Matches Table Create successful!");
			Table1Success = true;
		}

		q = "CREATE TABLE TEAMS (ID INT PRIMARY KEY NOT NULL, QS INT NOT NULL, AP INT NOT NULL, CP INT NOT NULL, TP INT NOT NULL, WINS INT NOT NULL, TIES INT NOT NULL, TOT INT NOT NULL)";
		cre = PerformInternalUpdateQuery(q);
		if (cre != 0) {
			logger.info("Teams Table Create failed!");
			return false;
		} else {
			logger.info("Teams Table Create successful!");
			if (Table1Success) {
				Table2Success = true;
			}
		}
		
		q = "CREATE TABLE OPTIONS (ID TEXT PRIMARY KEY NOT NULL, VAL TEXT NOT NULL, PUBLIC BOOLEAN NOT NULL)";
		cre = PerformInternalUpdateQuery(q);
		if (cre != 0) {
			logger.info("Options Table Create failed!");
			return false;
		} else {
			logger.info("Options Table Create successful!");
			if (Table2Success) {
				Table3Success = true;
			}
		}
		
		q = "CREATE TABLE ALLYS (ID TEXT PRIMARY KEY NOT NULL, Team1 INT NOT NULL, Team2 INT NOT NULL, Team3 INT NOT NULL, QF INT NOT NULL, SF INT NOT NULL, FF INT NOT NULL)";
		cre = PerformInternalUpdateQuery(q);
		if (cre != 0) {
			logger.info("Finals Ally Table Create failed!");
			return false;
		} else {
			logger.info("Finals Ally Table Create successful!");
			if (Table3Success) {
				return true;
			}
			return false;
		}
	}

	private String PadString(String inStr) {
		if (inStr.length() >= 4) {
			return inStr;
		}
		String out = inStr;
		while (out.length() != 4) {
			out = "0" + out;
		}
		return out;
	}

	public int PerformInternalUpdateQuery(String q) {
		try {
			Statement st = c.createStatement();
			int ret = st.executeUpdate(q);
			st.close();
			return ret;
		} catch (Exception e) {
			Pops.Exception("PerformInternalUpdt",e,"SQL Update Function failed at a required task. :/", false);
			return -1;
		}
	}

	public boolean SaveMatchChanges(List<SingleMatch> Match) {
		try {
			String q = "UPDATE MATCHES SET";
			String[] clrs = { "R", "B" };
			// Build the query
			logger.info("Saving Match to DB");
			logger.debug("Building query..");
			for (String clr : clrs) {
				logger.debug("Making {}'s columns..", clr);
				q = BuildUpdateString(q, clr, "DisksLA");
				q = BuildUpdateString(q, clr, "DisksLT");
				q = BuildUpdateString(q, clr, "DisksMA");
				q = BuildUpdateString(q, clr, "DisksMT");
				q = BuildUpdateString(q, clr, "DisksHA");
				q = BuildUpdateString(q, clr, "DisksHT");
				q = BuildUpdateString(q, clr, "DisksP");
				q = BuildUpdateString(q, clr, "1Climb");
				q = BuildUpdateString(q, clr, "2Climb");
				q = BuildUpdateString(q, clr, "3Climb");
				q = BuildUpdateString(q, clr, "1Dq");
				q = BuildUpdateString(q, clr, "2Dq");
				q = BuildUpdateString(q, clr, "3Dq");
				q = BuildUpdateString(q, clr, "Foul");
				q = BuildUpdateString(q, clr, "TFoul");
				q = BuildUpdateString(q, clr, "QS");
				q = BuildUpdateString(q, clr, "AP");
				q = BuildUpdateString(q, clr, "CP");
				q = BuildUpdateString(q, clr, "TP");
				q = BuildUpdateString(q, clr, "Score");
			}
			q = q + " Saved = 1 WHERE ID = ?";
			PreparedStatement s = c.prepareStatement(q);
			SingleMatch B = new SingleMatch();
			SingleMatch R = new SingleMatch();
			for (SingleMatch m : Match) {
				if (m.aColor() == "R") {
					R = m;
				}
				if (m.aColor() == "B") {
					B = m;
				}
			}
			logger.debug("Filling Columns..");
			// Red
			s.setInt(1, R.DisksLA);
			s.setInt(2, R.DisksLT);
			s.setInt(3, R.DisksMA);
			s.setInt(4, R.DisksMT);
			s.setInt(5, R.DisksHA);
			s.setInt(6, R.DisksHT);
			s.setInt(7, R.DisksP);
			s.setInt(8, R.Climb1);
			s.setInt(9, R.Climb2);
			s.setInt(10, R.Climb3);
			s.setBoolean(11, R.Dq1);
			s.setBoolean(12, R.Dq2);
			s.setBoolean(13, R.Dq3);
			s.setInt(14, R.Foul);
			s.setInt(15, R.TFoul);
			s.setInt(16, R.QS);
			s.setInt(17, R.AP);
			s.setInt(18, R.CP);
			s.setInt(19, R.TP);
			s.setInt(20, R.Score);
			// Blue
			s.setInt(21, B.DisksLA);
			s.setInt(22, B.DisksLT);
			s.setInt(23, B.DisksMA);
			s.setInt(24, B.DisksMT);
			s.setInt(25, B.DisksHA);
			s.setInt(26, B.DisksHT);
			s.setInt(27, B.DisksP);
			s.setInt(28, B.Climb1);
			s.setInt(29, B.Climb2);
			s.setInt(30, B.Climb3);
			s.setBoolean(31, B.Dq1);
			s.setBoolean(32, B.Dq2);
			s.setBoolean(33, B.Dq3);
			s.setInt(34, B.Foul);
			s.setInt(35, B.TFoul);
			s.setInt(36, B.QS);
			s.setInt(37, B.AP);
			s.setInt(38, B.CP);
			s.setInt(39, B.TP);
			s.setInt(40, B.Score);
			// WHERE
			s.setString(41, B.MatchID());
			logger.debug("Performing DB Update..");
			int ret = s.executeUpdate();
			if (ret != 1) {
				logger.info("Update Failed. '"+ret+"' "+s.getWarnings());
				return false;
			}
			logger.info("Update Complete!");
			return true;
		} catch (Exception e) {
			Pops.Exception("UpdateMatch", e, "Match update failed?", false);
		}

		return false;
	}

	public boolean UpdateOption(String Name, String Val) {
		try {
			String q = "UPDATE OPTIONS SET VAL=? WHERE ID=?";
			PreparedStatement s = c.prepareStatement(q);
			s.setString(1, Val);
			s.setString(2, Name);
			int cre = s.executeUpdate();
			if (cre != 1) {
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			logger.error("Update Option threw exception "+e.getMessage());
			return false;
		}

	}
}
