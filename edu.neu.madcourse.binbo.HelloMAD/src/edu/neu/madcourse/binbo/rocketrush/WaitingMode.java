package edu.neu.madcourse.binbo.rocketrush;

import android.os.Handler;

public class WaitingMode extends BaseMode {

	protected WaitingScene mScene = null; 
	protected WaitingModeThread mThread = null;
	
	public WaitingMode(GameEngine engine, Handler handler) {
		setEngine(engine);
		setHandler(handler);
		mScene = new WaitingScene();
	}
	
	@Override
	public GameScene getScene() {
		return mScene;
	}	
	
	@Override
	public void start() {
		mThread = new WaitingModeThread(mHandler);
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

	private final class WaitingModeThread extends BaseThread {
		
		public WaitingModeThread(Handler handler) {
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
