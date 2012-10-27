package edu.neu.madcourse.binbo;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class PBMain extends Activity implements OnClickListener {
	private TextView mTextViewAccount;
	private String mAccount = "";
	private PBPlayerInfo mPlayer  = null;
	private AcquireTask  mAcquire = null;
	private CommitTask   mCommit  = null;
	private static final String ACCOUNT_NAME = "account_name";
	private static final String HOST_INFO = "host_info";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_main);

		Intent intent = getIntent();
		mAccount = intent.getStringExtra(ACCOUNT_NAME);
		mTextViewAccount = (TextView)findViewById(R.id.pbmain_account_name_label);
		mTextViewAccount.setText("Hi, " + mAccount);
		
		// Set up click listeners for all the buttons
		View btnNewGame = findViewById(R.id.pbmain_new_game_button);
		btnNewGame.setOnClickListener(this);
		View btnRank = findViewById(R.id.pbmain_rank_button);
		btnRank.setOnClickListener(this);
		View btnOptions = findViewById(R.id.pbmain_options_button);
		btnOptions.setOnClickListener(this);
		View btnAbout = findViewById(R.id.pbmain_about_button);
		btnAbout.setOnClickListener(this);
		View btnExit = findViewById(R.id.pbmain_exit_button);
		btnExit.setOnClickListener(this);
		
		mPlayer = new PBPlayerInfo(mAccount);
		mCommit = new CommitTask(mHandler, mPlayer);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.pbmain_new_game_button:				
				startNewGame();
				break;
			case R.id.pbmain_exit_button:
				exitGame();
				break;
		}
	}
	
	private void startNewGame() {			
		mAcquire = new AcquireTask(mHandler, mPlayer, 0, true);
		mAcquire.start();		
	}
	
	private void exitGame() {
		this.mPlayer.setStatus("Offline");
		this.mPlayer.setUpdateTime((new Date()).getTime());
		CommitTask cmt = new CommitTask(this.mHandler, this.mPlayer);
		cmt.execute();
	}
	
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {
		
		private static final int SERVER_UNAVAILABLE = -1;
		private static final int UPDATE_DATA_DONE   = 1;  
		private static final int UPDATE_DATA_ERROR  = 2;
		private static final int COMMIT_DATA_DONE   = 3; 
	    private static final int COMMIT_DATA_ERROR  = 4;
 
		public void handleMessage(Message msg) {        	
        	
        	switch (msg.arg1) { 
    		case SERVER_UNAVAILABLE:
    			onServerUnavailable();
    			break;
            case UPDATE_DATA_DONE:
            	onUpdateDataDone();	            	
                break;   
            case UPDATE_DATA_ERROR:
            	onUpdateDataError();
            	break;
            case COMMIT_DATA_DONE:
            	onCommitDataDone();
            	break;
            case COMMIT_DATA_ERROR:
            	onCommitDataError();
            	break;
            default:
            	break;
            }            
        } 
		
		private void onServerUnavailable() {
	    	Toast.makeText(getApplicationContext(), 
	            "Sorry, server can not be connected. Please try again.",
	            Toast.LENGTH_LONG).show();
	    }
	    
	    private void onUpdateDataDone() {    	
	    	Intent i = new Intent(getApplicationContext(), PBInvite.class);
			try {
				mPlayer.setStatus("online"); // update the status to online
				mPlayer.setUpdateTime(System.currentTimeMillis());
				i.putExtra(HOST_INFO, mPlayer.obj2json());		
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			finish();	
			startActivity(i);
	    }       
	    
	    private void onUpdateDataError() {    	
	    	// not necessary, already create player info when sign up and log in
	    	
	    	// no player info found, just commit a new one
	    	//mPlayer.setScore(0);
	    	//mPlayer.setBestScore(0);
	    	//mPlayer.setSelLetters("");
	    	//mPlayer.setStatus("online");
	    	//mPlayer.setUpdateTime(System.currentTimeMillis());
	    	
	    	// create commit task
			//mCommit.execute();
	    }

	    private void onCommitDataDone() {
			// Intent i = new Intent(getApplicationContext(), PBInvite.class);
			// try {
			// i.putExtra(HOST_INFO, mPlayer.obj2json());
			// } catch (JSONException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// startActivity(i);
	    	
	    	Intent i = new Intent(getApplicationContext(), HelloMAD.class);
	    	finish();
	    	startActivity(i);
	    }
	    
	    private void onCommitDataError() {
	    	Toast.makeText(getApplicationContext(), 
	            "Fail to start the game. Please try again.",
	            Toast.LENGTH_LONG).show();
	    }
    };
}
