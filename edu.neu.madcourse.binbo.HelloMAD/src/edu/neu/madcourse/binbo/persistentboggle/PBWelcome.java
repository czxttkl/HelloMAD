package edu.neu.madcourse.binbo.persistentboggle;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.R.id;
import edu.neu.madcourse.binbo.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class PBWelcome extends Activity implements OnClickListener {
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.pb_welcome);
		
		// Set up click listeners for all the buttons
		View btn_login = this.findViewById(R.id.pbwelcome_login_button);
		btn_login.setOnClickListener(this);
		View btn_signup = this.findViewById(R.id.pbwelcome_signup_button);
		btn_signup.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
			case R.id.pbwelcome_login_button:
				i = new Intent(this, PBLogIn.class);
				finish();
				startActivity(i);
				break;
			case R.id.pbwelcome_signup_button:
				i = new Intent(this, PBSignUp.class);
				finish();
				startActivity(i);
				break;
		}
	}
}
