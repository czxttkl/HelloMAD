package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Odometer extends Utility {

	protected int mAccMoveDuration = 0;
	protected int mOdometer = 0;
	protected int mTarget = 100;
	protected int mMilestone = 5000;
	protected Paint mPaint = null;
	protected OnOdometerUpdateListener mOdometerUpdateListener = null; 
	public final static float DEFAULT_SPEED_X = 0;
	public final static float DEFAULT_SPEED_Y = 3f;
	
	public Odometer(Resources res) {
		super(res);
		setKind(ODOMETER);
		setCollidable(false);
		setZOrder(ZOrders.ODOMETER);
		setSpeed(DEFAULT_SPEED_X, DEFAULT_SPEED_Y);
		setMaxSpeed(0, DEFAULT_SPEED_Y * 3);
		setAccSpeed(0, DEFAULT_SPEED_Y / (1000 / GameEngine.ENGINE_SPEED));
		
		mX = 24; mY = 82;
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setARGB(255, 0, 0, 0);
		mPaint.setStyle(Style.FILL);	
		mPaint.setColor(Color.WHITE);
		mPaint.setTextSize(30);
		mPaint.setFakeBoldText(true);
		mPaint.setShadowLayer(2, 1, 1, Color.BLACK);		
	}
	
	public int getDistance() {
		return mOdometer / 10;
	}
	
	public void setOdometerUpdateListener(OnOdometerUpdateListener listener) {
		mOdometerUpdateListener = listener;
	}
	
	@Override
	public void operate(GameCtrl ctrl) {
		int command = ctrl.getCommand();
		
		if (command == GameCtrl.MOVE_VERT) {
			mAccMoveDuration = 1000;
		} 
	}

	@Override
	public void update() {
		if (!mEnable) {
			return;
		}
		if (mAccMoveDuration > 0) {
			mSpeedY = Math.min(mSpeedY + mAccSpeedY, mMaxSpeedY);			
			mAccMoveDuration -= GameEngine.ENGINE_SPEED;
		} else {
			mSpeedY = Math.max(mSpeedY - mAccSpeedY, DEFAULT_SPEED_Y);
		}
		
		mOdometer += mSpeedY;
		if (mOdometer >= mTarget) {
			if (mOdometerUpdateListener != null) {
				mOdometerUpdateListener.onReachTarget(mOdometer);
				if (mOdometer >= mMilestone) {
					mOdometerUpdateListener.onReachMilestone(mOdometer);
					mMilestone += 5000;
				}
			}
			mTarget += 100;			
		}
	}

	@Override
	public void doDraw(Canvas c) {
		String s = "Distance   " + mOdometer / 10 + " / " + mMilestone / 10;
		c.drawText(s, mX, mY, mPaint);
	}
	
	public interface OnOdometerUpdateListener {
		void onReachTarget(int odometer);
		void onReachMilestone(int odometer);
	}

	@Override
	public void onSizeChanged(int width, int height) {
		if (width <= 480) {
			mPaint.setTextSize(27);
	    } else {
	    	mPaint.setTextSize(30);
	    }
	}
}
