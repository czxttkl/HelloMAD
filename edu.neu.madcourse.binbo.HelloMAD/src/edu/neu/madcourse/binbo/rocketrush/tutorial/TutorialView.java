package edu.neu.madcourse.binbo.rocketrush.tutorial;

import edu.neu.madcourse.binbo.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

public class TutorialView extends View {
	private Bitmap mImage = null;
	private Paint  mPaint = null;
	private String mText  = ""; // used for testing only
	
	public TutorialView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(Color.BLUE);
		mPaint.setStyle(Style.FILL);
		mPaint.setTextSize(40);
		mPaint.setTextAlign(Paint.Align.CENTER);		
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
	}
	
	public void setText(String text) {
		mText = text;
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		int width  = getWidth();
		int height = getHeight();
		
		canvas.drawText(mText, width / 2, height / 2, mPaint);
	}

}
