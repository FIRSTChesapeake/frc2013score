package FRC_Score_Sys;

import java.util.Comparator;

public class MatchesCompare implements Comparator<SingleMatch> {

	@Override
	public int compare(SingleMatch o1, SingleMatch o2) {
		int MT1 = GetMatchType(o1);
		int MT2 = GetMatchType(o2);
		int MI1 = GetMatchID(o1);
		int MI2 = GetMatchID(o2);
		
		int i = MT1 - MT2;
		if(i != 0) return i;
		
		i = MI1 - MI2;
		return i;
		
		
	}

	private int GetMatchType(SingleMatch s){
		String lead = s.MatchID().substring(0,2);
		switch(lead){
			case "QQ": return 0;
			case "QF": return 1;
			case "SF": return 2;
			case "FF": return 3;
			default: return 9;
		}
	}
	
	private int GetMatchID(SingleMatch s){
		String id = s.MatchID().substring(3);
		try{
			return Integer.parseInt(id);
		} catch (Exception e){
			return 0;
		}
	}
}
