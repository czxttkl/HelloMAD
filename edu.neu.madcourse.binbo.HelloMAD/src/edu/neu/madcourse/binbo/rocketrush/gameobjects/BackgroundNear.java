package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.neu.madcourse.binbo.R;

public class BackgroundNear extends Background {

	public BackgroundNear(Resources res) {
		super(res);
		mSpeedX = 0;
		mSpeedY = 3;
		setImage(BitmapFactory.decodeResource(res, R.drawable.background_b));
	}
	
	public BackgroundNear(Resources res, Bitmap image) {
		super(res);
		mSpeedX = 0;
		mSpeedY = 3;
		setImage(image);
	}

	@Override
	public void onSizeChanged(int width, int height) {
		if (mImage == null) return;
		// scale the background according to the surface size
		float radio = mImageHeight / mImageWidth;	
		int scaledWidth  = width;
		int scaledHeight = (int)(width * radio);
		scaledHeight += 3 - scaledHeight % 3; // 3 is the same as mSpeedY
		
		Bitmap newImage = 
			Bitmap.createScaledBitmap(mImage, scaledWidth, scaledHeight, true);	
		mImage.recycle(); // explicit call to avoid out of memory
		setImage(newImage);
	}
}
