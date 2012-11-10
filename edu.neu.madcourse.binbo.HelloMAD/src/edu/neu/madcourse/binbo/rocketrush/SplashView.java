package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class SplashView extends View {

	private int mProgress = 0;
	
	public SplashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the background with bitmap
		
		// Draw the rocket
		
		// Draw the progress dots
		
	}

	public void updateProgress(int progress) {
		mProgress = progress;
		invalidate();
	}
	
	public int getProgress() {
		return mProgress;
	}
}
