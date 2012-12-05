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
		Runtime.getRuntime().gc();
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
		super.onSizeChanged(w, h, oldw, oldh);
		
		Bitmap image = null;
		Resources res = getResources();
		BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inPurgeable = true;
        
		switch (getId()) {
		case R.id.tutorialView1:
			image = BitmapFactory.decodeResource(res, R.drawable.tutorial_1, options);
			break;
		case R.id.tutorialView2:
			image = BitmapFactory.decodeResource(res, R.drawable.tutorial_2, options);
			break;
		case R.id.tutorialView3:
			image = BitmapFactory.decodeResource(res, R.drawable.tutorial_3, options);
			break;	
		default:
			break;
		}
		
		// scale the background according to the surface size
		float radio = image.getHeight() / (float) image.getWidth();	
		int scaledWidth  = w;
		int scaledHeight = (int)(w * radio);
		
		Bitmap scaledImage = 
			Bitmap.createScaledBitmap(image, scaledWidth, scaledHeight, false);	
		image.recycle();
		image = null;
		setImage(scaledImage);
		
		Runtime.getRuntime().gc();
	}

}
