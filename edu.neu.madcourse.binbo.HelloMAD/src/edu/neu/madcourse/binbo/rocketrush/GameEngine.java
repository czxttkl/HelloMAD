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
//	protected int mState = STATE_STOP;
//	public static final int STATE_STOP    = 0;
//	public static final int STATE_PREPAIR = 1;
//    public static final int STATE_PLAY    = 2;
//    public static final int STATE_PAUSE   = 3;
//    public static final int STATE_WIN     = 4;
//    public static final int STATE_LOSE    = 5;	  
    // flag to specify whether the initialization is done
    protected boolean mInitialized = false;
    // concurrent event queue used to receive all kinds of game events
    protected ConcurrentLinkedQueue<GameEvent> mEventQueue;
    // the list of GameCtrl which mostly generated from the user input, or sensors
    protected List<GameCtrl> mCtrls = new ArrayList<GameCtrl>();       

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
    	handleEvent(e);

    	// do the real job here
    	scene.updateBarriers();  
    	scene.updateReward();
    	List<GameObject> objects = scene.getGameObjects();
    	for (GameObject obj : objects) {    
    		for (GameCtrl ctrl : mCtrls) {
    			obj.operate(ctrl);
    		}
    		obj.update();
    		obj.detectCollision(objects);    		
    	}
    	mCtrls.clear();
    }        
    
    private void handleEvent(GameEvent e) {
    	if (e == null) return;
    	
		if (e.mEventType == GameEvent.EVENT_CONTROL) {
			if (e.mWhat == GameEvent.SENSOR_ACCELEROMETER) {
				ControlEvent ce = (ControlEvent)e;				
				if (ce.mAccX >= 2) {
					mCtrls.add(new GameCtrl(GameCtrl.MOVE_LEFT));
				} else if (ce.mAccX <= -2) {
					mCtrls.add(new GameCtrl(GameCtrl.MOVE_RIGHT));
				}
				if (Math.abs(ce.mAccY) >= 13) {
					mCtrls.add(new GameCtrl(GameCtrl.MOVE_VERT));
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

    	private float mLastX = 0;
    	private float mLastY = 0;
    	
        public void onSensorChanged(SensorEvent sensorEvent) {  
            if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {  

                float x = sensorEvent.values[0];  
                float y = sensorEvent.values[1];  
                float z = sensorEvent.values[2];                                         

                if (mLastX <= -2 && x > -2 && x < 2) {                
                	mEventQueue.clear();
                } else if (mLastX >= 2 && x > -2 && x < 2) {
                	mEventQueue.clear();
                } else if (Math.abs(x) >= 2 || Math.abs(y) >= 13) {
                	if (mEventQueue.size() > 2)
                		return;
                	ControlEvent e = new ControlEvent((int)x, (int)y, (int)z);
                	mEventQueue.add(e);
                }
                
                mLastX = x;
                mLastY = y;
            }  
        }
 
        public void onAccuracyChanged(Sensor sensor, int accuracy){  
            Log.i(TAG, "onAccuracyChanged");
        }  
    };

}
