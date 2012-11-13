package edu.neu.madcourse.binbo.rocketrush;

import android.os.Handler;

public class GameMode {
	// engine of the game
	protected GameEngine mEngine = null;
	// message handler
	protected Handler mHandler = null;	
	
	protected GameMode() {
		
	}
	
	public void start() {
		
	}
	
	public void stop() {
		
	}
	
	public void release() {
		
	}
	
	public GameScene getScene() {
		return null;
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
