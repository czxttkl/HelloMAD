package edu.neu.madcourse.binbo.rocketrush.tutorial;

import edu.neu.madcourse.binbo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class TutorialView extends View {
	protected int mImageIndex = 0;
	protected Bitmap mImage = null;
	protected int mScreenWidth  = 0;
	protected int mScreenHeight = 0;	
//	private Paint  mPaint = null;  // used for testing only
//	private String mText  = "";    // used for testing only	
	
	public TutorialView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
//		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
//		mPaint.setColor(Color.BLUE);
//		mPaint.setStyle(Style.FILL);
//		mPaint.setTextSize(40);
//		mPaint.setTextAlign(Paint.Align.CENTER);		
	}

	public void onPause() {
		if (mImage != null) {
			mImage.recycle();
			mImage = null;
		}
		System.gc();
	}
	
//	public void setText(String text) {
//		mText = text;
//	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		if (mImage == null) {
			if (mImageIndex == 0) {
				mImage = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_1);
			} else if (mImageIndex == 1) {
				mImage = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_2);
			} else if (mImageIndex == 2) {
				mImage = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_3);
			} else if (mImageIndex == 3) {
				mImage = BitmapFactory.decodeResource(getResources(), R.drawable.tutorial_end);
			}
		}
		Rect rect = new Rect(0, 0, mScreenWidth, mScreenHeight);
		canvas.drawBitmap(mImage, null, rect, null); 			
//		mImage.recycle();
//		mImage = null;		
		System.gc();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		switch (getId()) {
		case R.id.tutorialView1:
			mImageIndex = 0;
			break;
		case R.id.tutorialView2:
			mImageIndex = 1;
			break;
		case R.id.tutorialView3:
			mImageIndex = 2;
			break;
		case R.id.tutorialEndView:
			mImageIndex = 3;
			break;	
		}
		
		mScreenWidth  = w;
		mScreenHeight = h;
//		int orginWidth  = sImages.get(mImageIndex).getWidth();
//		int orginHeight = sImages.get(mImageIndex).getHeight();
//		// scale the background according to the surface size
//		float radio = orginHeight / (float) orginWidth;	
//		int scaledWidth  = w;
//		int scaledHeight = (int)(w * radio);
//		
//		if (scaledWidth == orginWidth && scaledHeight == orginHeight) {
//			return;
//		}
//		
//		Bitmap scaledImage = 
//			Bitmap.createScaledBitmap(sImages.get(mImageIndex), scaledWidth, scaledHeight, false);	
//		sImages.get(mImageIndex).recycle();
//		sImages.set(mImageIndex, scaledImage);		
//		System.gc();
	}

}
