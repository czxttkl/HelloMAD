package edu.neu.madcourse.binbo.rocketrush.tutorial;

import edu.neu.madcourse.binbo.rocketrush.tutorial.TutorialActivity.OnTutorialChangedListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ProgressView extends View implements OnTutorialChangedListener {
	
	public ProgressView(Context context) {
		super(context);
		
		mPaint.setAntiAlias(true);		    
		mPaint.setARGB(150, 240, 240, 240);
		mPaint.setStyle(Style.FILL);
		
		mPaintFocus.setAntiAlias(true);		    
		mPaintFocus.setARGB(255, 255, 255, 255);
		mPaintFocus.setStyle(Style.FILL);
	}
	
	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mPaint.setAntiAlias(true);		    
		mPaint.setARGB(150, 240, 240, 240);
		mPaint.setStyle(Style.FILL);
		
		mPaintFocus.setAntiAlias(true);		    
		mPaintFocus.setARGB(255, 255, 255, 255);
		mPaintFocus.setStyle(Style.FILL);
	}

	protected int mPosition = 0;
	protected Paint mPaint = new Paint();
	protected Paint mPaintFocus = new Paint();


	@Override
	protected void onDraw(Canvas canvas) {
		int width  = getWidth();
		int height = getHeight();
		// draw the dots
		canvas.drawCircle(width / 2 - 40, height * 0.9f, 8, mPosition == 0 ? mPaintFocus : mPaint);
		canvas.drawCircle(width / 2, height * 0.9f, 8, mPosition == 1 ? mPaintFocus : mPaint);
		canvas.drawCircle(width / 2 + 40, height * 0.9f, 8, mPosition == 2 ? mPaintFocus : mPaint);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.onTouchEvent(event);		
	}

	public void OnTutorialChanged(int position) {
		mPosition = position;
		invalidate();
	}

	
}
