package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PBGame extends Activity implements IBoggleGame, OnClickListener, OnTouchListener {
	private static final String TAG = "Persistent Boggle";	
	private static final int DEFAULT_GAME_TIME = 179;
	private static final int ACCEL_ACCURACY = 13;
	private static final float GOLDEN_DIVIDE = 0.618f;
	
	private NativeDictionary mDict = null;
	private BogglePuzzle mPuzzle = null;
	private BogglePuzzleView mPuzzleView = null;
	private SensorManager mSensorMgr = null;	
	private ToneGenerator mToneGen = null;	
	private List<String> mWordsFound = new ArrayList<String>();
	private final String mHighPrequency[] = { 
		"a", "e", "i", "l", "n", "o", "r", "s", "t"
	};
	
	private TextView mTextViewName1   = null;	
	private TextView mTextViewName2   = null;
	private TextView mTextViewScore1  = null;	
	private TextView mTextViewScore2  = null;
	private TextView mTextViewStatus1 = null;	
	private TextView mTextViewStatus2 = null;
	private TextView mTextViewTime    = null;
	private TextView mTextViewBest    = null;
	private Button   mButtonPause     = null;
	private Button   mButtonShake     = null;
	private ListView mListViewWords   = null;
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);				
		// do not show title bar and change to full screen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
		// set views from xml layout
		setContentView(R.layout.pb_game);
		// disable screen rotation on this version
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		// initialize views
		initViews();
		// make a new puzzle
		mPuzzle = new BogglePuzzle(this, 6); // should get the size from the option
		// adjust layouts according to the screen resolution
		DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (dm.heightPixels >= dm.widthPixels) {
			adjustPortraitLayout(dm.widthPixels, dm.heightPixels);
		} else if (dm.widthPixels > dm.heightPixels) {
			adjustLandscapeLayout(dm.widthPixels, dm.heightPixels);
		}
		// load dictionaries
		loadDictionaries();
		// initialize tone generator
		mToneGen = new ToneGenerator(AudioManager.STREAM_MUSIC, 70);
		// initialize sensors
		initSensors();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {		
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {	
		case R.id.pbgame_buttonPause:
			//pauseGame(!paused);			
			break;
		}
	}
	
	public boolean onTouch(View v, MotionEvent event) {  
        if (v.getId() == R.id.pbgame_buttonShake) { 
            if (event.getAction() == MotionEvent.ACTION_DOWN){  
                Log.d(TAG, "shake button ---> down");  
                if (mSensorMgr != null) {
                	int sensorType = Sensor.TYPE_ACCELEROMETER;
                	mSensorMgr.registerListener(
                		boggleAccelerometerListener,
                		mSensorMgr.getDefaultSensor(sensorType),
                		SensorManager.SENSOR_DELAY_NORMAL
                	);                 	
                }
            }   
            if (event.getAction() == MotionEvent.ACTION_UP){  
                Log.d(TAG, "shake button ---> up");  
                if (mSensorMgr != null) {
                	mSensorMgr.unregisterListener(boggleAccelerometerListener);                
                }
            }  
        }  
        return false;  
    }  
	
	private void initViews() {
		// get views
		mTextViewName1   = (TextView)findViewById(R.id.pbgame_textViewName1);
		mTextViewName2   = (TextView)findViewById(R.id.pbgame_textViewName2);
		mTextViewScore1  = (TextView)findViewById(R.id.pbgame_textViewScore1);
		mTextViewScore2  = (TextView)findViewById(R.id.pbgame_textViewScore2);
		mTextViewStatus1 = (TextView)findViewById(R.id.pbgame_textViewStatus1);
		mTextViewStatus2 = (TextView)findViewById(R.id.pbgame_textViewStatus2);
		mTextViewTime    = (TextView)findViewById(R.id.pbgame_textViewTime);
		mTextViewBest    = (TextView)findViewById(R.id.pbgame_textViewBest);		
		mListViewWords   = (ListView)findViewById(R.id.pbgame_listViewWords);
		mButtonShake     = (Button)findViewById(R.id.pbgame_buttonShake); 		
 		mButtonPause     = (Button)findViewById(R.id.pbgame_buttonPause);
 		// register events handler
 		mButtonShake.setOnTouchListener(this);
 		mButtonPause.setOnClickListener(this);
	}	

	protected void loadDictionaries() {
		// load dictionaries of high frequency
		mDict = new NativeDictionary(getAssets());
        for (int i = 0; i < mHighPrequency.length; ++i) {        	
        	// use ".mpg" to make sure that android won't consider the files in the apk
        	// as the compressed files, or it won't be available to get a valid native fd.
        	// these files have been compressed to huffman coding.
        	// due to the singleton pattern used in NDK code, if the corresponding wordlist
        	// has been loaded before, it won't be loaded again when the activity is recreated.
        	String dictName = "" + mHighPrequency[i];
        	if (!mDict.isLoaded(dictName)) {
        		mDict.load("wordlist_" + dictName + ".mpg", dictName, getAssets());
        	}
        } 
	}
	
	protected void adjustPortraitLayout(int width, int height) {
		// adjust the layout according to the screen resolution
		LinearLayout llRoot = (LinearLayout)findViewById(R.id.linearLayoutPBGameRoot);
		
		if (mPuzzleView == null) {
			mPuzzleView = new BogglePuzzleView(this, mPuzzle);
		}				
		llRoot.addView(mPuzzleView);				
      
        LayoutParams laParams = null;
        LinearLayout llSubRoot = (LinearLayout)findViewById(R.id.linearLayoutPBGameSubRoot);
        laParams = llSubRoot.getLayoutParams();
        laParams.height = height - width;
        llSubRoot.setLayoutParams(laParams);
        
        LinearLayout llInfoTime = (LinearLayout)findViewById(R.id.linearLayoutPBGameInfoTime);
        laParams = llInfoTime.getLayoutParams();
        laParams.height = (int)((height - width) * 0.5);
        llInfoTime.setLayoutParams(laParams);               
        
        LinearLayout llPlayer1Info = (LinearLayout)findViewById(R.id.linearLayoutP1Info);
        laParams = llPlayer1Info.getLayoutParams();
        laParams.width = (int)(width * 0.36);
        llPlayer1Info.setLayoutParams(laParams);
        
        LinearLayout llTime = (LinearLayout)findViewById(R.id.linearLayoutTime);
        laParams = llTime.getLayoutParams();
        laParams.width = (int)(width * 0.28);
        llTime.setLayoutParams(laParams);
        
        LinearLayout llPlayer2Info = (LinearLayout)findViewById(R.id.linearLayoutP2Info);
        laParams = llPlayer2Info.getLayoutParams();
        laParams.width = (int)(width * 0.36);
        llPlayer2Info.setLayoutParams(laParams);
        
        laParams = mListViewWords.getLayoutParams();
        laParams.width = (int)(width * GOLDEN_DIVIDE);        
        
        LinearLayout llControl = (LinearLayout)findViewById(R.id.linearLayoutControl);
        laParams = llControl.getLayoutParams();
        laParams.height = (int)(width * (1 - GOLDEN_DIVIDE));
        llControl.setLayoutParams(laParams);
	}
	
	protected void adjustLandscapeLayout(int width, int height) {
		// force the screen to portrait on this version
		// so do nothing here now.
	}

	protected void initSensors() {
		// get system sensor manager to deal with sensor issues  
        mSensorMgr = (SensorManager)getSystemService(Context.SENSOR_SERVICE);                              
	}

	/* 
     * SensorEventListener implement
     * method1: onSensorChanged 
     * method2: onAccuracyChanged 
     * */  
    final SensorEventListener boggleAccelerometerListener = new SensorEventListener() {  
           
        public void onSensorChanged(SensorEvent sensorEvent){  
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){  
                Log.i(TAG, "onSensorChanged");  
 
                float X_lateral = sensorEvent.values[0];  
                float Y_longitudinal = sensorEvent.values[1];  
                float Z_vertical = sensorEvent.values[2];                
                //Log.i(TAG,"\n heading " + X_lateral);  
                //Log.i(TAG,"\n pitch " + Y_longitudinal);  
                //Log.i(TAG,"\n roll " + Z_vertical);
                double offset = Math.sqrt(X_lateral * X_lateral + 
          			  					    Y_longitudinal * Y_longitudinal + 
          			  					    Z_vertical * Z_vertical);
                if (offset >= ACCEL_ACCURACY) {
                	mPuzzle.rotatePuzzle();
                	mPuzzleView.rotatePuzzle();
                }
            }  
        }  
 
        public void onAccuracyChanged(Sensor sensor, int accuracy){  
            Log.i(TAG, "onAccuracyChanged");  
        }  
    };
    
    
    
    // IBoggleGame    
    public boolean isGamePaused() {
		return false;
	}
	
	public boolean isGameOver() {
		return false;
	}

	public boolean lookUpWord(String word) {
		String dictName = "" + word.charAt(0);
		
		if (!mDict.isLoaded(dictName)) {
			mDict.load("wordlist_" + dictName + ".mpg", dictName, getAssets());			
		} 
		
		if (mWordsFound.contains(word)) {
			Toast.makeText(this, "Oops! Repeated!", Toast.LENGTH_SHORT).show();
			return false;
		}
				
		if (mDict.lookupWord(word)) {
			mWordsFound.add(word);
			mListViewWords.setAdapter(
	        	new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mWordsFound)
	        );			
			int bonus = 1;
			switch (word.length()) {			
			case 3: bonus = 1; break;
			case 4: bonus = 2; break;
			case 5: bonus = 4; break;
			case 6: bonus = 6; break;
			default: bonus = 10; break;
			}			
			//game_score += bonus		
			String toastText = "";
			if (bonus <= 1) {
				toastText = "Good! +";
			} else if (bonus > 1 && bonus <= 4) {
				toastText = "Great! +";
			} else {
				toastText = "Excellent! +";
			}
			// special sound
			if (mToneGen != null) {
				mToneGen.startTone(ToneGenerator.TONE_CDMA_CALL_SIGNAL_ISDN_NORMAL, 100);
    		}
			Toast.makeText(this, toastText + bonus, Toast.LENGTH_SHORT).show();			
			return true;
		}
		
		return false;
	}

}
