package edu.neu.madcourse.binbo;

import java.util.ArrayList;

import org.json.JSONException;

import edu.neu.mobileclass.apis.KeyValueAPI;
import android.os.Handler;
import android.os.Message;

public class PlayerInfoAcquirer extends Thread { 
	 
    private Handler mHandler = null;
    private boolean mRun = false;
    private boolean mEnd = true;
    private boolean mRunOnce = false;
 
    private static final int SERVER_UNAVAILABLE   = -1;
	private static final int UPDATE_PLAYERS_INFO  = 0; // get info successfully
    private static final int UPDATE_PLAYERS_ERROR = 1; // fail to get info             
    
    ArrayList<PBPlayerInfo> infos = null;
    ServerDelegator mDelegator = new ServerDelegator();

    public PlayerInfoAcquirer(Handler handler, boolean runOnce) { 
        mHandler = handler;  
        mRunOnce = runOnce;
    } 

    @Override 
    public void run() { 
    	mRun = true;
    	mEnd = false;
    	
        while (mRun) {
            try {                    
            	infos = mDelegator.pullPlayerInfos();
        	
        		Message msg = mHandler.obtainMessage(); 
                msg.arg1 = (infos == null) ? SERVER_UNAVAILABLE : UPDATE_PLAYERS_INFO;
                msg.obj = infos;
                mHandler.sendMessage(msg);            	                
                                
            } catch (NullPointerException e) { 
            	sendErrorMessage();
        	} catch (JSONException e) {
        		sendErrorMessage();
			} finally {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
            
            if (mRunOnce) {
            	mRun = false;
            }
        }                    
        
        mEnd = true;
    }      
	
	public synchronized void end() {
		mRun = false;	
		
		try {
			while (mEnd == false) {
				sleep(30);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}    
	
	private void sendErrorMessage() {
		Message msg = mHandler.obtainMessage(); 
        msg.arg1 = UPDATE_PLAYERS_ERROR;
        msg.obj = null;
        mHandler.sendMessage(msg);
	}
} 