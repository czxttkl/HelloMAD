package edu.neu.madcourse.binbo.persistentboggle;

import java.util.ArrayList;

import org.json.JSONException;

import android.os.Handler;
import android.os.Message;

public class AcquireTask extends Thread {
	private Handler mHandler = null;
    private boolean mRun = false;
    private boolean mEnd = true;
    private boolean mRunOnce = false;
 
    private static final int SERVER_UNAVAILABLE = -1;
	private static final int UPDATE_DATA_DONE   = 1;  
	private static final int UPDATE_DATA_ERROR  = 2;
        
    protected int mInterval = 200;
    protected IRemoteData mData = null;

    public AcquireTask(Handler handler, IRemoteData data, int interval, boolean runOnce) { 
        mHandler  = handler;
        mData     = data;
        mInterval = interval;
        mRunOnce  = runOnce;
    } 
    
    public void setInterval(int interval) {
    	mInterval = interval;
    }

    public void run() { 
    	mRun = true;
    	mEnd = false;
    	
        while (mRun) {
            try {                    
        		Message msg = mHandler.obtainMessage(); 
                msg.arg1 = mData.acquire() ? UPDATE_DATA_DONE : SERVER_UNAVAILABLE;
                msg.arg2 = mData.getDataId();
                mHandler.sendMessage(msg);            	                                                
            } catch (NullPointerException e) { 
            	e.printStackTrace();
        	} catch (JSONException e) {
        		Message msg = mHandler.obtainMessage(); 
                msg.arg1 = UPDATE_DATA_ERROR;
                msg.arg2 = mData.getDataId();
                mHandler.sendMessage(msg); 
        	} finally {        	
				try {
					Thread.sleep(mInterval);
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
}
