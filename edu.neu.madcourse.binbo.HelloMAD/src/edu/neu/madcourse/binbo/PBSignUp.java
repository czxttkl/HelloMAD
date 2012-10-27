package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PBSignUp extends Activity implements OnClickListener {
	private EditText mEditTextAccount = null;
	private PBNameList  mNames   = new PBNameList();
	private AcquireTask mAcquire = null;
	private CommitTask  mCommit  = null;
	private static final String ACCOUNT_NAME = "account_name";
	
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {
		
		private static final int SERVER_UNAVAILABLE = -1;
		private static final int UPDATE_DATA_DONE   = 1;  
		private static final int UPDATE_DATA_ERROR  = 2;
		private static final int COMMIT_DATA_DONE   = 3; 
	    private static final int COMMIT_DATA_ERROR  = 4;
 
		public void handleMessage(Message msg) {        	
        	
        	switch (msg.arg1) { 
    		case SERVER_UNAVAILABLE:
    			onServerUnavailable();
    			break;
            case UPDATE_DATA_DONE:
            	onUpdateDataDone();	            	
                break;   
            case UPDATE_DATA_ERROR:
            	onUpdateDataError();
            	break;
            case COMMIT_DATA_DONE:
            	onCommitDataDone();
            	break;
            case COMMIT_DATA_ERROR:
            	onCommitDataError();
            	break;
            default:
            	break;
            }            
        } 
    };
    
    private void onServerUnavailable() {
    	Toast.makeText(getApplicationContext(), 
            "Sorry, server can not be connected. Please try again.",
            Toast.LENGTH_LONG).show();
    }
    
    private void onUpdateDataDone() {
    	String newAccount = mEditTextAccount.getText().toString();    	
    	
    	for (int i = 0; i < mNames.size(); i++) {
    		String existAccount = mNames.get(i);
    		if (existAccount.compareTo(newAccount) == 0) { // already exist
    			Toast.makeText(getApplicationContext(), 
                   "Sorry, the account name has been used.", Toast.LENGTH_LONG).show();
    			return;
    		}
    	}
    	
    	doSignUp(newAccount);
    }       
    
    private void onUpdateDataError() {
    	// no names are found, just register
    	String newAccount = mEditTextAccount.getText().toString();
    	
    	doSignUp(newAccount);
    }

    private void onCommitDataDone() {
    	String newAccount = mEditTextAccount.getText().toString();
    	
    	Intent i = new Intent(this, PBMain.class);
		i.putExtra(ACCOUNT_NAME, newAccount);
		finish();
		startActivity(i);  
    }
    
    private void onCommitDataError() {
    	Toast.makeText(getApplicationContext(), 
                "Sorry, can't sign up now.", Toast.LENGTH_LONG).show();
    }

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_signup);
		
		mEditTextAccount = (EditText)findViewById(R.id.pbsignup_account_name_input);
		
		// Set up click listeners for all the buttons
		View btn_signup = this.findViewById(R.id.pbsignup_signup_button);
		btn_signup.setOnClickListener(this);
		// create commit task
		mCommit = new CommitTask(mHandler, mNames);				
	}
	
	public void onClick(View v) {
		String account = mEditTextAccount.getText().toString();

		switch (v.getId()) {
		case R.id.pbsignup_signup_button:
			if (account.compareTo("") != 0) {
				mAcquire = new AcquireTask(mHandler, mNames, 0, true);
				mAcquire.start();
			} else {
				Toast.makeText(getApplicationContext(), 
						"Please enter an account name.", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	public void doSignUp(String newAccount) {
		// upload new account
		Log.d("PBSignUp:doSignUp", "Account name: " + newAccount);
		mNames.add(newAccount);
		
		PBPlayerInfo pInfo = new PBPlayerInfo(newAccount);
		pInfo.setStatus("Online");
		pInfo.setUpdateTime((new Date()).getTime());
		CommitTask cmt = new CommitTask(new Handler(), pInfo);
		cmt.execute();
		
		if (mCommit != null) { 
			mCommit.execute();
		}
		

	}
}
