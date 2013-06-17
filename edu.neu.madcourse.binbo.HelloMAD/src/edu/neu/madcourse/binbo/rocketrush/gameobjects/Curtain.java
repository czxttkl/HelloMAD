package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;

public class Curtain extends Utility {

	protected Paint mPaint = null;
	protected Paint mPaintText = null;
	protected float mUpper  = 0;
	protected float mBottom = 0;
	protected float mDelta  = 0;
	protected int mCloseDuration = 0;
	protected int mDelayDuration = 0;
	protected int mOpenDuration  = 0;
	protected OnCurtainEventListener mListener = null;
	
	public Curtain(Resources res) {
		super(res);
		setKind(CURTAIN);
		setMovable(false);
		setCollidable(false);
		setZOrder(ZOrders.CURTAIN);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.DKGRAY);
		mPaint.setStyle(Style.FILL);
		
		mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaintText.setColor(Color.WHITE);
		mPaintText.setStyle(Style.FILL);
		mPaintText.setTypeface(Typeface.SERIF);
		mPaintText.setFakeBoldText(true);
		mPaintText.setTextSize(40);
		mPaintText.setShadowLayer(4, 1, 1, Color.DKGRAY);
		mPaintText.setTextAlign(Paint.Align.CENTER);
	}
	
	public void setCurtainEventListener(OnCurtainEventListener listener) {
		mListener = listener;
	}
	
	public void setDelay(int delay) {
		mDelayDuration = delay;
	}
	
	public void close() {
		mCloseDuration = 1000;
		if (mListener != null) {
			mListener.onCurtainPreClosing();
		}
	}
	
	public void open() {
		mOpenDuration = 1000;
		if (mListener != null) {
			mListener.onCurtainPreOpening();
		}
	}

	@Override
	public void update() {
		if (mCloseDuration > 0) {
			mBottom += mDelta;
			mUpper  -= mDelta;
			mCloseDuration -= GameEngine.ENGINE_SPEED;		
			if (mUpper <= mBottom) {
				mCloseDuration = 0;
				mDelayDuration = 500;
				if (mListener != null) {
					mListener.onCurtainClosed();
				}
			}
		} else if (mDelayDuration > 0) {
			mDelayDuration -= GameEngine.ENGINE_SPEED;
		} else if (mOpenDuration > 0) {
			mBottom -= mDelta;
			mUpper  += mDelta;
			mOpenDuration -= GameEngine.ENGINE_SPEED;
			if (mBottom <= 0 && mUpper >= mHeight) {
				mOpenDuration = 0;
				if (mListener != null) {
					mListener.onCurtainOpened();
				}
			}
		}
	}

	@Override
	public void doDraw(Canvas c) {
		if (mCloseDuration == 0 && mDelayDuration == 0 && mOpenDuration == 0) {
			return;
		}
		c.drawRect(0, 0, mWidth, mBottom, mPaint);
		c.drawRect(0, mUpper, mWidth, mHeight, mPaint);
		if (mDelayDuration > 0) {
			float textWidth = mPaintText.measureText("Next Loop");			
			c.drawText("Next Loop", mWidth / 2, mHeight / 2, mPaintText);
		}
	}

	@Override
	public void onSizeChanged(int width, int height) {
		mWidth  = width;
		mHeight = height;
		
		mBottom = 0;
		mUpper  = mHeight;		
		mDelta  = mHeight / (1000f / GameEngine.ENGINE_SPEED / 2);		
	}

	public interface OnCurtainEventListener {
		void onCurtainPreClosing();
		void onCurtainClosed();
		void onCurtainPreOpening();
		void onCurtainOpened();
	}
}
