package edu.neu.madcourse.binbo.rocketrush;

import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;

public class GameDrawer extends BaseThread {
	// the holder of the SurfaceView
	protected SurfaceHolder mHolder = null;
	// the game scene to draw
	protected GameScene mScene = null;	
	
	public GameDrawer(GameScene scene, SurfaceHolder holder, Handler handler) {
		mScene  = scene;
		mHolder = holder;
		setHandler(handler);				
	}	
	
	public synchronized void setGameScene(GameScene scene) {
		mScene = scene;
	}
	
	public synchronized GameScene getGameScene() {
		return mScene;
	}
	
	@Override
	public void run() {			
		Canvas c = null;
		
		while (mRun) {
			handleEvent(mEventQueue.poll());			
			
			try {
                c = mHolder.lockCanvas(null);
                getGameScene().doDraw(c);
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
			
			try {
				sleep(30); // draw 33 times per second, it's more than enough
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	} // end of run
	
}
