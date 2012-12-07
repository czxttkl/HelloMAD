package edu.neu.madcourse.binbo.rocketrush;

import java.util.List;

import edu.neu.madcourse.binbo.R;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.os.Handler;

public class VersusMode extends GameMode {
	
	protected VersusScene mScene = null; 
	protected VersusModeThread mThread = null;
	protected Context mContext = null;
	
	public VersusMode(Context context, GameEngine engine, Handler handler) {
		super(engine);
		setHandler(handler);		
		mContext = context;
		mScene = new VersusScene(context);
		mScene.load();
		mScene.setGameEventHandler(this);
	}
	
	@Override
	public GameScene getScene() {
		return mScene;
	}
	
	@Override
	public void resume() {
		if (!mEnable) return;
		
		if (mThread == null) {
			mThread = new VersusModeThread(mHandler);
			mThread.start();
		}
		mBackgroundMusic.play();
		super.resume();
	}

	@Override
	public void pause() {
		if (mThread != null) {
			mThread.end();
			mThread = null;
		}
		mBackgroundMusic.pause();
		super.pause();
	}
	
	@Override
	public void start() {
		if (!mEnable) return;
		
		mBackgroundMusic.create(mContext, R.raw.bkg_music_3);
		super.start();
	}

	@Override
	public void stop() {
		mBackgroundMusic.stop();
		super.stop();
	}

	@Override
	public void reset() {
		synchronized (mScene) {
			mScene.reset();
		}		
		mBackgroundMusic.reset();
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
				synchronized (mScene) {
					mEngine.updateGameScene(mScene);
				}
				
				synchronized (this) {
					try {
						wait(GameEngine.ENGINE_SPEED);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
