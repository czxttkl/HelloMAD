package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import android.content.res.Resources;

public class TrickyAlient extends Alient {
	private int mType = 0;
	
	public TrickyAlient(Resources res, int type) {
		super(res);
		mType = type;
	}
	
	@Override
	public void initSpeeds(float x, float y, int accTime) {		
		float accSpeedX = - x / ((mType == 0 ? 2000 : 320) / GameEngine.ENGINE_SPEED);
		float accSpeedY = y / (1000 / GameEngine.ENGINE_SPEED);			
		setSpeed(x, y + accSpeedY * accTime);
		setMinSpeed(0, y);
		setMaxSpeed(x, y + y);
		setAccSpeed(accSpeedX, accSpeedY);
	}

	@Override
	public void update() {	
		if (mSpeedUnchangeable) {
			mX += mSpeedX;
			mY += mSpeedY;
			return;
		}
		
		if (mAccMoveDuration > 0) {
			mSpeedY = Math.min(mSpeedY + mAccSpeedY, mMaxSpeedY);
			mAccMoveDuration -= GameEngine.ENGINE_SPEED;
		} else {
			mSpeedY = Math.max(mSpeedY - mAccSpeedY, mMinSpeedY);
		}
		
		if (mType == 0) {
			mSpeedX += mAccSpeedX;			
		} else if (mType == 1) {
			if (Math.abs(Math.abs(mSpeedX) - mMaxSpeedX) <= 1e-2) {
				if (mSpeedX > 0) { // right
					mAccSpeedX = -Math.abs(mAccSpeedX);
				} else {
					mAccSpeedX = Math.abs(mAccSpeedX);
				}
			}
			mSpeedX += mAccSpeedX;			
		}

		mX += mSpeedX;
		mY += mSpeedY;		
	}

	@Override
	public void triggerCollideEffect(int kind, float x, float y) {
		super.triggerCollideEffect(kind, x, y);		
	}
}
