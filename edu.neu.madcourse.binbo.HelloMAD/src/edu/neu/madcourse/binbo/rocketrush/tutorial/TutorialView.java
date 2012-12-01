package edu.neu.madcourse.binbo.rocketrush.tutorial;

import edu.neu.madcourse.binbo.R;
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

public class TutorialView extends View {
	private Bitmap mImage = null;
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
	
	public void setImage(Bitmap image) {
		mImage = image;
	}
	
	public void onPause() {
		if (mImage != null) {
			mImage.recycle();
			mImage = null;
		}
	}
	
//	public void setText(String text) {
//		mText = text;
//	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		if (mImage != null) {		
			canvas.drawBitmap(mImage, 0, 0, null);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		Bitmap image = null;
		Resources res = getResources();		
		
		switch (getId()) {
		case R.id.tutorialView1:
			image = BitmapFactory.decodeResource(res, R.drawable.tutorial_1);
			break;
		case R.id.tutorialView2:
			image = BitmapFactory.decodeResource(res, R.drawable.tutorial_2);
			break;
		case R.id.tutorialView3:
			image = BitmapFactory.decodeResource(res, R.drawable.tutorial_3);
			break;	
		default:
			break;
		}
		
		Bitmap newImage = Bitmap.createScaledBitmap(image, w, h, true);	
		image.recycle();
		image = null;
		setImage(newImage);
		
		super.onSizeChanged(w, h, oldw, oldh);
	}

}