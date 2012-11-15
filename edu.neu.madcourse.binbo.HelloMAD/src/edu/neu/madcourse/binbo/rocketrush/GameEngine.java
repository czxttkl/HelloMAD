package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.util.Log;


public class GameEngine {
	// TAG name definition
	protected static final String TAG = "RocketRush"; 
	// singleton game engine 
    protected static GameEngine sEngine = null;
    // engine speed
    public static final int ENGINE_SPEED = 20;
	// game states, only one state is available at the exactly time
	protected int mState = STATE_STOP;
	public static final int STATE_STOP    = 0;
	public static final int STATE_PREPAIR = 1;
    public static final int STATE_PLAY    = 2;
    public static final int STATE_PAUSE   = 3;
    public static final int STATE_WIN     = 4;
    public static final int STATE_LOSE    = 5;	  
    // flag to specify whether the initialization is done
    protected boolean mInitialized = false;
    // concurrent event queue used to receive all kinds of game events
    protected ConcurrentLinkedQueue<GameEvent> mEventQueue;
    // the list of GameCtrl which mostly generated from the user input, or sensors
    protected List<GameCtrl> mCtrls = new ArrayList<GameCtrl>();
    // generate game elements according to the scene configuration
    private Random mRandom = new Random();    

    protected GameEngine() {
    	mEventQueue = new ConcurrentLinkedQueue<GameEvent>();
    }
    
    public static GameEngine getInstance() {
    	if (sEngine == null) {
    		sEngine = new GameEngine();
    	}
    	return sEngine;    	
    }
    
    public void initialize() {
    	if (mInitialized) {
    		return;
    	}
    	
    	// do something here
    	
    	// update the flag to avoid re-initialize
    	mInitialized = true;
    }
    
    public void reset() {
    	mEventQueue.clear();
    } 
    
    public void updateGameScene(GameScene scene) {
    	// get events
    	GameEvent e = mEventQueue.poll();    	
    	if (e != null) {
    		handleEvent(e);
    	}
    	// do the real job here
    	List<GameObject> objects = scene.getGameObjects();
    	for (GameObject obj : objects) {    
    		for (GameCtrl ctrl : mCtrls) {
    			obj.execute(ctrl);
    		}
    		obj.update();
    	}
    	mCtrls.clear();
    }        
    
    private void handleEvent(GameEvent e) {
		if (e.mEventType == GameEvent.EVENT_CONTROL) {
			if (e.mWhat == GameEvent.SENSOR_ACCELEROMETER) {
				ControlEvent ce = (ControlEvent)e;				
				if (ce.mAccX >= 14) {
					mCtrls.add(new GameCtrl(GameCtrl.MOVE_LEFT));
				} else if (ce.mAccX <= -14) {
					mCtrls.add(new GameCtrl(GameCtrl.MOVE_RIGHT));
				}
				if (Math.abs(ce.mAccY) >= 15) {
					mCtrls.add(new GameCtrl(GameCtrl.MOVE_UP));
				}
			}
		}
	}

	public SensorEventListener getSensorEventListener() {
    	return mSensorListener;
    }
        
    // SensorEventListener implement
    // It's special, so I put it here
    protected SensorEventListener mSensorListener = new SensorEventListener() {
        //int count = 0;
        //long last_time = System.currentTimeMillis();
        public void onSensorChanged(SensorEvent sensorEvent){  
            if(sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER){  
                Log.i(TAG, "onSensorChanged");  
                                               
//                if (System.currentTimeMillis() - last_time > 1000) {
//                	last_time = System.currentTimeMillis();
//                	count = 0;
//                }
                float threshold = 13;
                float x = sensorEvent.values[0];  
                float y = sensorEvent.values[1];  
                float z = sensorEvent.values[2];                
//                Log.i(TAG,"\n heading " + x);  
//                Log.i(TAG,"\n pitch " + y);
//                Log.i(TAG,"\n roll " + z); 
//                Log.i(TAG,"\n count " + count);
//                count++;
                if (Math.abs(x) > threshold || Math.abs(y) > threshold || Math.abs(z) > threshold) {
                	ControlEvent e = new ControlEvent((int)x, (int)y, (int)z);
                	mEventQueue.add(e);
                }
            }  
        }
 
        public void onAccuracyChanged(Sensor sensor, int accuracy){  
            Log.i(TAG, "onAccuracyChanged");
        }  
    };
    
}
