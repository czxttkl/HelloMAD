package edu.neu.madcourse.binbo;

import java.util.ArrayList;

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
	private ArrayList<PBPlayerInfo> mInfos = new ArrayList<PBPlayerInfo>();
	private PlayerInfoAcquirer  mAcquirer  = null;
	private PlayerInfoCommitter mCommitter = null;
	private static final String ACCOUNT_NAME = "account_name";
	
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {
		private static final int SERVER_UNAVAILABLE   = -1;
    	private static final int UPDATE_PLAYERS_INFO  = 0; // get info successfully
        private static final int UPDATE_PLAYERS_ERROR = 1; // fail to get info         
        private static final int COMMIT_PLAYERS_INFO  = 2; // commit info successfully 
        private static final int COMMIT_PLAYERS_ERROR = 3; // fail to commit info        
 
        @SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {        	
        	
        	switch (msg.arg1) { 
        		case SERVER_UNAVAILABLE:
        			onServerUnavailable();
        			break;
	            case UPDATE_PLAYERS_INFO:
	            	onUpdatePlayersInfo((ArrayList<PBPlayerInfo>)msg.obj);	            	
	                break;  
	            case UPDATE_PLAYERS_ERROR:  
	            	onUpdatePlayersInfoError();
	                break;  
	            case COMMIT_PLAYERS_INFO:
	            	onCommitPlayersInfo();
	            	break;
	            case COMMIT_PLAYERS_ERROR:
	            	onCommitPlayersInfoError();
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
    
    private void onUpdatePlayersInfo(ArrayList<PBPlayerInfo> infos) {
    	mInfos = infos;
    	String newAccount = mEditTextAccount.getText().toString();
    	
    	for (int i = 0; i < mInfos.size(); i++) {
    		String existAccount = mInfos.get(i).getName();
    		if (existAccount.compareTo(newAccount) == 0) { // already exist
    			Toast.makeText(getApplicationContext(), 
                   "Sorry, the account name has been used.", Toast.LENGTH_LONG).show();
    			return;
    		}
    	}
    	
    	doSignUp(newAccount);
    }
    
    private void onUpdatePlayersInfoError() {
    	// no user found, just do registration
    	String newAccount = mEditTextAccount.getText().toString();
    	doSignUp(newAccount);
    }
    
    private void onCommitPlayersInfo() {
    	String newAccount = mEditTextAccount.getText().toString();
    	
    	Intent i = new Intent(this, PBMain.class);
		i.putExtra(ACCOUNT_NAME, newAccount);
		finish();
		startActivity(i);  
    }
    
    private void onCommitPlayersInfoError() {
    	Toast.makeText(getApplicationContext(), 
    			"Sorry, unable to submit your info.",
    			Toast.LENGTH_LONG).show(); 
    }
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_signup);
		
		mEditTextAccount = (EditText) this.findViewById(R.id.pbsignup_account_name_input);
		
		// Set up click listeners for all the buttons
		View btn_signup = this.findViewById(R.id.pbsignup_signup_button);
		btn_signup.setOnClickListener(this);
		
		// create acquirer and committer		
		mCommitter = new PlayerInfoCommitter(mHandler); 
	}
	
	public void onClick(View v) {
		String account = mEditTextAccount.getText().toString();

		switch (v.getId()) {
		case R.id.pbsignup_signup_button:
			if (account.compareTo("") != 0) {
				if (mAcquirer != null) {				
					mAcquirer.end();
				}
				mAcquirer = new PlayerInfoAcquirer(mHandler, true);
				mAcquirer.start();
			} else {
				Toast.makeText(getApplicationContext(), 
						"Please enter an account name.", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
	
	@SuppressWarnings("unchecked")
	public void doSignUp(String newAccount) {
		// upload new account
		PBPlayerInfo newPlayer = new PBPlayerInfo();
		newPlayer.setName(newAccount);
		Log.d("PBSignUp:doSignUp", "Account name: " + newAccount);
		mInfos.add(newPlayer);
		
		if (mCommitter != null) { 
			mCommitter.execute(mInfos);
		}
	}
}
