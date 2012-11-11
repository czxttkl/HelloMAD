package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.os.Handler;

public class VersusMode extends BaseMode implements BaseMode.Callback {
	
	protected VersusScene mScene = null; 
	protected VersusModeThread mThread = null;
	
	public VersusMode(Context context, GameEngine engine, Handler handler) {
		setEngine(engine);
		setHandler(handler);		
		mScene = new VersusScene(context.getResources());
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

	public void onSurfaceChanged(int width, int height) {
		mScene.updateSceneSize(width, height);
	}
		
}
