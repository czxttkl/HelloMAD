package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.binbo.R;
import android.content.Context;
import android.hardware.SensorEventListener;
import android.media.AudioManager;
import android.media.SoundPool;
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
	// audio feedback
	protected SoundPool mSoundPool = null;
	protected int mSoundResIDs[] = {
		R.raw.get_reward_1, R.raw.get_reward_2, R.raw.hit_alient, R.raw.hit_bird,
		R.raw.hit_stone_thunder
	};
	protected List<Integer> mSoundIDs = new ArrayList<Integer>();
	
	public RushMode(Context context, GameEngine engine, Handler handler) {
		super(engine);
		setHandler(handler);
		mContext = context;
		mScene = new RushScene(context);
		mScene.load();
		mScene.setGameEventHandler(this);
		
		mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);   
		for (int resID : mSoundResIDs) {
			int soundID = mSoundPool.load(context, resID, 1);
			mSoundIDs.add(Integer.valueOf(soundID));
		}

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
			}
		} else if (evt.mEventType == GameEvent.EVENT_SCENE) {
			final SceneEvent sce = (SceneEvent) evt;
			if (sce.mWhat == SceneEvent.SCENE_LEVELUP) {
				// not good to do the cast here, modify later
				((FragmentActivity) mContext).runOnUiThread(new Runnable() {
				    public void run() {				    	
				    	mMusicIndex = mMusicIndex > 3 ? 0 : mMusicIndex + 1;
				    	mBackgroundMusic.create(mContext, mMusicIDs[mMusicIndex]);
				    	mBackgroundMusic.play();
				    }
				});
			} else if (sce.mWhat == SceneEvent.SCENE_COLLIDE) {
				((FragmentActivity) mContext).runOnUiThread(new Runnable() {
				    public void run() {
				    	int soundID = 0;
				    	switch (sce.mMsg.what) {
				    	case GameObject.PROTECTION:
				    		soundID = mSoundIDs.get(0);
				    		break;
				    	case GameObject.ALIENT:
				    		soundID = mSoundIDs.get(2);
				    		break;
				    	case GameObject.BIRD:
				    		soundID = mSoundIDs.get(3);
				    		break;
				    	case GameObject.ASTEROID:
				    		soundID = mSoundIDs.get(4);
				    		break;
				    	case GameObject.THUNDER:
				    		soundID = mSoundIDs.get(4);
				    		break;
				    	}
				    	int ret = mSoundPool.play(soundID, 0.3f, 0.3f, 2, 0, 1);
				    	if (ret == 0) {
				    		ret = 1;
				    	}
				    }
				});
			}
		}
	}
//	protected int mSoundResIDs[] = {
//			R.raw.get_reward_1, R.raw.get_reward_2, R.raw.hit_alient, R.raw.hit_bird,
//			R.raw.hit_stone_thunder
//		};
}
