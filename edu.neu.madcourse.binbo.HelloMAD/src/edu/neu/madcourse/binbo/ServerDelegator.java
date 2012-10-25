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
		JSONArray array = new JSONArray();  
		
	    for (PBPlayerInfo info:infos){   
	        JSONObject obj = new JSONObject();  	              
	        try {  
	            obj.put("name", info.getName());  
	            obj.put("score", info.getScore());  
	            obj.put("best_score", info.getBestScore());  
	            obj.put("status", info.getStatus());
	            obj.put("update_time", info.getUpdateTime());
	            obj.put("message", info.getMessage());
	            obj.put("invitor", info.getInvitor());
	            obj.put("invite_time", info.getInviteTime());
	            array.put(obj); 
	        } catch (JSONException e) {
	        	throw e;
	        }  	              	         
	    } 
	 
	    if (KeyValueAPI.isServerAvailable()) {
	    	String result = array.toString();
	    	result = KeyValueAPI.put(TEAMNAME, PASSWORD, PLAYER_INFO_KEY, array.toString());
	    	return true;
	    }
	    
	    return false;
	}
	
	public ArrayList<PBPlayerInfo> pullPlayerInfos() throws JSONException {	
		
		if (KeyValueAPI.isServerAvailable() == false) {
	    	return null;
	    }		
		String retSrc = KeyValueAPI.get(TEAMNAME, PASSWORD, PLAYER_INFO_KEY);
		
		JSONArray array = null;
		ArrayList<PBPlayerInfo> infos = null;
		
		try {
			array = new JSONArray(retSrc);
			infos = new ArrayList<PBPlayerInfo>();
			for (int i = 0; i < array.length(); ++i) {
				JSONObject obj = array.getJSONObject(i);
				
				PBPlayerInfo info = new PBPlayerInfo();				
				info.setName(obj.getString("name"));
				info.setScore(obj.getInt("score"));
				info.setBestScore(obj.getInt("best_score"));
				info.setStatus(obj.getString("status"));
				info.setUpdateTime(obj.getLong("update_time"));	
				info.setMessage(obj.getInt("message"));
				info.setInvitor(obj.getString("invitor"));
				info.setInviteTime(obj.getLong("invite_time"));
				infos.add(info); 
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			throw e1;
		}				
		
		return infos;
	}			
}
