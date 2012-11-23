package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Odometer extends GameObject {

	protected int mAccMoveDuration = 0;
	protected int mOdometer = 0;
	protected int mTarget = 100;
	protected int mMilestone = 10000;
	protected Paint mPaint = null;
	protected OnOdometerUpdateListener mOdometerUpdateListener = null; 
	public final static float DEFAULT_SPEED_X = 0;
	public final static float DEFAULT_SPEED_Y = 3f;
	
	public Odometer(Resources res) {
		super(res);
		setKind(ODOMETER);
		setZOrder(ZOrders.ODOMETER);
		setSpeed(DEFAULT_SPEED_X, DEFAULT_SPEED_Y);
		setMaxSpeed(0, DEFAULT_SPEED_Y * 3);
		setAccSpeed(0, DEFAULT_SPEED_Y / (1000 / GameEngine.ENGINE_SPEED));
		
		mX = 16; mY = 32;
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(0xff404040);
		mPaint.setStyle(Style.FILL);	
		mPaint.setFakeBoldText(true);
		mPaint.setTextSize(28);
	}
	
	public void setOdometerUpdateListener(OnOdometerUpdateListener listener) {
		mOdometerUpdateListener = listener;
	}
	
	@Override
	public void onSizeChanged(int width, int height) {
		mWidth  = width;
		mHeight = height;
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
					mMilestone += 10000;
				}
			}
			mTarget += 100;			
		}
	}

	@Override
	public void doDraw(Canvas c) {
		c.drawText("Score " + mOdometer, mX, mY, mPaint);
	}
	
	public interface OnOdometerUpdateListener {
		void onReachTarget(int odometer);
		void onReachMilestone(int odometer);
	}
}
