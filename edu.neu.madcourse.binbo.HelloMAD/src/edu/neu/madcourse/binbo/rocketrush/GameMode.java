package edu.neu.madcourse.binbo.rocketrush;

import android.hardware.SensorEventListener;
import android.os.Handler;


public class GameMode {
	// engine of the game
	protected GameEngine mEngine = null;
	// message handler
	protected Handler mHandler = null;	
	
	protected GameMode(GameEngine engine) {
		assert(engine != null);
		setEngine(engine);
	}
	
	public void start() {
		
	}
	
	public void stop() {
		mEngine.reset();
	}
	
	public void reset() {
		
	}
	
	public void release() {
		
	}
	
	public GameScene getScene() {
		return null;
	}
	
	public SensorEventListener getSensorListener() {
		return mEngine.getSensorEventListener();
	}

	protected void setEngine(GameEngine engine) {
		mEngine = engine;
	}
	
	protected void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	protected GameEngine getEngine() {
		return mEngine;
	}
}
