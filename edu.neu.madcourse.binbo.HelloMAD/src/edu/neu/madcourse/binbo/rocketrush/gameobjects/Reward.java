package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.Random;

import android.content.res.Resources;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Reward extends GameObject {	
		
	protected Random mRand = new Random();
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	protected boolean mBound = false;
	protected Rocket mRocket = null;
	protected long mBoundTimeout   = 0;
	protected long mUnboundTimeout = 0; 
	protected long mBegTime = System.currentTimeMillis();
	
	public Reward(Resources res) {
		super(res);		
		setMovable(true);				
		setTimeout(20000, 20000);
		// set speed for unbound state
		setSpeed(2 + mRand.nextInt(3), 2 + mRand.nextInt(4));
	}
	
	public void setTimeout(long boundTimeout, long unboundTimeout) {
		mBoundTimeout   = boundTimeout;
		mUnboundTimeout = unboundTimeout;
	}
	
	public void bindRocket(Rocket rocket) {	
		setCollidable(false);
		rocket.bindReward(this);	
		mBound   = true;
		mRocket  = rocket;
		mBegTime = System.currentTimeMillis(); // update this time
		onBound();
	}
	
	public void unbindRocket() {
		if (mRocket == null) { // hasn't been bound
			return;
		}
		mRocket.unbindReward(this);
	}
	
	public boolean isTimeout() {
		return (System.currentTimeMillis() - mBegTime) > 
					(mBound ? mBoundTimeout : mUnboundTimeout);
	}

	@Override
	public void doDraw(Canvas c) {					
		if (mBound) {			
			drawBound(c);
		} else {
			drawUnbound(c);
		}
	}	

	@Override
	public void update() {				
		if (mBound) {
			updateBound();
			if (isTimeout()) {
				unbindRocket();
			}
		} else {
			updateUnbound();			
		}
	}
	
	@Override
	public void release() {
		unbindRocket();
	}
	
	@Override
	public void operate(GameCtrl ctrl) {
	}

	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;		
	}
	
	protected void onBound() {}
	protected void updateBound() {}
	protected void updateUnbound() {}
	protected void drawBound(Canvas c) {}
	protected void drawUnbound(Canvas c) {}
}
