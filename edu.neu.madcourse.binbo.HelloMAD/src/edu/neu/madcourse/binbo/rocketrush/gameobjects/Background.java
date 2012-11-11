package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Background extends GameObject {
	private Bitmap mImage = null;
	private int mImageHeight = 0;

	public Background(Bitmap image) {
		setImage(image);
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
		mImageHeight = mImage.getHeight();
	}

	@Override
	public int doDraw(Canvas c) {
		if (mY >= mImageHeight) {
			mY = 0;
			c.drawBitmap(mImage, mX, mY, null);
		} else {
			c.drawBitmap(mImage, mX, mY - mImageHeight, null);
			c.drawBitmap(mImage, mX, mY, null);
		}
		
		return super.doDraw(c);
	}

	@Override
	public void update() {
		mX += mSpeedX;
		mY += mSpeedY;
	}
}
