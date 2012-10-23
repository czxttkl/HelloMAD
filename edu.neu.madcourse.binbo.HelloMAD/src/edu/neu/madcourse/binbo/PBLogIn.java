package edu.neu.madcourse.binbo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class PBLogIn extends Activity implements OnClickListener {
	private EditText mETAccountName;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_login);
		
		this.mETAccountName = (EditText) this.findViewById(R.id.pblogin_account_name_input);
		
		// Set up click listeners for all the buttons
		View btn_login = this.findViewById(R.id.pblogin_login_button);
		btn_login.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
			case R.id.pblogin_login_button:
				//i = new Intent(this, PBLogIn.class);
				//startActivity(i);
				break;
		}
	}
}
