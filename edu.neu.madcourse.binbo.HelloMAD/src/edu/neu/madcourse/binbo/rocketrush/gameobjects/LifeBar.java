package edu.neu.madcourse.binbo.rocketrush.gameobjects;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Style;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class LifeBar extends Utility {
	// canvas size
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	// left and right of the bar
	protected int mBarLeft  = 0;
	protected int mBarRight = 0;
	// also bad name, but short : P
	protected Paint mPaint0 = new Paint();
	protected Paint mPaint2 = new Paint(); 
	protected Paint mPaint3 = new Paint();
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
		
		mPaint0.setColor(Color.BLACK);
		mPaint0.setStyle(Style.FILL);
		mPaint0.setAntiAlias(true);
		mPaint0.setFakeBoldText(true);
		mPaint0.setTextSize(28);

		mPaint2.setAntiAlias(true);		    
		mPaint2.setARGB(120, 255, 255, 255);
		mPaint2.setStyle(Style.FILL);
		
		mPaint3.setAntiAlias(true);		    
		mPaint3.setARGB(255, 86, 217, 7);
		mPaint3.setStyle(Style.FILL);
		
		r3.right = 78 + mWidth * mLife;
	}
	
	public float getLife() {
		return mLife;
	}

	public void lifeChange(float change) {
		mLife = Math.max(Math.min(mLife + change, 1f), 0);
		
		if (mLife <= 0.33) {
	    	mPaint3.setARGB(255, 230, 0, 0);
	    } else if (mLife <= 0.66) {
	    	mPaint3.setARGB(255, 255, 252, 0);
	    } else {
	    	mPaint3.setARGB(255, 86, 217, 7);
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
		if (!mEnable) {
			return;
		}
		r3.right = 78 + mWidth * mLife;
	}
	
	@Override
	public void doDraw(Canvas c) {		
		c.drawText("HP", 24, 80, mPaint0);   	    
	    c.drawRect(r2, mPaint2);	    
	    c.drawRect(r3, mPaint3);
	}

	// too many magic numbers in the class, I will modify later. :P
	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;
	    
	    r2.left   = 78;
	    r2.top    = 62;
	    r2.right  = 68 + width / 3f;
	    r2.bottom = 78;
	    
	    r3.left   = 78;
	    r3.top    = 62;
	    r3.right  = 68 + width / 3f;
	    r3.bottom = 78;
	    
	    mWidth = r3.right - r3.left;
	}

	public interface OnLifeChangedListener {
		void onLifeChanged(float life);
	}
}
