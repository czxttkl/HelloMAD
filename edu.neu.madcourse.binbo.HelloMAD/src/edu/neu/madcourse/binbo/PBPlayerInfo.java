package edu.neu.madcourse.binbo;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import edu.neu.mobileclass.apis.KeyValueAPI;

public class PBPlayerInfo implements IRemoteData, Serializable {
	
	private static final long serialVersionUID = -7060210544600464481L; 
	private static final String TEAMNAME = "MAD_WB_TEAM";
    private static final String PASSWORD = "111111";
    private static final String PLAYER_INFO_KEY_PREFIX = "PBGAME_PLAYER_INFO_"; 
    private static final int DATA_ID = 2;
	
	protected String mName = "";
	protected String mStatus = "";
	protected String mSelLetters = "";
	protected int    mScore = 0;
	protected int    mBestScore = 0;
	protected long   mUpdateTime = 0;
	
	public PBPlayerInfo(String name) {
		mName = name;
	}
	
	public synchronized void setName(String name) {  
        mName = name;  
	} 
	
	public synchronized void setStatus(String status) {  
		mStatus = status;  
	} 
	
	public synchronized void setSelLetters(String letters) {
		mSelLetters = letters;
	}
	
	public synchronized void setScore(int score) {  
        mScore = score;  
	} 
	
	public synchronized void setBestScore(int bestScore) {  
		mBestScore = bestScore;  
	} 
	
	public synchronized void setUpdateTime(long updateTime) {  
		mUpdateTime = updateTime;  
	} 
	
	public synchronized String getName() {  
        return mName;  
	} 
	
	public synchronized String getStatus() {  
		return mStatus;  
	} 
	
	public synchronized String getSelLetters() {
		return mSelLetters;
	}
	
	public synchronized int getScore() {  
        return mScore;  
	} 
	
	public synchronized int getBestScore() {  
		return mBestScore;  
	} 
	
	public synchronized long getUpdateTime() {  
		return mUpdateTime;  
	}
	
	public boolean acquire() throws JSONException {		

		if (KeyValueAPI.isServerAvailable() == false) {
	    	return false;
	    }		
		
		String content = KeyValueAPI.get(TEAMNAME, PASSWORD, PLAYER_INFO_KEY_PREFIX + mName);		
		JSONObject obj = new JSONObject(content);			
					
		setName(obj.getString("name"));
		setScore(obj.getInt("score"));
		setBestScore(obj.getInt("best_score"));
		setSelLetters(obj.getString("selected_letters"));
		setStatus(obj.getString("status"));
		setUpdateTime(obj.getLong("update_time"));			
		
		return true;
	}
	
	public boolean commit() throws JSONException {

		if (KeyValueAPI.isServerAvailable() == false) {
	    	return false;
	    }		
		
		JSONObject obj = new JSONObject();  	              

        obj.put("name", getName());
        obj.put("score", getScore());
        obj.put("best_score", getBestScore()); 
        obj.put("selected_letters", getSelLetters());
        obj.put("status", getStatus());
        obj.put("update_time", getUpdateTime());
        
        String content = obj.toString();
       	KeyValueAPI.put(TEAMNAME, PASSWORD, PLAYER_INFO_KEY_PREFIX + getName(), content);
	
        return true;
    } 
	
	public String obj2json () throws JSONException {
		JSONObject obj = new JSONObject();  	              

        obj.put("name", getName());
        obj.put("score", getScore());
        obj.put("best_score", getBestScore()); 
        obj.put("selected_letters", getSelLetters());
        obj.put("status", getStatus());
        obj.put("update_time", getUpdateTime());
        
        return obj.toString();
	}
    
	public void json2obj (String strJson) throws JSONException {
		JSONObject obj = new JSONObject(strJson);		
		
		setName(obj.getString("name"));
		setScore(obj.getInt("score"));
		setBestScore(obj.getInt("best_score"));
		setSelLetters(obj.getString("selected_letters"));
		setStatus(obj.getString("status"));
		setUpdateTime(obj.getLong("update_time"));	
	}
	
	public int getDataId() {
		return DATA_ID;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
}
