package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PBLogIn extends Activity implements OnClickListener {
	private EditText mEditTextAccount = null;
	private PBNameList   mNames   = new PBNameList();
	private AcquireTask  mAcquire = null;
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
    	String account = mEditTextAccount.getText().toString();    			
		boolean found = false;
		
    	for (int i = 0; i < mNames.size(); i++) {
    		String existAccount = mNames.get(i);
    		if (existAccount.compareTo(account) == 0) { // if exists, then login
    			found = true;
    			break;
    		}
    	}
    	
    	if (found) {
    		doLogin(account);
    	} else {
	    	Toast.makeText(getApplicationContext(), 
               "Sorry, the account name doesn't exist. Please login first.",
               Toast.LENGTH_LONG).show();	    	
    	}    	
    }       
    
    private void onUpdateDataError() {    	
    	Toast.makeText(getApplicationContext(), 
                "Sorry, the account name doesn't exist. Please login first.",
                Toast.LENGTH_LONG).show();
    }

    private void onCommitDataDone() {
  
    }
    
    private void onCommitDataError() {

    }
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_login);
		
		mEditTextAccount = (EditText)findViewById(R.id.pblogin_account_name_input);
		
		// Set up click listeners for all the buttons
		View btn_login = findViewById(R.id.pblogin_login_button);
		btn_login.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		String account = mEditTextAccount.getText().toString();
		
		switch (v.getId()) {
		case R.id.pblogin_login_button:
			if (account.compareTo("") != 0) {
				mAcquire = new AcquireTask(mHandler, mNames, 0, true);
				mAcquire.start();
			} else {
				Toast.makeText(getApplicationContext(), 
	                       "Please enter an account name.",
	                       Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
	
	public void doLogin(String account) {
		Intent i = new Intent(this, PBMain.class);
		i.putExtra(ACCOUNT_NAME, account);
		finish();
		startActivity(i);					
	}

}
