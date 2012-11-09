package edu.neu.madcourse.binbo.persistentboggle;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.neu.mobileclass.apis.KeyValueAPI;

public class PBNameList extends ArrayList<String> implements IRemoteData {

	private static final String TEAMNAME = "MAD_WB_TEAM";
    private static final String PASSWORD = "111111";
    private static final String PLAYER_NAME_LIST_KEY = "PBGAME_NAME_LIST";
    private static final int DATA_ID = 1; 

	public PBNameList() {
	}

//	public boolean add(String newName) {
//		if (acquire() == false) {
//			return false;
//		}
//		
//		add(newName);
//		
//		if (commit() == false) {
//			return false;
//		}
//		
//		return true;
//	}
		
	public boolean commit() throws JSONException {	
		
		if (KeyValueAPI.isServerAvailable() == false) {
			return false;
		}
				
		JSONArray array = new JSONArray();
	    
	    for (String name:this) {   
	        JSONObject obj = new JSONObject();  	              		   
            obj.put("name", name);  	           
            array.put(obj); 	            	            		        	              	         
	    } 	    	
	    String content = array.toString();
	   	KeyValueAPI.put(TEAMNAME, PASSWORD, PLAYER_NAME_LIST_KEY, content);		   	 
	    
	    return true;
	}
	
	public boolean acquire() throws JSONException {			

		if (KeyValueAPI.isServerAvailable() == false) {
	    	return false;
	    }		
		
		String retSrc = KeyValueAPI.get(TEAMNAME, PASSWORD, PLAYER_NAME_LIST_KEY);
		
		JSONArray array = null;
		clear();
		array = new JSONArray(retSrc);
		for (int i = 0; i < array.length(); ++i) {
			JSONObject obj = array.getJSONObject(i);			
			this.add(obj.getString("name"));
		}			
			
		return true;
	}			
	
	public int getDataId() {
		return DATA_ID;
	}
}
