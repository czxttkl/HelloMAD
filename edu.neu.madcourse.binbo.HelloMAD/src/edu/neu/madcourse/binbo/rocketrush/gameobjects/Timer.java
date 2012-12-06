package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.Paint.Style;
import edu.neu.madcourse.binbo.rocketrush.GameObject;
import edu.neu.madcourse.binbo.rocketrush.GameObject.ZOrders;

public class Timer extends GameObject {

	protected Paint mPaint = null;
	protected int mTime = 30;
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
		// TODO Auto-generated method stub
		super.update();
	}		

	@Override
	public void doDraw(Canvas c) {
		String textTime = "Time: " + mTime;
		float textLength = mPaint.measureText(textTime);
		c.drawText(textTime, mCanvasWidth - textLength - 24, 40, mPaint);
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
