package edu.neu.madcourse.binbo.persistentboggle;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import edu.neu.mobileclass.apis.KeyValueAPI;

public class PBInviteInfo implements IRemoteData {
	
	private static final String TEAMNAME = "MAD_WB_TEAM";
    private static final String PASSWORD = "111111";
    private static final String INVITE_INFO_KEY_PREFIX = "PBINVITE_INFO_"; 
    private static final int DATA_ID = 3;
    
    //after this period of time, the invitation should be invalid
    private int mValidPeriod = 10000;
    
	protected String mPoster = "";
	protected String mReceiver = "";
	protected long mPostTime = 0;
	protected long mReceiveTime = 0;
	protected Boolean mHasNotified = false;
	protected String mStatus = "unknown";
	
	public PBInviteInfo(String poster, String receiver, long postTime) {
		this.mPoster = poster;
		this.mReceiver = receiver;
		this.mPostTime = postTime;
	}
	
	public PBInviteInfo(String poster) {
		this.mPoster = poster;
	}
	
	public Boolean getHasNotified() {  
        return this.mHasNotified;
	}
	
	public String getPoster() {  
		return this.mPoster;  
	}
	
	public String getReceiver() {  
		return this.mReceiver;  
	}
	
	public Long getPostTime() {
		return this.mPostTime;
	}
	
	public Long getReceiveTime() {
		return this.mReceiveTime;
	}
	
	public String getStatus() {
		return this.mStatus;
	}
	
	public void setReceiver(String receiver) {
		this.mReceiver = receiver;
	}
	
	public void setReceiveTime(long time) {
		this.mReceiveTime = time;
	}
	
	public void setPostTime(long time) {
		this.mPostTime = time;
	}
	
	public void setStatus(String status) {
		this.mStatus = status;
	}
	
	public void setHasNotified(Boolean hasNotified) {  
        this.mHasNotified = hasNotified;  
	} 
	
	public Boolean isExpired() {  
		Date expireTime = new Date(this.mPostTime + this.mValidPeriod);
		return (new Date()).after(expireTime);
	}
	
	public Boolean isReceiverExpired() {  
		Date expireTime = new Date(this.mReceiveTime + this.mValidPeriod);
		return (new Date()).after(expireTime);
	}
	
	public boolean acquire() throws JSONException {		

		if (KeyValueAPI.isServerAvailable() == false) {
	    	return false;
	    }		
		
		String content = KeyValueAPI.get(TEAMNAME, PASSWORD,
				INVITE_INFO_KEY_PREFIX + getPoster());
		
		if (content.equals("")) {
	    	return false;
	    }
		JSONObject obj = new JSONObject(content);
					
		this.mPoster = obj.getString("poster");
		this.mReceiver = obj.getString("receiver");
		this.mPostTime = obj.getLong("post_time");
		this.mHasNotified = obj.getBoolean("hasNotified");
		this.mStatus = obj.getString("status");
		this.mReceiveTime = obj.getLong("receive_time");
		
		return true;
	}
	
	public boolean commit() throws JSONException {

		if (KeyValueAPI.isServerAvailable() == false) {
	    	return false;
	    }		
		
		JSONObject obj = new JSONObject();

        obj.put("poster", getPoster());
        obj.put("receiver", getReceiver());
        obj.put("post_time", getPostTime()); 
        obj.put("hasNotified", getHasNotified());
        obj.put("status", getStatus());
        obj.put("receive_time", getReceiveTime());
        
        String content = obj.toString();
       	KeyValueAPI.put(TEAMNAME, PASSWORD, INVITE_INFO_KEY_PREFIX + getPoster(), content);
	
        return true;
    } 
	
	public boolean delete() throws JSONException {
		if (KeyValueAPI.isServerAvailable() == false) {
	    	return false;
	    }
		
		KeyValueAPI.clearKey(TEAMNAME, PASSWORD, INVITE_INFO_KEY_PREFIX + getPoster());
		return true;
	}
    
	public String obj2json() throws JSONException {
		
		JSONObject obj = new JSONObject();  	              

        obj.put("poster", getPoster());
        obj.put("receiver", getReceiver());
        obj.put("post_time", getPostTime()); 
        obj.put("hasNotified", getHasNotified());
        obj.put("status", getStatus());
        obj.put("receive_time", getReceiveTime());
        
        return obj.toString();
	}
	
	public int getDataId() {
		return DATA_ID;
	}
}
