package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.List;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class LifeBar extends GameObject {
	// canvas size
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	// left and right of the bar
	protected int mBarLeft  = 0;
	protected int mBarRight = 0;
	// also bad name, but short : P
	protected Paint mPaint0 = new Paint();
	protected Paint mPaint1 = new Paint();
	protected Paint mPaint2 = new Paint(); 
	protected Paint mPaint3 = new Paint();
	protected RectF r1 = new RectF();
	protected RectF r2 = new RectF();
	protected RectF r3 = new RectF();
	// current life
	protected float mLife = 1f;
	// OnLifeChangedListener
	protected OnLifeChangedListener mLifeChangedListener = null; 

	public LifeBar(Resources res) {
		super(res);
		setKind(LIFEBAR);
		setZOrder(ZOrders.LIFEBAR);
		
		mPaint0 = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint0.setColor(0xff404040);
		mPaint0.setStyle(Style.FILL);	
		mPaint0.setFakeBoldText(true);
		mPaint0.setTextSize(28);
		
		mPaint1.setAntiAlias(true);
		mPaint1.setColor(Color.BLACK);                   
		mPaint1.setStrokeWidth(3.0f);
		mPaint1.setStyle(Style.STROKE);
		
		mPaint2.setAntiAlias(true);		    
		mPaint2.setColor(0x80cccccc);
		mPaint2.setStyle(Style.FILL); 
		
		mPaint3.setAntiAlias(true);		    
		mPaint3.setColor(0xff00cc00);
		mPaint3.setStyle(Style.FILL); 
		
		r3.right = 68 + mWidth * mLife;
	}
	
	public float getLife() {
		return mLife;
	}

	public void lifeChange(float change) {
		mLife = Math.max(Math.min(mLife + change, 1f), 0);
		
		if (mLife <= 0.33) {
	    	mPaint3.setColor(Color.RED);
	    } else if (mLife <= 0.66) {
	    	mPaint3.setColor(Color.YELLOW);
	    } else {
	    	mPaint3.setColor(0xff00cc00);
	    }
		
		if (mLifeChangedListener != null) {
			mLifeChangedListener.onLifeChanged(mLife);
		}
	}	
	
	public void setOnLifeChangedListener(OnLifeChangedListener listener) {
		mLifeChangedListener = listener;
	}
	
	@Override
	public void update() {		
		r3.right = 68 + mWidth * mLife;
	}
	
	@Override
	public void doDraw(Canvas c) {		
		c.drawText("HP", 16, 72, mPaint0);
	    c.drawRoundRect(r1, 12, 12, mPaint1);	    	    
	    c.drawRoundRect(r2, 9, 9, mPaint2);	    
	    c.drawRoundRect(r3, 9, 9, mPaint3);
	}

	// too many magic numbers in the class, I will modify later. :P
	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;
		
		r1.left   = 66;
	    r1.top    = 50;
	    r1.right  = 69 + width / 3.6f;
	    r1.bottom = 75;
	    
	    r2.left   = 68;
	    r2.top    = 52;
	    r2.right  = 70 + width / 3.6f;
	    r2.bottom = 73;
	    
	    r3.left   = 68;
	    r3.top    = 52;
	    r3.right  = 67 + width / 3.6f;
	    r3.bottom = 73;
	    
	    mWidth = r3.right - r3.left;
	}

	public interface OnLifeChangedListener {
		void onLifeChanged(float life);
	}
}
