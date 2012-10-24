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
	private EditText mETAccountName;
	private ArrayList<PBPlayerInfo> mPlayerInfoList = new ArrayList<PBPlayerInfo>();
	private PlayerInfoAcquirer mAcquirer = null;
	private static final String ACCOUNT_NAME = "account_name";
	
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {
    	private static final int UPDATE_PLAYERS_INFO  = 0;   
        private static final int UPDATE_PLAYERS_ERROR = 1;
        private static final int COMMIT_PLAYERS_ERROR = 2;
 
        @SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
        	if (mAcquirer != null) {
    			mAcquirer.end();
    			mAcquirer = null;
    		}
        	String strName = mETAccountName.getText().toString();
        	switch (msg.arg1) { 
	            case UPDATE_PLAYERS_INFO:
	            	mPlayerInfoList = (ArrayList<PBPlayerInfo>)msg.obj;
	            	for (int i = 0; i < mPlayerInfoList.size(); i++) {
	            		if (mPlayerInfoList.get(i).getName() == strName) {
	            			Toast.makeText(getApplicationContext(), 
	 		                       "Sorry, the account name has been used.",
	 		                       Toast.LENGTH_LONG).show();
	            			return;
	            		}
	            	}
	            	doSignUp(strName);
	                break;  
	            case UPDATE_PLAYERS_ERROR:  
	            	if (msg.arg2 == 1) {
	            		doSignUp(strName);
	            	} else {
	            		Toast.makeText(getApplicationContext(), 
			                       "Server connection lost during downloading data.",
			                       Toast.LENGTH_LONG).show();
	            	}
	                break;  
	            case COMMIT_PLAYERS_ERROR:  
	            	Toast.makeText(getApplicationContext(), 
	                       "Server connection lost during committing data.",
	                       Toast.LENGTH_LONG).show();                                                       
	            	break;
	            default:
	            	break;
            }
            
        } 
    };
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_signup);
		
		this.mETAccountName = (EditText) this.findViewById(R.id.pbsignup_account_name_input);
		
		// Set up click listeners for all the buttons
		View btn_signup = this.findViewById(R.id.pbsignup_signup_button);
		btn_signup.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
			case R.id.pbsignup_signup_button:
				if (mETAccountName.getText().toString() != "") {
					if (mAcquirer == null) {
						mAcquirer = new PlayerInfoAcquirer(mHandler, true);
						mAcquirer.start();
					}
				} else {
					Toast.makeText(getApplicationContext(), 
		                       "Please enter an account name.",
		                       Toast.LENGTH_LONG).show();
				}
				break;
		}
	}
	
	public void doSignUp(String accName) {
		//upload data
		ServerDelegator d = new ServerDelegator();
		PBPlayerInfo newPlayer = new PBPlayerInfo();
		newPlayer.setName(accName);
		Log.d("PBSignUp:doSignUp", "Acc name: " + accName);
		this.mPlayerInfoList.add(newPlayer);
		try {
			d.commitPlayersInfo(this.mPlayerInfoList);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Intent i = new Intent(this, PBMain.class);
		i.putExtra(ACCOUNT_NAME, accName);
		finish();
		startActivity(i);
	}
}
