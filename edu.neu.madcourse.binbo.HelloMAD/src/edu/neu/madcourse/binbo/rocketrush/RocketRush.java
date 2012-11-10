package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.R.*;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.*;

public class RocketRush extends Activity implements OnClickListener {
	private static final String TAG = "RocketRush";
	
	protected SplashView mSplashView = null;
	protected GameView mGameView = null;
	protected TextView mTimeView = null;
	protected ImageButton mRushModeButton = null;
	protected ImageButton mVSModeButton = null;
	protected SensorManager mSensorManager = null;
	// all of the game modes
	protected BaseMode mCurMode = null;
	protected List<BaseMode> mModes = new ArrayList<BaseMode>();	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);						
		setContentView(R.layout.rocket_rush);					
	    
		//loadPreferences(command);
		// get views and set listeners
		setupViews();
		// adjust layouts according to the screen resolution
		adjustLayout();
		// create accelerometer
		createSensor();
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
		mModes.add(new WaitingMode(engine, mHandler));
		mModes.add(new RushMode(engine, mHandler));
		mModes.add(new VersusMode(engine, mHandler));
		mCurMode = mModes.get(MODE_WAITING);
	}
	
	private void switchGameMode(int modeTo) {
		if (mCurMode == mModes.get(modeTo)) {
			return;
		}
		// first stop to update the scene using game engine
		mCurMode.stop();	
		// get the new game mode
		mCurMode = mModes.get(modeTo);
		// set the scene of the new mode to game drawer
		mGameView.getDrawer().setGameScene(mCurMode.getScene());
		// finally start the new game mode
		mCurMode.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// the sequence here is important
		mGameView.onPause();
		mCurMode.stop();
 
        if (mSensorManager != null) {
        	mSensorManager.unregisterListener(mAccListener);                
        }        
		
		super.onPause();		
	}

	@Override
	protected void onResume() {		
		super.onResume();
		
        if (mSensorManager != null) {
        	mSensorManager.registerListener(
        		mAccListener,
        		mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
        		SensorManager.SENSOR_DELAY_GAME
        	);                 	
        }        
		
        mCurMode.start();
		mGameView.onResume(mCurMode.getScene());
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
		}
	}
	
	private void createSensor() {
		// get system sensor manager to deal with sensor issues  
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	}

	private void adjustLayout() {
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
	}

	private void setupViews() {
		mGameView = (GameView)findViewById(R.id.rocketRushView);
		mTimeView = (TextView)findViewById(R.id.timerView);
		mRushModeButton = (ImageButton)findViewById(R.id.rushModeButton);
		mVSModeButton = (ImageButton)findViewById(R.id.vsModeButton);
		
		mRushModeButton.setOnClickListener(this);
		mVSModeButton.setOnClickListener(this);
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
    
    // SensorEventListener implement  
    final SensorEventListener mAccListener = new SensorEventListener() {  
        int count = 0;
        public void onSensorChanged(SensorEvent sensorEvent){  
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){  
                Log.i(TAG, "onSensorChanged");  
                               
                float x = sensorEvent.values[0];  
                float y = sensorEvent.values[1];  
                float z = sensorEvent.values[2];                
                Log.i(TAG,"\n heading " + x);  
                Log.i(TAG,"\n pitch " + y);  
                Log.i(TAG,"\n roll " + z); 
                Log.i(TAG,"\n count " + count);
                count++;
            }  
        }  
 
        public void onAccuracyChanged(Sensor sensor, int accuracy){  
            Log.i(TAG, "onAccuracyChanged");  
        }  
    };
	
}
