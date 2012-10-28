package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.hardware.*;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.*;

public class PBGame extends Activity implements IBoggleGame, OnClickListener, OnTouchListener {
	private static final String TAG = "Persistent Boggle";	
	public  static final int DEFAULT_GAME_TIME = 30;
	private static final int ACCEL_ACCURACY = 13;
	private static final float GOLDEN_DIVIDE = 0.618f;
	private static final int DIALOG_QUIT    = 0;
	private static final int DIALOG_DROP_P1 = 1;
	private static final int DIALOG_DROP_P2 = 2;
	private static final int TIME_OUT_DROP = 15000;
	private static final String BOGGLE_WORDS = "words";
	private static final String BOGGLE_WORDS_SIZE = "words_size";
	
	private static final String SERVICE_COMMAND = "service_command";
	private static final int SERVICE_START = 1;
	private static final int SERVICE_END   = 2;
	private static final String PLAYER_NAMES = "player_names";	
	
	private NativeDictionary mDict = null;
	private BogglePuzzle mPuzzle = null;
	private BogglePuzzleView mPuzzleView = null;	
	private SensorManager mSensorMgr = null;	
	private ToneGenerator mToneGen = null;	
	private List<String> mWordsFound = new ArrayList<String>();
	
	private PBPlayerInfo  mHost = null;
	private PBPlayerInfo  mOppo = null;
	private AcquireTask   mAcquire = null;
	private AutoCommitter mAutoCommitter = null;
	
	private boolean mNew  = true;
	private boolean mQuit = false;
	private boolean[] mDrop = new boolean[2];
	private long mStartTime = 0;
	private long mCurTime = DEFAULT_GAME_TIME;
	private int mDefTextColor = Color.WHITE;

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
	private Button   mButtonQuit      = null;
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
		// get useful data from intent
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {			
			mNew = bundle.getBoolean("new game");
		}

		// get players information
		getPlayerInfos();			
		// do not show title bar and change to full screen mode
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
		// set views from xml layout
		setContentView(R.layout.pb_game);
		// disable screen rotation on this version
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
		// initialize views
		initViews();
		// load preferences
		loadPreferences();
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
		// create music player
		BoggleMusic.create(this, R.raw.boggle_game);
		if (mNew) BoggleMusic.reset();
		// set quit flag to false
		mQuit = false;
	}

	@Override
	protected void onDestroy() {
		BoggleMusic.stop(this);
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		savePreferences();
		BoggleMusic.pause();
		super.onPause();
	}

	@Override
	protected void onRestart() {
		mNew = false;
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		BoggleMusic.play();
		super.onResume();				
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// stop the service if it has been started
		Intent i = new Intent(this, MonitorService.class);
		Bundle bundle = new Bundle();  
	    bundle.putInt(SERVICE_COMMAND, SERVICE_END);	    
	    i.putExtras(bundle); 
		// it's ok to deal with this command for the first time
	    if (!mDrop[0]) {
	    	startService(i);
	    }				
	    // start data sessions
		mAcquire = new AcquireTask(mHandler, mOppo, 300, false);
		mAcquire.start();			
		mAutoCommitter = new AutoCommitter(mHandler, mHost, 300);
		mAutoCommitter.start();		
		// start timer
		mCurTime = mNew ? DEFAULT_GAME_TIME : 
			Math.max(0, (DEFAULT_GAME_TIME - (System.currentTimeMillis() - mStartTime) / 1000));
 	 	String minute = "" + mCurTime / 60;
    	String second = "" + mCurTime % 60;
    	if (second.length() == 1) {
    		second = "0" + second;
    	}
    	mTextViewTime.setText(minute + ":" + second);
    	if (mCurTime < 20) mHandlerFlash.postDelayed(mRunnableFlash, 500);
		mHandlerTimer.postDelayed(mRunnableTimer, 1000);
		// update views
		updateViews();
	}

	@Override
	protected void onStop() {
		// stop timer		
		mHandlerTimer.removeCallbacks(mRunnableTimer);
		// stop data sessions
		if (mAcquire != null) {
			mAcquire.end();
		}
		if (mAutoCommitter != null) {
			mAutoCommitter.end();
		}
		// start the service only when the user
		// has been interrupted or press the home key
		if (mQuit == false) { // game is not quit for these cases
			Intent i = new Intent(this, MonitorService.class);		 
		    Bundle bundle = new Bundle();  
		    bundle.putInt(SERVICE_COMMAND, SERVICE_START);  
		    ArrayList<String> players = new ArrayList<String>();
		    players.add(mHost.getName());
		    players.add(mOppo.getName());
		    bundle.putStringArrayList(PLAYER_NAMES, players);
		    i.putExtras(bundle); 		    
			startService(i);
		}
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
		case R.id.pbgame_buttonQuit:	
			showDialog(DIALOG_QUIT);
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
	
	private void getPlayerInfos() {
		mHost = (PBPlayerInfo)getIntent().getSerializableExtra(PBInvite.HOST_INFO);
		mOppo = (PBPlayerInfo)getIntent().getSerializableExtra(PBInvite.OPPONENT_INFO);
		
		mDrop[0] = false;
		mDrop[1] = false;
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
 		mButtonQuit      = (Button)findViewById(R.id.pbgame_buttonQuit);
 		// register events handler
 		mButtonShake.setOnTouchListener(this);
 		mButtonQuit.setOnClickListener(this);
 		// get default text view color 	
 	 	mDefTextColor = mTextViewTime.getCurrentTextColor(); 	 	
	}	
	
	protected void updateViews() {
		mTextViewName1.setText(mHost.getName());
    	mTextViewScore1.setText(String.valueOf(mHost.getScore()));
    	mTextViewStatus1.setText(mHost.getSelLetters());
    	mListViewWords.setAdapter(
        	new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, mWordsFound)
        );
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
			mPuzzleView.bindHost(mHost);
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
			mHost.setScore(mHost.getScore() + bonus);		
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
	
	public void updateGameViews() {
		updateViews();
	}

	private Handler  mHandlerFlash  = new Handler();
	private Runnable mRunnableFlash = new Runnable() {
		public void run() {
			int clr = mTextViewTime.getCurrentTextColor(); 
			
			mTextViewTime.setTextColor(
				(clr == mDefTextColor ? Color.RED : mDefTextColor));
			mHandlerFlash.postDelayed(this, 500);
		}
	};

	private Handler  mHandlerTimer  = new Handler();
	private Runnable mRunnableTimer = new Runnable() {
		public void run() {
			// decrease remaining time
			mCurTime--;					
			// generate text for display the remaining time
	    	String minute = String.valueOf(mCurTime / 60);
	    	String second = String.valueOf(mCurTime % 60);
	    	if (second.length() == 1) {
	    		second = "0" + second;
	    	}
	    	// generate flash effect when little time left
	    	if (mCurTime <= 20) {
	    		if (mCurTime == 20) {
	    			mHandlerFlash.postDelayed(mRunnableFlash, 500);
	    		}
	    		// alert sound
	    		if (mToneGen != null) {
	    			mToneGen.startTone(ToneGenerator.TONE_DTMF_5, 100);
	    		}	    		
	    	}
	    		    	    	
	    	if (mCurTime <= 0) {
	    		doGameOver();
	    	} else {
	    		// update UI
		    	mTextViewTime.setText(minute + ":" + second);	    	
		    	mTextViewTime.setTextColor(mDefTextColor);
	    		mHandlerTimer.postDelayed(mRunnableTimer, 1000);
	    	}	    	
		}
	};
	
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
	    	// host has dropped		    		
	    	if (mAcquire != null) {
				mAcquire.end();
				mAcquire = null;
			}
			if (mAutoCommitter != null) {
				mAutoCommitter.end();
				mAutoCommitter = null;
			}
						
			if (!mDrop[0]) {
				showDialog(DIALOG_DROP_P1);
				mDrop[0] = true;
			}
	    }
	    
		private long mOppoStartTime = -1;
		
	    private void onUpdateDataDone() {
	    	if (mOppoStartTime == -1) {
	    		mOppoStartTime = mOppo.getUpdateTime();
	    	}
	    	long elapsedOppo = mOppo.getUpdateTime() - mOppoStartTime;
	    	long elapsedHost = System.currentTimeMillis() - mStartTime;
	    	if (elapsedHost - elapsedOppo > TIME_OUT_DROP) {
	    		// opponent has dropped	    		
	    		mTextViewStatus2.setText("dropped");
	    		if (mAcquire != null) {
	    			mAcquire.end();
	    			mAcquire = null;
	    		}
	    		if (!mDrop[1]) {
	    			showDialog(DIALOG_DROP_P2);
	    			mDrop[1] = true;
	    		}
	    	} else {
	    		// everything is ok
	    		mTextViewName2.setText(mOppo.getName());
		    	mTextViewScore2.setText(String.valueOf(mOppo.getScore()));
		    	mTextViewStatus2.setText(mOppo.getSelLetters());
	    	}	    		    	
	    }       
	    
	    private void onUpdateDataError() {    		    	
	    }

	    private void onCommitDataDone() {	    	
	    }
	    
	    private void onCommitDataError() {	    	
	    }
    };
    
    protected void doGameOver() {
    	quitGame();
    }
    
    protected void quitGame() {
    	mQuit = true;
    	finish();
    	
    	//Intent i = new Intent(this, PBResult.class);
		//i.putExtra(PBMain.HOST_INFO, mPlayer.obj2json());
		//startActivity(i);
    }

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dlg = null;
				
		switch (id) {
		case DIALOG_QUIT:
			dlg = buildDialogQuit(this);
			break;
		case DIALOG_DROP_P1:
			dlg = buildDialogDropP1(this);
			break;
		case DIALOG_DROP_P2:
			dlg = buildDialogDropP2(this);
			break;
		}
		// TODO Auto-generated method stub
		return dlg;
	}
	
	private Dialog buildDialogQuit(Context context) {
		// TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setIcon(R.drawable.icon);
        builder.setTitle("Are you sure you want to quit?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	quitGame();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // continue, do nothing but wait for the timer
            }
        });
        return builder.create();
	}
	
	private Dialog buildDialogDropP1(Context context) {
		// TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setIcon(R.drawable.icon);
        builder.setTitle("You have dropped. Do you want to continue?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	;
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                quitGame();
            }
        });
        return builder.create();
	}
	
	private Dialog buildDialogDropP2(Context context) {
		// TODO Auto-generated method stub
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //builder.setIcon(R.drawable.icon);
        builder.setTitle(mOppo.getName() + " has dropped. Do you want to continue?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	;
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            	quitGame();
            }
        });
        return builder.create();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			showDialog(DIALOG_QUIT);
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	private static final String BOGGLE_PUZZLE = "boggle_puzzle";
	private static final String BOOGLE_START_TIME = "boogle_start_time";
 
	private void savePreferences() {
		getPreferences(MODE_PRIVATE).edit().putString(BOGGLE_PUZZLE, mPuzzle.toJSONString()).commit();
		getPreferences(MODE_PRIVATE).edit().putLong(BOOGLE_START_TIME, mStartTime);
		// Save the words list BOGGLE_WORDS_SIZE
		getPreferences(MODE_PRIVATE).edit().putInt(BOGGLE_WORDS_SIZE, mWordsFound.size()).commit();
		for (int i = 0; i < mWordsFound.size(); ++i) {
			String word = mWordsFound.get(i);
			getPreferences(MODE_PRIVATE).edit().putString(BOGGLE_WORDS + i, word).commit();
		}	
	}
	
	private void loadPreferences() {
		// load boggle puzzle
		String jsonString = getPreferences(MODE_PRIVATE).getString(BOGGLE_PUZZLE, "nothing");
		if (jsonString.compareTo("nothing") != 0 && !mNew) {
			mPuzzle = new BogglePuzzle(this, jsonString);
		} else {
			// create a new boggle puzzle
			mPuzzle = new BogglePuzzle(this, 6);
		}
		// load the game start time and words found before
		if (mNew) {
			mStartTime = System.currentTimeMillis();
			mWordsFound.clear();
		}
	}
}
