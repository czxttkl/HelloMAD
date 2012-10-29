package edu.neu.madcourse.binbo;

import org.json.JSONException;

import edu.neu.mobileclass.apis.KeyValueAPI;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class PBMain extends Activity implements OnClickListener {
	private TextView mTextViewAccount;
	private PBPlayerInfo mPlayer  = new PBPlayerInfo("");
	public static final String HOST_INFO = "host_info";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_main);
		
		//KeyValueAPI.clear("MAD_WB_TEAM", "111111");

		Intent intent = getIntent();		
		try {
			mPlayer.json2obj(intent.getStringExtra(HOST_INFO));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); 
			// ignore this error
		}
		
		mTextViewAccount = (TextView)findViewById(R.id.pbmain_account_name_label);
		mTextViewAccount.setText("Hi, " + mPlayer.getName());
		
		// Set up click listeners for all the buttons
		View btnNewGame = findViewById(R.id.pbmain_new_game_button);
		btnNewGame.setOnClickListener(this);
		View btnOptions = findViewById(R.id.pbmain_options_button);
		btnOptions.setOnClickListener(this);
		View btnAbout = findViewById(R.id.pbmain_about_button);
		btnAbout.setOnClickListener(this);
		View btnExit = findViewById(R.id.pbmain_exit_button);
		btnExit.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.pbmain_new_game_button:				
				startNewGame();
				break;
			case R.id.pbmain_about_button:
				startActivity(new Intent(this, PBAbout.class));
				break;
//			case R.id.pbmain_options_button:
//				startActivity(new Intent(this, PBPrefs.class));
//				break;
			case R.id.pbmain_exit_button:
				exitGame();
				break;
		}
	}
	
	private void startNewGame() {			

		try {
			mPlayer.setStatus("online"); // update the status to online
			mPlayer.setUpdateTime(System.currentTimeMillis());		
			
			Intent i = new Intent(this, PBInvite.class);
			i.putExtra(PBMain.HOST_INFO, mPlayer.obj2json());
			finish();
    		startActivity(i);
		} 
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(this, 
               "Sorry, can't start the new game.", Toast.LENGTH_LONG).show();	  	    
		} 
		
//		Intent intent = new Intent(this, PBGame.class);
//		Bundle bundle = new Bundle();  
//	    bundle.putBoolean("new game", true);		    
//	    bundle.putSerializable(HOST_INFO, mPlayer);
//	    PBPlayerInfo oppo = new PBPlayerInfo("bigbug");
//	    bundle.putSerializable(PBInvite.OPPONENT_INFO, oppo); 
//	    intent.putExtras(bundle);
//	    finish();
//	    startActivity(intent);
	}
	
	private void exitGame() {
		mPlayer.setStatus("offline");
		mPlayer.setUpdateTime(System.currentTimeMillis());		
		
		try {
			if (mPlayer.commit() == false) {
				// do nothing because you don't want to bother the user
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finish(); // just close the activity	
	}					
}	
