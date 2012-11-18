package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	protected SurfaceHolder mHolder = null;
	protected Handler mHandler = null;
	protected GameDrawer mDrawer = null;
	protected boolean mCreated = false;
	
	// constructor must have AttributeSet to create from XML
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);	
		mHolder = getHolder();
		mHolder.addCallback(this);
	}
	
	public void onPause() {
		mDrawer.end();
		mDrawer = null;
	}
	
	public void onResume(GameScene scene) {
		mDrawer = new GameDrawer(this, scene, mHandler);
		mDrawer.start();
	}	
	
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	public GameDrawer getDrawer() {
		return mDrawer;
	}
	
	public boolean isSurfaceCreated() {
		return mCreated;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		GameScene scene = mDrawer.getGameScene();
		if (scene != null) {
			scene.onSizeChanged(width, height);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		mCreated = true;
		synchronized (mDrawer) {
			mDrawer.notify();
		}		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		mCreated = false;	
	}
	
}
