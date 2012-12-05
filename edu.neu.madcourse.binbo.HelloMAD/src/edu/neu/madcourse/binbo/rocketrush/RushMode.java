package edu.neu.madcourse.binbo.rocketrush;

import edu.neu.madcourse.binbo.R;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;

public class RushMode extends GameMode {
	
	protected RushScene mScene = null; 
	protected RushModeThread mThread = null;
	protected Context mContext = null;
	protected int mMusicIDs[] = { 
		R.raw.game_over, R.raw.bkg_music_2, R.raw.bkg_music_3, R.raw.bkg_music_4
	};
	protected int mMusicIndex = 1;
	
	public RushMode(Context context, GameEngine engine, Handler handler) {
		super(engine);
		setHandler(handler);
		mContext = context;
		mScene = new RushScene(context);
		mScene.load();
		mScene.setGameEventHandler(this);
	}
	
	@Override
	public GameScene getScene() {
		return mScene;
	}
	
	@Override
	public void resume() {
		mThread = new RushModeThread(mHandler);
		mThread.start();
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
		mBackgroundMusic.create(mContext, mMusicIDs[mMusicIndex]);
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

	private final class RushModeThread extends BaseThread {
		
		public RushModeThread(Handler handler) {
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

	@Override
	public void handleGameEvent(GameEvent evt) {
		if (evt.mEventType == GameEvent.EVENT_STATE) {
			StateEvent se = (StateEvent) evt;
			if (se.mWhat == StateEvent.STATE_OVER) {
				Message msg = mHandler.obtainMessage();     	
		        msg.what = StateEvent.STATE_OVER;
		        msg.obj  = se.mDescription;
		        mHandler.sendMessage(msg);
		        ((FragmentActivity) mContext).runOnUiThread(new Runnable() {
				    public void run() {				    	
				    	mBackgroundMusic.create(mContext, mMusicIDs[++mMusicIndex]);
				    	mBackgroundMusic.play();
				    }
				});
			} else if (se.mWhat == StateEvent.STATE_LEVELUP) {
				// not good to do the cast here, modify later
				((FragmentActivity) mContext).runOnUiThread(new Runnable() {
				    public void run() {				    	
				    	mBackgroundMusic.create(mContext, mMusicIDs[++mMusicIndex]);
				    	mBackgroundMusic.play();
				    }
				});
				
			}
		}
	}
	
}
