package edu.neu.madcourse.binbo.rocketrush;

import edu.neu.madcourse.binbo.rocketrush.GameEvent.IGameEventHandler;
import android.hardware.SensorEventListener;
import android.media.SoundPool;
import android.os.Handler;


public class GameMode implements IGameEventHandler {
	// engine of the game
	protected GameEngine mEngine = null;	
	// message handler
	protected Handler mHandler = null;	
	// music of the game
	protected BackgroundMusic mBackgroundMusic = new BackgroundMusic();	

	protected GameMode(GameEngine engine) {
		assert(engine != null);
		setEngine(engine);
	}
	
	public void resume() {
	}
	
	public void pause() {
	}
	
	public void start() {
	}
	
	public void stop() {
		mEngine.reset();		
	}
	
	public void reset() {}
	
	public void restart() {
		pause();
		reset();
		resume();
	}
	
	public void release() {}
	
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

	public void handleGameEvent(GameEvent evt) {
	}
		
}
