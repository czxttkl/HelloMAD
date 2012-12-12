package edu.neu.madcourse.binbo.rocketrush;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.dialog.GameOverDialog;
import edu.neu.madcourse.binbo.rocketrush.dialog.GameOverDialogFragment;
import edu.neu.madcourse.binbo.rocketrush.dialog.RushModeDialogFragment;
import edu.neu.madcourse.binbo.rocketrush.dialog.VersusModeDialogFragment;
import edu.neu.madcourse.binbo.rocketrush.dialog.WaitingModeDialogFragment;
import edu.neu.madcourse.binbo.rocketrush.speech.OpusManager;
import edu.neu.madcourse.binbo.rocketrush.splash.SplashView;
import edu.neu.madcourse.binbo.rocketrush.tutorial.TutorialActivity;

public class RocketRushActivity extends FragmentActivity 
								implements OnClickListener, OnTouchListener {	
	// views in the game menu
	protected SplashView mSplashView = null;
	protected GameView mGameView = null;
	protected TextView mTimeView = null;
	protected ImageButton mRushModeButton = null;
	protected ImageButton mVSModeButton = null;
	protected ImageButton mSettingsButton = null;
	protected ImageButton mTutorialButton = null;
	protected ImageButton mRankButton = null;
	protected ImageButton mAboutButton = null;
	protected SensorManager mSensorManager = null;
	// all of the game modes
	protected GameMode mCurMode = null;
	protected List<GameMode> mModes = new ArrayList<GameMode>();	
	// speech controller
	protected OpusManager mOpus = null;
	// for dialog request code
	protected final static int GAMEOVER_DIALOG_REQUEST = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);			
		setContentView(R.layout.rocket_rush);					

		// get views and set listeners
		setupViews();
		// adjust layouts according to the screen resolution
		adjustLayout();
		// create accelerometer
		createSensor();
		// create speech controller
		// createOpus();
		// create game, including game engine and all the modes and scenes
		createGame();
	}	

	public static final int MODE_WAITING = 0;
	public static final int MODE_RUSH    = 1;
	public static final int MODE_VERSUS  = 2;		
	
	private void createGame() {
		GameEngine engine = GameEngine.getInstance();
		engine.initialize();
		// only three modes now		
		mModes.add(new WaitingMode(this, engine, mHandler));
		mModes.add(new RushMode(this, engine, mHandler));
		mModes.add(new VersusMode(this, engine, mHandler));
		mCurMode = mModes.get(MODE_WAITING);
	}
	
	public void switchGameMode(int modeTo) {
		if (mCurMode == mModes.get(modeTo)) {
			return;
		}		

		// first stop to update the scene
		mCurMode.pause();
		mCurMode.stop();
		// reset the current mode
		mCurMode.reset();
		mCurMode.setEnable(false); // disable it, then pause() and start() will do nothing
		// get the new game mode
		mCurMode = mModes.get(modeTo);
		mCurMode.setEnable(true); // enable it, otherwise pause() and start() will do nothing
		// set the scene of the new mode to game drawer
		mGameView.getDrawer().setGameScene(mCurMode.getScene());		
		// finally start the new game mode
		mCurMode.start();
		mCurMode.resume();

		// ...
		if (modeTo == MODE_WAITING) {
			mRushModeButton.setVisibility(View.VISIBLE);
			mVSModeButton.setVisibility(View.GONE);
			mSettingsButton.setVisibility(View.VISIBLE);
			mTutorialButton.setVisibility(View.VISIBLE);
			mRankButton.setVisibility(View.VISIBLE);
			mAboutButton.setVisibility(View.VISIBLE);
		} else {
			mRushModeButton.setVisibility(View.GONE);
			mVSModeButton.setVisibility(View.GONE);
			mSettingsButton.setVisibility(View.GONE);
			mTutorialButton.setVisibility(View.GONE);
			mRankButton.setVisibility(View.GONE);
			mAboutButton.setVisibility(View.GONE);
		}
	}
	
	public GameMode getCurrentGameMode() {
		return mCurMode;
	}
	
	@Override
	protected void onDestroy() {
		mModes.get(0).release();
		mModes.get(1).release();
		mModes.get(2).release();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		//mGameView.onPause();
		mCurMode.pause();
 
        if (mSensorManager != null) {
        	mSensorManager.unregisterListener(
        		mCurMode == null ? null : mCurMode.getSensorListener()
        	);                
        }        
		
		super.onPause();		
	}

	@Override
	protected void onResume() {		
		super.onResume();
		
        if (mSensorManager != null) {
        	mSensorManager.registerListener(
        		mCurMode == null ? null : mCurMode.getSensorListener(),
        		mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        		SensorManager.SENSOR_DELAY_GAME // 20ms on my s3
        	);                 	
        }        
		                
		//mGameView.onResume(mCurMode.getScene());
		mCurMode.resume();
	}

	@Override
	protected void onStart() {
		mCurMode.start();
		mGameView.onStart(mCurMode.getScene());
		super.onStart();
	}

	@Override
	protected void onStop() {
		mGameView.onStop();
		mCurMode.stop();
		super.onStop();
	}
	
	public void onClick(View v) {
		Intent i = null;
		// here we switch from waiting mode to other modes
		switch (v.getId()) {
		case R.id.rushModeButton:	
			switchGameMode(MODE_RUSH);
			break;		
		case R.id.vsModeButton:			
			switchGameMode(MODE_VERSUS);
			break;
		case R.id.settingsButton:
			startActivity(new Intent(this, Prefs.class));
			break;
		case R.id.rankButton:
			i = new Intent(this, GameRank.class);
			i.putExtra(GameRank.RESULTS, getGameResults());
			startActivity(i);			
			break;
		case R.id.helpButton:
			i = new Intent(this, TutorialActivity.class);
			i.putExtra("edu.neu.madcourse.binbo.rocketrush.Main", "RocketRushActivity");
			startActivity(i);
			break;
		case R.id.aboutButton:
			startActivity(new Intent(this, Acknowledge.class));
			break;
		}
	}
	
	public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
	
	private final static int WAITING_MODE = 0;
	private final static int RUSH_MODE	  = 1;
	private final static int VERSUS_MODE  = 2;
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mCurMode == mModes.get(WAITING_MODE)) { // waiting mode
				WaitingModeDialogFragment dialog = new WaitingModeDialogFragment();
				dialog.show(getSupportFragmentManager(), "WaitingModeDialogFragment");
			} else if (mCurMode == mModes.get(RUSH_MODE)) {
				RushModeDialogFragment dialog = new RushModeDialogFragment();
				dialog.show(getSupportFragmentManager(), "RushModeDialogFragment");				
			} else if (mCurMode == mModes.get(VERSUS_MODE)) {
				VersusModeDialogFragment dialog = new VersusModeDialogFragment();
				dialog.show(getSupportFragmentManager(), "VersusModeDialogFragment");
			}			
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}		
	
	private void createSensor() {
		// get system sensor manager to deal with sensor issues  
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	}
	
//	private void createOpus() {
//		mOpus = new OpusManager(this);
//	}

	private void adjustLayout() {
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, 
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
		);  
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
	}

	private void setupViews() {
		mGameView = (GameView)findViewById(R.id.rocketRushView);
		mTimeView = (TextView)findViewById(R.id.timerView);
		mRushModeButton = (ImageButton)findViewById(R.id.rushModeButton);
		mVSModeButton   = (ImageButton)findViewById(R.id.vsModeButton);
		mSettingsButton = (ImageButton)findViewById(R.id.settingsButton);
		mTutorialButton = (ImageButton)findViewById(R.id.helpButton);
		mRankButton     = (ImageButton)findViewById(R.id.rankButton);
		mAboutButton    = (ImageButton)findViewById(R.id.aboutButton);
		
		mRushModeButton.setOnClickListener(this);
		mVSModeButton.setOnClickListener(this);
		mSettingsButton.setOnClickListener(this);
		mTutorialButton.setOnClickListener(this);
		mRankButton.setOnClickListener(this);
		mAboutButton.setOnClickListener(this);
		mVSModeButton.setVisibility(View.GONE);
		
		mGameView.setHandler(mHandler);
	}	
	
	protected final static String RANK_SIZE  = "rank_size";
	protected final static String RANK_SCORE = "rank_score_";
	protected final static String RANK_TIME  = "rank_time_";
	
	protected void recordGameResult(int score, String time) {
		int size = getPreferences(MODE_PRIVATE).getInt(RANK_SIZE, 0);
		getPreferences(MODE_PRIVATE).edit().putInt(RANK_SIZE, size + 1).commit();	
		getPreferences(MODE_PRIVATE).edit().putInt(RANK_SCORE + size, score).commit();
		getPreferences(MODE_PRIVATE).edit().putString(RANK_TIME + size, time).commit();
	}
	
	protected GameResults getGameResults() {
		GameResults results = new GameResults();
		
		int size = getPreferences(MODE_PRIVATE).getInt(RANK_SIZE, 0);
		for (int i = 0; i < size; ++i) {								
			int score = getPreferences(MODE_PRIVATE).getInt(RANK_SCORE + i, 0);
			String date = getPreferences(MODE_PRIVATE).getString(RANK_TIME + i, "");
			GameResult result = new GameResult(score, date);
			results.add(result);
		}	
		Collections.sort(results, Collections.reverseOrder());
	
		return results;
	}

	// use main looper as the default
	private final Handler mHandler = new Handler() {	

		public void handleMessage(Message msg) {        	
			Intent i = null;
			// all game messages that should handle in UI thread
        	switch (msg.what) {  
        	case StateEvent.STATE_OVER:
//        		GameOverDialogFragment dialog = new GameOverDialogFragment();
//        		dialog.setDistance(((Integer) msg.obj).intValue());
//				dialog.show(getSupportFragmentManager(), "GameOverDialogFragment");
//				recordGameResult(((Integer) msg.obj).intValue(), 
//						DateFormat.getDateInstance().format(new Date()));
        		int distance = ((Integer) msg.obj).intValue();
        		i = new Intent(getApplicationContext(), GameOverDialog.class);
        		i.putExtra("distance", distance);
        		startActivityForResult(i, GAMEOVER_DIALOG_REQUEST);
        		recordGameResult(distance, DateFormat.getDateInstance().format(new Date()));
        		break;
            default:
            	break;
            }            
        }			
    };

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GAMEOVER_DIALOG_REQUEST) {
            if (resultCode == GameOverDialog.RESTART_GAME) {
                mCurMode.restart();
            } else if (resultCode == GameOverDialog.BACK_TO_MENU) {
            	switchGameMode(0);
            }
        }
    } 
    
}
