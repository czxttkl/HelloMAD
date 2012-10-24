package edu.neu.madcourse.binbo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class PBMain extends Activity {
	private TextView mTVAccName;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_main);

		Intent intent = getIntent();
		String strName = intent.getStringExtra("account_name");
		this.mTVAccName = (TextView) this.findViewById(R.id.pbmain_account_name_label);
		this.mTVAccName.setText("Hi, " + strName);
		
		// Set up click listeners for all the buttons
		//View btn_signup = this.findViewById(R.id.pbsignup_signup_button);
		//btn_signup.setOnClickListener(this);
	}
	
	public void onClick(View v) {
//		Intent i = null;
//
//		switch (v.getId()) {
//			case R.id.pbsignup_signup_button:
//				//i = new Intent(this, PBLogIn.class);
//				//startActivity(i);
//				break;
//		}
	}
}
