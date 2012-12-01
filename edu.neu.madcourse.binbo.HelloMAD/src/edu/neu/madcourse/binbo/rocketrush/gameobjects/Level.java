package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Level extends GameObject {	
	protected int mLevel = 1;
	public float mSpeedScaleX     = 1;
	public float mSpeedScaleY     = 1;
	public float mComplexityScale = 1.1f;

	protected final static int DEFAULT_MOVE_DURATION = 200;
	protected final static int DEFAULT_STAY_DURATION = 1000;
	protected int mDisplayDuration = DEFAULT_MOVE_DURATION * 2 + DEFAULT_STAY_DURATION;
	
	protected float mTextWidth = 0;
	protected Paint mPaint = null;
	
	public Level(Resources res) {
		super(res);
		setKind(LEVEL);
		setMovable(true);
		setZOrder(ZOrders.LEVEL);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(0xff404040);
		mPaint.setStyle(Style.FILL);	
		mPaint.setFakeBoldText(true);
		mPaint.setTextSize(35);
		mPaint.setTextAlign(Paint.Align.CENTER);
	}
	
	public int getValue() {
		return mLevel;
	}
	
	public void levelUp() {
		++mLevel;
		
		mSpeedScaleX *= 1.1;
		mSpeedScaleY *= 1.1;
		mDisplayDuration = DEFAULT_MOVE_DURATION * 2 + DEFAULT_STAY_DURATION;
		
		mTextWidth = (int)mPaint.measureText("Level " + mLevel);
		float centerX = (mWidth - mTextWidth) / 2;		
		mSpeedX = (centerX + mTextWidth) / (DEFAULT_MOVE_DURATION / GameEngine.ENGINE_SPEED);
		
		mX = -mTextWidth;
	}
	
	@Override
	public void update() {		
		if (mDisplayDuration >= DEFAULT_STAY_DURATION + DEFAULT_MOVE_DURATION) {
			mX += mSpeedX;
			mDisplayDuration -= GameEngine.ENGINE_SPEED;
		} else if (mDisplayDuration >= DEFAULT_MOVE_DURATION) {
			mDisplayDuration -= GameEngine.ENGINE_SPEED;
		} else if (mDisplayDuration > 0) {
			mX += mSpeedX;
			mDisplayDuration -= GameEngine.ENGINE_SPEED;
		}				
	}
	
	public void doDraw(Canvas c) {
		if (mDisplayDuration > 0) {
			c.drawText("Level " + mLevel, mX, mHeight * 0.5f, mPaint);
		}
	}
	
	public void onSizeChanged(int width, int height) {
		mWidth  = width;
		mHeight = height;
			
		mTextWidth = (int)mPaint.measureText("Level " + mLevel);
		float centerX = (mWidth - mTextWidth) / 2;		
		mSpeedX = (centerX + mTextWidth) / (DEFAULT_MOVE_DURATION / GameEngine.ENGINE_SPEED);
		
		mX = -mTextWidth;
	}
}
