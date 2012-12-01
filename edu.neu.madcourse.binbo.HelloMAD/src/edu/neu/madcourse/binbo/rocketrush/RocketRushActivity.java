package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.speech.OpusManager;
import edu.neu.madcourse.binbo.rocketrush.splash.SplashView;
import edu.neu.madcourse.binbo.rocketrush.tutorial.TutorialActivity;

public class RocketRushActivity extends Activity implements OnClickListener, OnTouchListener {	
	
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
		createOpus();
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
	
	private void switchGameMode(int modeTo) {
		if (mCurMode == mModes.get(modeTo)) {
			return;
		}		

		// first stop to update the scene
		mCurMode.stop();	
		// get the new game mode
		mCurMode = mModes.get(modeTo);
		// set the scene of the new mode to game drawer
		mGameView.getDrawer().setGameScene(mCurMode.getScene());		
		// finally start the new game mode
		mCurMode.start();

		// ...
		mRushModeButton.setVisibility(View.INVISIBLE);
		mVSModeButton.setVisibility(View.INVISIBLE);
		mSettingsButton.setVisibility(View.INVISIBLE);
		mTutorialButton.setVisibility(View.INVISIBLE);
		mRankButton.setVisibility(View.INVISIBLE);
		mAboutButton.setVisibility(View.INVISIBLE);
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
		mGameView.onPause();
		mCurMode.stop();
 
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
		                
		mGameView.onResume(mCurMode.getScene());
		mCurMode.start();
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
	
	public void onClick(View v) {
		// here we switch from waiting mode to other modes
		switch (v.getId()) {
		case R.id.rushModeButton:	
			switchGameMode(MODE_RUSH);
			break;		
		case R.id.vsModeButton:			
			switchGameMode(MODE_VERSUS);
			break;
		case R.id.helpButton:
			Intent i = new Intent(this, TutorialActivity.class);
			startActivity(i);
		}
	}
	
	public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			//showDialog(DIALOG_QUIT);			
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}		
	
	private void createSensor() {
		// get system sensor manager to deal with sensor issues  
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	}
	
	private void createOpus() {
		mOpus = new OpusManager(this);
	}

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
		
		mGameView.setHandler(mHandler);
	}
	
	// use main looper as the default
	private final Handler mHandler = new Handler() {	

		public void handleMessage(Message msg) {        	
			// all game messages that should handle in UI thread
        	switch (msg.what) {     		        
            default:
            	break;
            }            
        } 		
    };        
	
}
