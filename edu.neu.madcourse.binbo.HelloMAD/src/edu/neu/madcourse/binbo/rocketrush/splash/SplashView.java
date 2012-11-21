package edu.neu.madcourse.binbo.rocketrush.splash;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class SplashView extends View {

	private int mProgress = 0;
	private Paint mPaint = null;  // used for testing only
	
	public SplashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextSize(40);
		mPaint.setTextAlign(Paint.Align.CENTER);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the background with bitmap
		
		// Draw the rocket
		
		// Draw the progress dots
		canvas.drawColor(Color.DKGRAY);

		int width  = getWidth();
		int height = getHeight();		
		canvas.drawText("Splash Screen", width / 2, height / 2, mPaint);
	}

	public void updateProgress(int progress) {
		mProgress = progress;
		invalidate();
	}
	
	public int getProgress() {
		return mProgress;
	}
}
