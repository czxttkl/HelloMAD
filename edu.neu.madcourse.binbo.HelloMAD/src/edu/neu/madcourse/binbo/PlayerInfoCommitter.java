package edu.neu.madcourse.binbo;

import java.util.ArrayList;

import org.json.JSONException;

import edu.neu.mobileclass.apis.KeyValueAPI;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;


public class PlayerInfoCommitter extends AsyncTask<ArrayList<PBPlayerInfo>, Integer, Boolean> {
	
	private static final int SERVER_UNAVAILABLE   = -1;        
    private static final int COMMIT_PLAYERS_INFO  = 2; // commit info successfully 
    private static final int COMMIT_PLAYERS_ERROR = 3; // fail to commit info  
	
	Handler mHandler = null;
	ServerDelegator mDelegator = new ServerDelegator();
	
	public PlayerInfoCommitter(Handler handler) { 
		mHandler = handler;        
    }
	
    protected Boolean doInBackground(ArrayList<PBPlayerInfo>... infos) {
    	boolean result = false;
    	
		try {
			result = mDelegator.commitPlayersInfo(infos[0]);			
		} catch (JSONException e) {
			sendErrorMessage();
		}
    	
        return Boolean.valueOf(result);
    }
  
    protected void onPostExecute(Boolean result) {
    	if (result == false) {
    		sendErrorMessage();
		} else {
			Message msg = mHandler.obtainMessage(); 
	        msg.arg1 = COMMIT_PLAYERS_INFO;		        
	        mHandler.sendMessage(msg);
		}
    }
    
    private void sendErrorMessage() {
		Message msg = mHandler.obtainMessage(); 
        msg.arg1 = COMMIT_PLAYERS_ERROR;
        mHandler.sendMessage(msg);
	}
}