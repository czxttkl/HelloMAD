package edu.neu.madcourse.binbo.rocketrush;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameDrawer extends BaseThread {
	// the holder of the SurfaceView
	protected SurfaceHolder mHolder = null;
	// the game surface view
	protected GameView mView = null;
	// the game scene to draw
	protected GameScene mScene = null;	
	// flag to indicate whether the drawer should be paused
	protected boolean mPause = false;
	
	public GameDrawer(GameView view, GameScene scene, Handler handler) {
		mView   = view;
		mScene  = scene;		
		mHolder = view.getHolder(); 
		setHandler(handler);				
	}	
	
	public synchronized void setGameScene(GameScene scene) {
		mScene = scene;
		// notify the surface size to the scene
		if (mScene != null) {
			int width  = mHolder.getSurfaceFrame().width();
			int height = mHolder.getSurfaceFrame().height();
			if (width != 0 && height != 0) {
				mScene.onSizeChanged(width, height);
			}
		}
	}
	
	public synchronized GameScene getGameScene() {
		return mScene;
	}
	
	public void pause(boolean pause) {
		mPause = pause;
	}
	
	@Override
	public void run() {			
		Canvas c = null;
		
		blockIfSurfaceNotCreated();
		
		while (mRun) {
			handleEvent(mEventQueue.poll());	
			
			try {
                c = mHolder.lockCanvas(null);
                synchronized (mHolder) {
                	if (c != null) {
                		//Log.d("draw scene", "in doDraw");
                		GameScene scene = getGameScene();
                		synchronized (scene) {
                			scene.doDraw(c);
                		}
                		//Log.d("draw scene", "out doDraw");
                	}
                }
            } catch (Exception e) {
            	e.printStackTrace();
			} finally {
                // do this in a finally so that if an exception is thrown
                // during the above, we don't leave the Surface in an
                // inconsistent state
                if (c != null) {
                	mHolder.unlockCanvasAndPost(c);
                }
            } // end finally block
			
			synchronized (this) {
				try {
					wait(5);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			while (mPause) {
				try {
					sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		super.run();
	} // end of run
	
	protected void blockIfSurfaceNotCreated() {
		while (!mView.isSurfaceCreated()) {
			if (!mRun) break;
			
			try {
				synchronized (this) {
					wait(1000);
				}
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}
	}
}
