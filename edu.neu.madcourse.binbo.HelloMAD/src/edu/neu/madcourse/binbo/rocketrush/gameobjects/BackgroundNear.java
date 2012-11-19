package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.neu.madcourse.binbo.R;

public class BackgroundNear extends Background {

	public BackgroundNear(Resources res) {
		super(res);
		setSpeed(0, 2);
		setZOrder(ZOrders.BACKGROUND_NEAR);
		setImage(BitmapFactory.decodeResource(res, R.drawable.background_b));
	}
	
	public BackgroundNear(Resources res, Bitmap image) {
		super(res);
		setSpeed(0, 2);
		setZOrder(ZOrders.BACKGROUND_NEAR);
		setImage(image);
	}

	@Override
	public void onSizeChanged(int width, int height) {
		if (mImage == null) return;
		// scale the background according to the surface size
		float radio = mHeight / (float) mWidth;	
		int scaledWidth  = width;
		int scaledHeight = (int)(width * radio);
		scaledHeight += mSpeedY - scaledHeight % mSpeedY;
		
		Bitmap newImage = 
			Bitmap.createScaledBitmap(mImage, scaledWidth, scaledHeight, true);	
		mImage.recycle(); // explicit call to avoid out of memory
		setImage(newImage);
	}
}
