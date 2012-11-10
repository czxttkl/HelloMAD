package edu.neu.madcourse.binbo.rocketrush;

public class BaseMode extends BaseThread {
	// engine of the game
	protected GameEngine mEngine = null;
	
	protected BaseMode() {
		
	}
	
	protected void setEngine(GameEngine engine) {
		mEngine = engine;
	}
	
	protected GameEngine getEngine() {
		return mEngine;
	}
}
