package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	protected SurfaceHolder mHolder = null;
	protected Handler mHandler = null;
	protected GameDrawer mDrawer = null;
	
	// constructor must have AttributeSet to create from XML
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
	
	public GameDrawer getDrawer() {
		return mDrawer;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		GameScene scene = mDrawer.getGameScene();
		if (scene != null) {
			scene.onSizeChanged(width, height);
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {		
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub		
	}
	
}
