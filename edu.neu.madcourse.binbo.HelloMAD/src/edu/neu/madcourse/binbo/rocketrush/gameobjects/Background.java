package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Background extends GameObject {
	protected Bitmap mImage = null;
	protected int mImageWidth  = 0;
	protected int mImageHeight = 0;
	
	public Background(Resources res) {
		super(res);
		setMovable(false);
	}

	public Background(Resources res, Bitmap image) {
		super(res);
		setImage(image);
		setMovable(false);
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
		mImageWidth  = mImage.getWidth();
		mImageHeight = mImage.getHeight();
	}

	@Override
	public void doDraw(Canvas c) {
		if (mY >= mImageHeight) {
			mY = 0;
			c.drawBitmap(mImage, mX, mY, null);
		} else {
			c.drawBitmap(mImage, mX, mY - mImageHeight, null);
			c.drawBitmap(mImage, mX, mY, null);
		}
	}

	@Override
	public void update() {
		mX += mSpeedX;
		mY += mSpeedY;
	}

	@Override
	public void release() {
		if (mImage != null) {
			mImage.recycle();
			mImage = null;
		}
		super.release();
	}
}
