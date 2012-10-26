package edu.neu.madcourse.binbo;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.neu.mobileclass.apis.KeyValueAPI;

public class ServerDelegator {
	
	private static final String TEAMNAME = "MAD_WB_TEAM";
    private static final String PASSWORD = "111111";
    private static final String PLAYER_INFO_KEY = "PBGAME_PLAYER_INFOS";    
	
	public boolean commitPlayersInfo(ArrayList<PBPlayerInfo> infos) throws JSONException {
		
		
	    return true;
	}
	
	public ArrayList<PBPlayerInfo> pullPlayerInfos() throws JSONException {	
			
		
		return null;
	}			
}
