package edu.neu.madcourse.binbo;

import java.util.Date;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class PBResult extends Activity implements OnClickListener {
//	private EditText mEditTextAccount = null;
//	private PBNameList mNames = new PBNameList();
//	private static final String ACCOUNT_NAME = "account_name";		
	public static final String HOST_INFO     = "host_info";
	public static final String OPPONENT_INFO = "opponent_info";
	
	private PBPlayerInfo mHost = null;
	private PBPlayerInfo mOppo = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_result);
		
		// get players information
		mHost = (PBPlayerInfo)getIntent().getSerializableExtra(PBInvite.HOST_INFO);
		mOppo = (PBPlayerInfo)getIntent().getSerializableExtra(PBInvite.OPPONENT_INFO);
		//mEditTextAccount = (EditText)findViewById(R.id.pbsignup_account_name_input);
		
		// Set up click listeners for all the buttons
		View continueButton = this.findViewById(R.id.pbresult_buttonContinue);
		View quitButton = this.findViewById(R.id.pbresult_buttonQuit);
		
		continueButton.setOnClickListener(this);
		quitButton.setOnClickListener(this);					
	}
	
	public void onClick(View v) {				
		switch (v.getId()) {
		case R.id.pbresult_buttonContinue:			
			continueToPlay();
			break;
		case R.id.pbresult_buttonQuit:
			quitGame();
			break;
		}
	}

	private void continueToPlay() {						
		try {
			mHost.setStatus("online");
			mHost.setScore(0);
			mHost.setSelLetters("");		
			mHost.setUpdateTime(System.currentTimeMillis());
			mHost.commit();
			
			Intent i = new Intent(this, PBInvite.class);
			i.putExtra(PBMain.HOST_INFO, mHost.obj2json());
			finish();
			startActivity(i);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, 
		       "Sorry, can't start the new game.", Toast.LENGTH_LONG).show();  	    
		}
	}
	
	private void quitGame() {
		try {
			mHost.setStatus("offline");
			mHost.setScore(0);
			mHost.setSelLetters("");		
			mHost.setUpdateTime(System.currentTimeMillis());
			mHost.commit();
			
			Intent i = new Intent(this, PBMain.class);
			i.putExtra(PBMain.HOST_INFO, mHost.obj2json());
			finish();
			startActivity(i);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, 
		       "Sorry, can't start the new game.", Toast.LENGTH_LONG).show();  	    
		}
	}
}
