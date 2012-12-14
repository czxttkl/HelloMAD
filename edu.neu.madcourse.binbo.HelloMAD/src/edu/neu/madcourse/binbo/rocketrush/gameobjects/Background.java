package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Background extends GameObject {
	protected Bitmap mImage = null;
	
	public Background(Resources res) {
		super(res);	
		setKind(BACKGROUND);
		setMovable(false);
		setCollidable(false);
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
		setWidth(mImage.getWidth());
		setHeight(mImage.getHeight());
	}

	@Override
	public void doDraw(Canvas c) {
		if (mY >= mHeight) {
			mY = 0;
			c.drawBitmap(mImage, mX, mY, null);
		} else {
			c.drawBitmap(mImage, mX, mY - mHeight, null);
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
