package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.hardware.SensorEventListener;
import android.os.Handler;

public class WaitingMode extends GameMode {

	protected WaitingScene mScene = null; 
	protected WaitingModeThread mThread = null;
	
	public WaitingMode(Context context, GameEngine engine, Handler handler) {
		super(engine);
		setHandler(handler);
		mScene = new WaitingScene(context.getResources());
		mScene.load();
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
	public void release() {
		if (mScene != null) {
			mScene.release();
			mScene = null;
		}
		super.release();
	}

	@Override
	public SensorEventListener getSensorListener() {
		// TODO Auto-generated method stub
		return super.getSensorListener();
	}

}
