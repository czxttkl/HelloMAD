package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;
import edu.neu.madcourse.binbo.rocketrush.GameObject.ZOrders;

public class Timer extends Utility {

	protected Paint mPaint = null;
	protected int mTime = 40;
	protected int mOneSecond = 1000;
	protected String mTextTime = "Time: " + mTime;
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	protected OnTimeUpdateListener mTimeUpdateListener = null;
	
	public Timer(Resources res) {
		super(res);
		setKind(TIMER);
		setMovable(false);
		setCollidable(false);
		setZOrder(ZOrders.TIMER);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLACK);
		mPaint.setStyle(Style.FILL);
		mPaint.setFakeBoldText(true);
		mPaint.setTextSize(34);		
	}
	
	public void setOnTimeUpdateListener(OnTimeUpdateListener listener) {
		mTimeUpdateListener = listener;
	}

	public void addBonusTime(int bonus) {
		mTime += bonus;
	}
	
	@Override
	public void update() {
		if (!mEnable) {
			return;
		}
		if (mOneSecond == 0) {
			mOneSecond = 1000;
			mTime = Math.max(mTime - 1, 0);
			mTextTime = "Time: " + (mTime < 10 ? " " : "") + mTime;
			if (mTimeUpdateListener != null) {
				mTimeUpdateListener.onTimeUpdate(mTime);
			}
		}
		mOneSecond -= GameEngine.ENGINE_SPEED;
		// TODO Auto-generated method stub
		super.update();
	}		

	@Override
	public void doDraw(Canvas c) {		
		c.drawText(mTextTime, mCanvasWidth - mPaint.measureText(mTextTime) - 24, 40, mPaint);
	}

	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;
	}
	
	public interface OnTimeUpdateListener {
		void onTimeUpdate(int curTime);
	}
}
