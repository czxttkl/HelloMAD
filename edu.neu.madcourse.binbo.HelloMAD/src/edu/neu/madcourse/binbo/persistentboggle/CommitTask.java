package edu.neu.madcourse.binbo.persistentboggle;

import java.util.ArrayList;

import org.json.JSONException;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

public class CommitTask extends AsyncTask<Void, Integer, Integer> {
	private static final int SERVER_UNAVAILABLE = -1;        
    private static final int COMMIT_DATA_DONE   = 3; 
    private static final int COMMIT_DATA_ERROR  = 4;
	
	protected Handler mHandler  = null;
	protected IRemoteData mData = null;

	public CommitTask(Handler handler, IRemoteData data) {
		mData    = data;
		mHandler = handler;        		
    }
	
    protected Integer doInBackground(Void... arg0) {
    	int result = COMMIT_DATA_DONE;
    	
		try {
			result = mData.commit() ? result : SERVER_UNAVAILABLE;			
		} catch (NullPointerException e) { 
        	e.printStackTrace();
        	result = COMMIT_DATA_ERROR;
    	} catch (JSONException e) {    		
            result = COMMIT_DATA_ERROR;
    	}
    	
        return Integer.valueOf(result);
    }
  
    protected void onPostExecute(Integer result) {
    	Message msg = mHandler.obtainMessage();     	
        msg.arg1 = result;	
        msg.arg2 = mData.getDataId();
        mHandler.sendMessage(msg);
    }
}
