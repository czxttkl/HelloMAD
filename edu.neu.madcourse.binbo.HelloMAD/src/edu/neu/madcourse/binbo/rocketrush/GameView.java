package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	protected SurfaceHolder mHolder = null;
	protected Handler mHandler = null;
	protected GameDrawer mDrawer = null;
	protected BaseMode.Callback mModeCallback = null;
	
	// constructor must have attrs to create from xml
	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);	
		mHolder = getHolder();
		mHolder.addCallback(this);
	}
	
	public void onPause() {
		mDrawer.end();
	}
	
	public void onResume(GameScene scene) {
		mDrawer = new GameDrawer(scene, mHolder, mHandler);
		mDrawer.start();		
	}	
	
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	public void setModeCallback(BaseMode.Callback callback) {
		mModeCallback = callback;
	}
	
	public GameDrawer getDrawer() {
		return mDrawer;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		mModeCallback.onSurfaceChanged(width, height);
	}

	public void surfaceCreated(SurfaceHolder holder) {		
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub		
	}
	
}
