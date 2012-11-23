package edu.neu.madcourse.binbo.rocketrush.splash;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
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

	private int mNext = 0;
	private int mProgress = 0;
	private int mRocketWidth = 0;
	private Paint mPaint = null;	
	private Bitmap mBackground = null;
	private List<Bitmap> mRocketHori = new ArrayList<Bitmap>();
	
	public SplashView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.GRAY);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextSize(30);
		mPaint.setTextAlign(Paint.Align.CENTER);
		
		Resources res = context.getResources();
		mBackground = BitmapFactory.decodeResource(
				res, R.drawable.splash);
		mRocketHori.add(BitmapFactory.decodeResource(res, R.drawable.ship2_1_hori));
		mRocketHori.add(BitmapFactory.decodeResource(res, R.drawable.ship2_2_hori));
		mRocketHori.add(BitmapFactory.decodeResource(res, R.drawable.ship2_3_hori));
		mRocketHori.add(BitmapFactory.decodeResource(res, R.drawable.ship2_4_hori));
		mRocketWidth = mRocketHori.get(0).getWidth();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width  = getWidth();
		int height = getHeight();	
		
		// Draw the background
		canvas.drawBitmap(mBackground, 0, 0, null);
				
		// Draw the rocket
		float left = (width - mRocketWidth) * (mProgress / 100f);
		float top  = height / 20 * 11;		
		canvas.drawBitmap(mRocketHori.get(mNext++ % 4), left, top, null);
		
		// Draw the loading text	
		String progress = "" + mProgress + "%";
		while (progress.length() < 4) {
			progress = " " + progress;
		}
		canvas.drawText("Loading..." + progress, width / 2, height / 2, mPaint);
	}

	public void updateProgress(int progress) {
		mProgress = progress;
		invalidate();
	}
	
	public int getProgress() {
		return mProgress;
	}
	
	public void release() {
		if (mBackground != null) {
			mBackground.recycle();
			mBackground = null;
		}
		for (Bitmap image : mRocketHori) {
			if (image != null) {
				image.recycle();
				image = null;
			}			
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if (mBackground == null) return;
		// scale the background according to the view size
		Bitmap newImage = 
			Bitmap.createScaledBitmap(mBackground, w, h, true);	
		mBackground.recycle(); // explicit call to avoid out of memory
		mBackground = newImage;
	}
}
