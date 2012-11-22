package edu.neu.madcourse.binbo.rocketrush.splash;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;
import edu.neu.madcourse.binbo.R;

public class SplashView extends View {

	private int mProgress = 0;
//	private Paint mPaint = null;  // used for testing only
	private Bitmap mImage = null;
	
	public SplashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
//		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		mPaint.setColor(Color.BLUE);
//		mPaint.setStyle(Style.FILL);
//		mPaint.setTextSize(40);
//		mPaint.setTextAlign(Paint.Align.CENTER);
		
		mImage = BitmapFactory.decodeResource(
				context.getResources(), R.drawable.splash);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// Draw the background with bitmap
		canvas.drawBitmap(mImage, 0, 0, null);
		
		// Draw the rocket
		
		// Draw the progress dots
		/*canvas.drawColor(Color.DKGRAY);

		int width  = getWidth();
		int height = getHeight();		
		canvas.drawText("Splash Screen", width / 2, height / 2, mPaint);*/
	}

	public void updateProgress(int progress) {
		mProgress = progress;
		invalidate();
	}
	
	public int getProgress() {
		return mProgress;
	}
	
	public void release() {
		if (mImage != null) {
			mImage.recycle();
			mImage = null;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (mImage == null) return;
		// scale the background according to the view size
		Bitmap newImage = 
			Bitmap.createScaledBitmap(mImage, w, h, true);	
		mImage.recycle(); // explicit call to avoid out of memory
		mImage = newImage;
	}
}
