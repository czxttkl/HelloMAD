package edu.neu.madcourse.binbo.persistentboggle;

import org.json.JSONException;

import android.os.Handler;
import android.os.Message;

public class AutoCommitter extends Thread {
	private Handler mHandler = null;
    private boolean mRun = false;
    private boolean mEnd = true;
 
    private static final int SERVER_UNAVAILABLE = -1;        
    private static final int COMMIT_DATA_DONE   = 3; 
    private static final int COMMIT_DATA_ERROR  = 4;
        
    protected int mInterval = 200;
    protected IRemoteData mData = null;

    public AutoCommitter(Handler handler, IRemoteData data, int interval) { 
        mHandler  = handler;
        mData     = data;
        mInterval = interval;
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
                msg.arg1 = mData.commit() ? COMMIT_DATA_DONE : SERVER_UNAVAILABLE;
                msg.arg2 = mData.getDataId();
                mHandler.sendMessage(msg);            	                                                
            } catch (NullPointerException e) { 
            	e.printStackTrace();
        	} catch (JSONException e) {
        		Message msg = mHandler.obtainMessage(); 
                msg.arg1 = COMMIT_DATA_ERROR;
                msg.arg2 = mData.getDataId();
                mHandler.sendMessage(msg); 
        	} finally {        	
				try {
					Thread.sleep(mInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
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
