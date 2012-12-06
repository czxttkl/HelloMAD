package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Level extends GameObject {	
	protected int mLevel = 1;
	public float mSpeedScaleX     = 1;
	public float mSpeedScaleY     = 1;
	public float mComplexityScale = 1.1f;

	protected final static int DEFAULT_MOVE_DURATION = 200;
	protected final static int DEFAULT_STAY_DURATION = 2000;
	protected int mDisplayDuration = DEFAULT_MOVE_DURATION * 2 + DEFAULT_STAY_DURATION;
	
	protected float mTextWidth = 0;
	protected Paint mPaint = null;
	protected Paint mPaintContainer = new Paint();
	protected Rect mContainer = new Rect();
	
	public Level(Resources res) {
		super(res);
		setKind(LEVEL);
		setMovable(true);
		setCollidable(false);
		setZOrder(ZOrders.LEVEL);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Style.FILL);
		mPaint.setTypeface(Typeface.SERIF);
		mPaint.setFakeBoldText(true);
		mPaint.setTextSize(40);
		mPaint.setShadowLayer(4, 1, 1, Color.BLACK);
		mPaint.setTextAlign(Paint.Align.CENTER);
		
		mPaintContainer.setARGB(80, 0, 0, 0);
		mPaintContainer.setStyle(Style.FILL);
		mPaintContainer.setAntiAlias(true);
	}
	
	public int getValue() {
		return mLevel;
	}
	
	public void levelUp() {
		++mLevel;

		mSpeedScaleX *= 1.06;
		mSpeedScaleY *= 1.07;
		mDisplayDuration = DEFAULT_MOVE_DURATION * 2 + DEFAULT_STAY_DURATION;

		mTextWidth = mPaint.measureText("Level " + String.valueOf(mLevel));	
		float centerX = (mWidth + mTextWidth) / 2;		
		mSpeedX = centerX / (DEFAULT_MOVE_DURATION / GameEngine.ENGINE_SPEED);
		
		mX = -mTextWidth/2;
	}
	
	@Override
	public void update() {		
		if (mDisplayDuration > DEFAULT_STAY_DURATION + DEFAULT_MOVE_DURATION) {
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
			c.drawRect(mContainer, mPaintContainer);
			c.drawText("Level " + mLevel, mX, mHeight * 0.5f, mPaint);
		}
	}
	
	public void onSizeChanged(int width, int height) {
		mWidth  = width;
		mHeight = height;
			
		mTextWidth = mPaint.measureText("Level " + mLevel);
		float centerX = (mWidth + mTextWidth) / 2;		
		mSpeedX = centerX / (DEFAULT_MOVE_DURATION / GameEngine.ENGINE_SPEED);
		
		mX = -mTextWidth/2;
		
		mContainer.top = (int) (mHeight * 0.5f - 110);
		mContainer.bottom = (int) (mHeight * 0.5f + 90);
		mContainer.left = 0;
		mContainer.right = (int) (mWidth);
	}
}
