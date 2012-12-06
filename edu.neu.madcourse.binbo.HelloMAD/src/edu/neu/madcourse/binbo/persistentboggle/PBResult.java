package edu.neu.madcourse.binbo.persistentboggle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.R.id;
import edu.neu.madcourse.binbo.R.layout;
import edu.neu.mobileclass.apis.KeyValueAPI;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class PBResult extends Activity implements OnClickListener {
//	private EditText mEditTextAccount = null;
//	private PBNameList mNames = new PBNameList();
//	private static final String ACCOUNT_NAME = "account_name";		
	public static final String HOST_INFO     = "host_info";
	public static final String OPPONENT_INFO = "opponent_info";
	
	private TableLayout mTable = null;
	
	private PBPlayerInfo mHost = null;
	private PBPlayerInfo mOppo = null;
	private PBResultList mResults = new PBResultList();
	
	private int mNext = 0;
	private AcquireTask mAcquire = null;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_result);
		
		// get players information
		mHost = (PBPlayerInfo)getIntent().getSerializableExtra(PBInvite.HOST_INFO);
		mOppo = (PBPlayerInfo)getIntent().getSerializableExtra(PBInvite.OPPONENT_INFO);
		//mEditTextAccount = (EditText)findViewById(R.id.pbsignup_account_name_input);
		
		mTable = (TableLayout)findViewById(R.id.tableLayoutResult);
		// Set up click listeners for all the buttons
		View continueButton = this.findViewById(R.id.pbresult_buttonContinue);
		View quitButton = this.findViewById(R.id.pbresult_buttonQuit);
		
		continueButton.setOnClickListener(this);
		quitButton.setOnClickListener(this);
						
		mAcquire = new AcquireTask(mHandler, mResults, 0, true);
		mAcquire.start();		
	}		
	
	@Override
	protected void onPause() {
		if (mAcquire != null) {
			mAcquire.end();
		}
		super.onPause();
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
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;		
		}
		
		return super.onKeyDown(keyCode, event);
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
            default:
            	break;
            }            
        } 
				
		private void onServerUnavailable() {	
			Toast.makeText(getApplicationContext(), 
				"Sorry, can't get results because the server can not work.",
				Toast.LENGTH_LONG).show();
			return;
	    }

	    private void onUpdateDataDone() {
	    	Collections.sort(mResults, Collections.reverseOrder());
	    	
	    	for (int i = 0; i < mResults.size(); ++i) {
	    		PBPlayerInfo player = mResults.get(i);
		    	TableRow tablerow = new TableRow(getApplicationContext());  
	            tablerow.setBackgroundColor(Color.rgb(222, 220, 210));  
	                              
	                TextView textViewRank = new TextView(getApplicationContext());  
	                textViewRank.setText(String.valueOf(i + 1));                
	                tablerow.addView(textViewRank);   
	                
	                TextView textViewName = new TextView(getApplicationContext());  
	                textViewName.setText(player.getName());                
	                tablerow.addView(textViewName);
	                
	                TextView textViewBest = new TextView(getApplicationContext());  
	                textViewBest.setText(String.valueOf(player.getBestScore()));                
	                tablerow.addView(textViewBest);
	              
	            mTable.addView(tablerow, new TableLayout.LayoutParams(  
	                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
	    	}         
	    }       
	    
	    private void onUpdateDataError() {   
	    	// normally it won't happen, do nothing here now
	    }
    };

    protected class PBResultList extends ArrayList<PBPlayerInfo> implements IRemoteData {
        private static final int DATA_ID = 10;
        
        public PBResultList() {
    	}
    		
    	public boolean commit() throws JSONException {	

    	    for (PBPlayerInfo player:this) {   
    	        if (!player.commit()) {
    	        	return false;
    	        }
    	    } 	    	
  
    	    return true;
    	}
    	
    	public boolean acquire() throws JSONException {
    		// get name list 
    		PBNameList names = new PBNameList();
    		if (!names.acquire()) {
    			return false;
    		}
    		// build player list
    		for (String name:names) {
    			this.add(new PBPlayerInfo(name));
    		}
    		// acquire data in the player list
    		for (PBPlayerInfo player:this) {
    			if (!player.acquire()) {
    				return false;
    			}
    		}
    			
    		return true;
    	}			
    	
    	public int getDataId() {
    		return DATA_ID;
    	}
    }
}
