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

public class SpeedBar extends GameObject implements GameObject.IDrawer {
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
	protected RectF r1 = new RectF();
	protected RectF r2 = new RectF();
	protected RectF r3 = new RectF();
	// duration for move up, the same as Rocket
	protected int mUpDuration = 0;

	public SpeedBar(Resources res) {
		super(res);		
		setKind(SPEEDBAR);
		setSpeed(0, 1);
		setZOrder(ZOrders.SPEEDBAR);
		
		mPaint1.setAntiAlias(true);
		mPaint1.setColor(Color.BLACK);                   
		mPaint1.setStrokeWidth(3.0f);
		mPaint1.setStyle(Style.STROKE);
		
		mPaint2.setAntiAlias(true);		    
		mPaint2.setColor(Color.argb(0x80, 0xCC, 0xCC, 0xCC));
		mPaint2.setStyle(Style.FILL); 
		
		mPaint3.setAntiAlias(true);		    
		mPaint3.setColor(Color.argb(0xFF, 0x00, 0xCC, 0x00));
		mPaint3.setStyle(Style.FILL);
		
		r3.top = 0;
	}
	
	public SpeedBar(Resources res, Bitmap image) {
		super(res);
		setKind(SPEEDBAR);
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
	    r1.top    = mCanvasHeight * 0.65f;
	    r1.right  = 54;
	    r1.bottom = mCanvasHeight * 0.9f;
	    
	    r2.left   = 32;
	    r2.top    = mCanvasHeight * 0.65f + 2;
	    r2.right  = 52;
	    r2.bottom = mCanvasHeight * 0.9f - 2;
	    
	    r3.left   = 32;
	    r3.top    = 0;
	    r3.right  = 52;
	    r3.bottom = mCanvasHeight * 0.9f - 2;;
	    
	    mBarTop    = mCanvasHeight * 0.65f + 2;
	    mBarBottom = mCanvasHeight * 0.9f - 2;   
	    mY = mBarBottom;	   
	    setSpeed(0, (mBarBottom - mBarTop) / (3000f / GameEngine.ENGINE_SPEED));
	}

	@Override
	public void operate(GameCtrl ctrl) {
		int command = ctrl.getCommand();

		if (command == GameCtrl.MOVE_VERT) {
			mUpDuration = 1000;
		}
	}
}
