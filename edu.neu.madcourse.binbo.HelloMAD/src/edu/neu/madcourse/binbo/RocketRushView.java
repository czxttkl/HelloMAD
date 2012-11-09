package edu.neu.madcourse.binbo;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class RocketRushView extends SurfaceView implements SurfaceHolder.Callback {

	protected SurfaceHolder mHolder = null;
	protected Handler mHandler = null;
	
	// constructor must have attrs to create from xml
	public RocketRushView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mHolder = getHolder();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}

	public void onPause() {
		
	}
	
	public void onResume() {
		
	}
	
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
}
