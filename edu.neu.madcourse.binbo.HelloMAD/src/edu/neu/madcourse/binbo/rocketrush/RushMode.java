package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.os.Handler;

public class RushMode extends GameMode {
	
	protected RushScene mScene = null; 
	protected RushModeThread mThread = null;
	
	public RushMode(Context context, GameEngine engine, Handler handler) {
		super(engine);
		setHandler(handler);
		mScene = new RushScene(context.getResources());
		mScene.load();
	}
	
	@Override
	public GameScene getScene() {
		return mScene;
	}
	
	@Override
	public void start() {
		mThread = new RushModeThread(mHandler);
		mThread.start();
		super.start();
	}

	@Override
	public void stop() {
		if (mThread != null) {
			mThread.end();
			mThread = null;
		}
		super.stop();
	}
	
	private final class RushModeThread extends BaseThread {
		
		public RushModeThread(Handler handler) {
			super(handler);
		}
		
		@Override
		public void run() {			
	
			while (mRun) {
				handleEvent(mEventQueue.poll());			
				
				// update the game scene with the engine
				mEngine.updateGameScene(mScene);
				
				try {
					sleep(GameEngine.ENGINE_SPEED);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		} // end of run
	}

	@Override
	public SensorEventListener getSensorListener() {
		// TODO Auto-generated method stub
		return super.getSensorListener();
	}
	
}
