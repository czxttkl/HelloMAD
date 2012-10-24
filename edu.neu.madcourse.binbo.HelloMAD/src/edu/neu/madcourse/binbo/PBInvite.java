package edu.neu.madcourse.binbo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PBInvite extends Activity implements OnClickListener  {
	
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_invite);
		
//		this.mETAccountName = (EditText) this.findViewById(R.id.pbsignup_account_name_input);
//		
//		// Set up click listeners for all the buttons
//		View btn_signup = this.findViewById(R.id.pbsignup_signup_button);
//		btn_signup.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		Intent i = null;

//		switch (v.getId()) {
//			case R.id.pbsignup_signup_button:
//				if (mETAccountName.getText().toString() != "") {
//					if (mAcquirer == null) {
//						mAcquirer = new PlayerInfoAcquirer(mHandler, true);
//						mAcquirer.start();
//					}
//				} else {
//					Toast.makeText(getApplicationContext(), 
//		                       "Please enter an account name.",
//		                       Toast.LENGTH_LONG).show();
//				}
//				break;
//		}
	}
}
