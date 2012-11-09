package edu.neu.madcourse.binbo.persistentboggle;

import java.util.Date;
import org.json.JSONException;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.R.id;
import edu.neu.madcourse.binbo.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PBLogIn extends Activity implements OnClickListener {
	private EditText mEditTextAccount = null;
	private PBNameList mNames = new PBNameList();
	private static final String ACCOUNT_NAME = "account_name";	
     
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_login);
		
		mEditTextAccount = (EditText)findViewById(R.id.pblogin_account_name_input);
		
		// Set up click listeners for all the buttons
		View btn_login = findViewById(R.id.pblogin_login_button);
		btn_login.setOnClickListener(this);
	}
	
	public void onClick(View v) {				
		switch (v.getId()) {
		case R.id.pblogin_login_button:
			doLogin();
			break;
		}
	}
	
	public void doLogin() {
		String account = mEditTextAccount.getText().toString();
		
		if (account.compareTo("") == 0) {
			Toast.makeText(this, 
				"Please enter an account name.", Toast.LENGTH_LONG).show();
			return;
		}
		
		PBPlayerInfo newPlayer = new PBPlayerInfo(account);
		
		try {
			if (mNames.acquire() == false) {
				Toast.makeText(this, 
		            "Sorry, server can not be connected. Please try again.",
		            Toast.LENGTH_LONG).show();
				return;
			} 
			
			boolean found = false;			
	    	for (int i = 0; i < mNames.size(); i++) {
	    		String existAccount = mNames.get(i);
	    		if (existAccount.compareTo(account) == 0) { // if exists, then login
	    			found = true;
	    			break;
	    		}
	    	}
	    	
	    	if (found) {		    		
	    		// not necessary to do this commit, but we do it now to prevent error 
	    		newPlayer.acquire();
	    		newPlayer.setStatus("offline");
	    		newPlayer.setUpdateTime((new Date()).getTime());
	    		if (newPlayer.commit() == false) {
	    			Toast.makeText(this, 
    		            "Sorry, server can not be connected. Please try again.",
    		            Toast.LENGTH_LONG).show();
	    		} else {
	    			Intent i = new Intent(this, PBMain.class);
	    			i.putExtra(PBMain.HOST_INFO, newPlayer.obj2json());
		    		finish();
		    		startActivity(i);
	    		}		    				    			
	    	} else {
		    	Toast.makeText(getApplicationContext(), 
	               "Sorry, the account name doesn't exist. Please sign up first.",
	               Toast.LENGTH_LONG).show();	  
		    	return;
	    	}    				
		} catch (JSONException e) {
			Toast.makeText(getApplicationContext(), 
                "Sorry, the account name doesn't exist. Please login first.",
                Toast.LENGTH_LONG).show();
		}						
	}
}
