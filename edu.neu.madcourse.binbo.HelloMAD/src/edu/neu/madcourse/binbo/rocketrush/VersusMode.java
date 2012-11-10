package edu.neu.madcourse.binbo.rocketrush;

import android.os.Handler;

public class VersusMode extends BaseMode {
	
	protected VersusScene mScene = null; 
	protected VersusModeThread mThread = null;
	
	public VersusMode(GameEngine engine, Handler handler) {
		setEngine(engine);
		setHandler(handler);
		mScene = new VersusScene();
	}
	
	@Override
	public GameScene getScene() {
		return mScene;
	}
	
	@Override
	public void start() {
		mThread = new VersusModeThread(mHandler);
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
	
	private final class VersusModeThread extends BaseThread {
		
		public VersusModeThread(Handler handler) {
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
