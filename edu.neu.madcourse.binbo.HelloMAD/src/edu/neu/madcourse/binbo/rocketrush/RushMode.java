package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.os.Handler;

public class RushMode extends BaseMode implements BaseMode.Callback {
	
	protected RushScene mScene = null; 
	protected RushModeThread mThread = null;
	
	public RushMode(Context context, GameEngine engine, Handler handler) {
		setEngine(engine);
		setHandler(handler);
		mScene = new RushScene(context.getResources());
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
					sleep(30); // draw 33 times per second, it's more than enough
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		} // end of run
	}

	public void onSurfaceChanged(int width, int height) {
		mScene.updateSceneSize(width, height);
	}
		
}
