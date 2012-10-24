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
	private EditText mETAccountName;
	private ArrayList<PBPlayerInfo> mPlayerInfoList;
	private PlayerInfoAcquirer mAcquirer = null;
	private static final String ACCOUNT_NAME = "account_name";
	
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {
    	private static final int UPDATE_PLAYERS_INFO  = 0;   
        private static final int UPDATE_PLAYERS_ERROR = 1;
 
        @SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
        	if (mAcquirer != null) {
    			mAcquirer.end();
    			mAcquirer = null;
    		}
        	switch (msg.arg1) { 
	            case UPDATE_PLAYERS_INFO:                 
	            	String strName = mETAccountName.getText().toString();
	            	mPlayerInfoList = (ArrayList<PBPlayerInfo>)msg.obj;
	            	for (int i = 0; i < mPlayerInfoList.size(); i++) {
	            		if (mPlayerInfoList.get(i).getName().toString().equals(strName)) {
	            			goToPBMain(strName);
	            			return;
	            		}
	            	}
	            	Toast.makeText(getApplicationContext(), 
		                       "Sorry, the account name doesn't exist.",
		                       Toast.LENGTH_LONG).show();
	                break;  
	            case UPDATE_PLAYERS_ERROR:  
	            	if (msg.arg2 == 1) {
	            		Toast.makeText(getApplicationContext(), 
			                       "Sorry, the account name doesn't exist.",
			                       Toast.LENGTH_LONG).show();
	            	} else {
	            		Toast.makeText(getApplicationContext(), 
			                       "Server connection lost during downloading data.",
			                       Toast.LENGTH_LONG).show();
	            	}
	                break;
	            default:
	            	break;
            }
            
        } 
    };
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_login);
		
		this.mETAccountName = (EditText) this.findViewById(R.id.pblogin_account_name_input);
		
		// Set up click listeners for all the buttons
		View btn_login = this.findViewById(R.id.pblogin_login_button);
		btn_login.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.pblogin_login_button:
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
	
	public void goToPBMain(String accName) {
		Intent i = new Intent(this, PBMain.class);
		i.putExtra(ACCOUNT_NAME, accName);
		finish();
		startActivity(i);
	}
}
