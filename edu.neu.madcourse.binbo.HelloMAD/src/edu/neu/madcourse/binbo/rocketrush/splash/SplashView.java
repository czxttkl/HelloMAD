package edu.neu.madcourse.binbo.rocketrush.splash;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
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
		mPaint.setColor(0xff404040);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextSize(28);
		mPaint.setTextAlign(Paint.Align.CENTER);

		Options options = new Options();
		options.inPurgeable = true;
		options.inPreferredConfig = Config.RGB_565;
		InputStream in = null;
		try {
			in = context.getAssets().open("splash.png");
			mBackground = BitmapFactory.decodeStream(in, null, options);
		} catch (IOException e) {
			throw new RuntimeException("");
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					
				}
			}
		}		

		Resources res = context.getResources();
		mRocketHori.add(BitmapFactory.decodeResource(res, R.drawable.ship2_1_hori, options));
		mRocketHori.add(BitmapFactory.decodeResource(res, R.drawable.ship2_2_hori, options));
		mRocketHori.add(BitmapFactory.decodeResource(res, R.drawable.ship2_3_hori, options));
		mRocketHori.add(BitmapFactory.decodeResource(res, R.drawable.ship2_4_hori, options));
		mRocketWidth = mRocketHori.get(0).getWidth();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int width  = canvas.getWidth();
		int height = canvas.getHeight();	
		
		// Draw the background
		Rect rect = new Rect(0, 0, width, height);
		canvas.drawBitmap(mBackground, null, rect, null);
				
		// Draw the rocket
		float left = (width - mRocketWidth) * (mProgress / 100f);
		float top  = height / 20 * 12;		
		canvas.drawBitmap(mRocketHori.get(mNext++ % 4), left, top, null);
		
		// Draw the loading text	
		String progress = "" + mProgress + "%";
		while (progress.length() < 4) {
			progress = " " + progress;
		}
		
		canvas.drawText("Loading..." + progress, width / 2, height / 20f * 11, mPaint);
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
		System.gc();
	}
}
