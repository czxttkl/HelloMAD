package edu.neu.madcourse.binbo.rocketrush;

import android.os.Handler;

public class RushMode extends BaseMode {
	
	protected RushScene mScene = null; 
	protected RushModeThread mThread = null;
	
	public RushMode(GameEngine engine, Handler handler) {
		setEngine(engine);
		setHandler(handler);
		mScene = new RushScene();
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
				
				// update the game context with the engine
				// ...
				
				try {
					sleep(30); // draw 33 times per second, it's more than enough
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} // end of run
	}
		
}
