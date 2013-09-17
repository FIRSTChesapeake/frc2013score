package FRC_Score_Sys;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

public class myWebSvr extends NanoHTTPD {

	private List<TeamRankObj> ranks = new ArrayList<TeamRankObj>();
	
	public myWebSvr(int port) {
		super(port);
	}

	public void SetRankData(List<TeamRankObj> newRanks){
		ranks = newRanks;
	}
	
	@Override
    public Response serve(String uri, Method method, Map<String, String> header, Map<String, String> parms, Map<String, String> files) {
        String msg = "<html><body><h1>Hello server</h1>\n";
        if (parms.get("username") == null)
            msg +="<form action='?' method='get'>\n" +
            		"<p>Your name: <input type='text' name='username'></p>\n" +
                            "</form>\n";
        else
            msg += "<p>Hello, " + parms.get("username") + "!</p>";

        msg += "</body></html>\n";

        return new NanoHTTPD.Response(msg);
    }
}
