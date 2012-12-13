package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class SpeedBar extends Utility {
	protected Bitmap mImage = null;
	// canvas size
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	// bottom
	protected float mBarTop    = 0;
	protected float mBarBottom = 0;
	// bad name, but short : P
	protected Paint mPaint1 = new Paint();
	protected Paint mPaint2 = new Paint(); 
	protected Paint mPaint3 = new Paint();
	protected Paint mPaintShadow = new Paint();
	protected RectF r1 = new RectF();
	protected RectF r2 = new RectF();
	protected RectF r3 = new RectF();
	protected RectF rShadow = new RectF();
	// duration for move up, the same as Rocket
	protected int mUpDuration = 0;

	public SpeedBar(Resources res) {
		super(res);		
		setKind(SPEEDBAR);
		setSpeed(0, 1);
		setCollidable(false);
		setZOrder(ZOrders.SPEEDBAR);
		
		mPaint1.setAntiAlias(true);
		mPaint1.setARGB(180, 0, 0, 0);                 
		mPaint1.setStrokeWidth(4.0f);
		mPaint1.setStyle(Style.STROKE);
		
		mPaint2.setAntiAlias(true);		    
		mPaint2.setARGB(100, 255, 255, 255);
		mPaint2.setStyle(Style.FILL);
		
		mPaint3.setAntiAlias(true);		    
		mPaint3.setARGB(255, 86, 217, 7);
		mPaint3.setStyle(Style.FILL);
		
		mPaintShadow.setARGB(70, 0, 0, 0);
		mPaintShadow.setStrokeWidth(4.0f);
		mPaintShadow.setStyle(Style.STROKE);
		mPaintShadow.setAntiAlias(true);
	}
	
	public SpeedBar(Resources res, Bitmap image) {
		super(res);
		setKind(SPEEDBAR);
		setCollidable(false);
		setZOrder(ZOrders.SPEEDBAR);
		setImage(image);
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
	}

	@Override
	public void update() {		
		if (mUpDuration > 0) {
			mY = Math.max(mY - mSpeedY, mBarTop);
			mUpDuration -= GameEngine.ENGINE_SPEED;
		} else {
			mY = Math.min(mY + mSpeedY, mBarBottom);
			mUpDuration = 0;
		} 
		r3.top = mY;
	}
	
	@Override
	public void doDraw(Canvas c) {             	    
		c.drawRoundRect(rShadow, 15, 15, mPaintShadow);
		c.drawRoundRect(r1, 12, 12, mPaint1);	    	    
	    c.drawRoundRect(r2, 9, 9, mPaint2);	    
	    c.drawRoundRect(r3, 9, 9, mPaint3);
	}

	// too many magic numbers in the class, I will modify later. :P
	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;
		
		r1.left   = 30;
	    r1.top    = mCanvasHeight * 0.58f;
	    r1.right  = 50;
	    r1.bottom = mCanvasHeight * 0.9f;
	    
	    r2.left   = 32;
	    r2.top    = mCanvasHeight * 0.58f + 2;
	    r2.right  = 48;
	    r2.bottom = mCanvasHeight * 0.9f - 2;
	    
	    r3.left   = 32;
	    r3.top    = mCanvasHeight * 0.9f - 2;
	    r3.right  = 48;
	    r3.bottom = mCanvasHeight * 0.9f - 2;
	    
	    rShadow.left   = 26;
	    rShadow.top    = mCanvasHeight * 0.58f - 4;
	    rShadow.right  = 54;
	    rShadow.bottom = mCanvasHeight * 0.9f + 4;
	    
	    mBarTop    = mCanvasHeight * 0.58f + 2;
	    mBarBottom = mCanvasHeight * 0.9f - 2;
	    mY = mBarBottom;
	    setSpeed(0, (mBarBottom - mBarTop) / (3000f / GameEngine.ENGINE_SPEED));
	}

	@Override
	public void operate(GameCtrl ctrl) {	
		if (!mEnable) {
			return;
		}
		int command = ctrl.getCommand();

		if (command == GameCtrl.MOVE_VERT) {
			mUpDuration = 1000;
		}
	}
}
