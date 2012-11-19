package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.neu.madcourse.binbo.R;

public class BackgroundFar extends Background {
	
	public BackgroundFar(Resources res) {
		super(res);
		setSpeed(0, 1);
		setZOrder(ZOrders.BACKGROUND_FAR);
		setImage(BitmapFactory.decodeResource(res, R.drawable.background_a));
	}
	
	public BackgroundFar(Resources res, Bitmap image) {
		super(res);
		setSpeed(0, 1);
		setZOrder(ZOrders.BACKGROUND_FAR);
		setImage(image);
	}
	
	@Override
	public void onSizeChanged(int width, int height) {
		if (mImage == null) return;
		// scale the background according to the surface size
		float radio = mHeight / (float) mWidth;	
		int scaledWidth  = width;
		int scaledHeight = (int)(width * radio);
		
		Bitmap newImage = 
			Bitmap.createScaledBitmap(mImage, scaledWidth, scaledHeight, true);	
		mImage.recycle(); // explicit call to avoid out of memory
		setImage(newImage);	
	}
}
