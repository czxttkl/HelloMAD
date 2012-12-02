package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.binbo.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Field extends Reward {
	protected final static int IMAGE_COUNT = 2; // the same size of the total number of bitmaps
	protected final static int IMAGE_UNBOUND_START = 0; 
	protected final static int IMAGE_BOUND_START   = 1;
	protected static boolean sImageLoaded = false;	
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();
	// the difference of the top left point between this field and the rocket 
	protected float mOffsetX = 0;
	protected float mOffsetY = 0;	
	
	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient01));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient02));
	}
	
	public Field(Resources res) {
		super(res);
		loadImages(res);
		setWidth(sImages.get(IMAGE_UNBOUND_START).getWidth());
		setHeight(sImages.get(IMAGE_UNBOUND_START).getHeight());	
	}

	@Override
	protected void updateBound() {
		if (isTimeout()) {
			// just make sure it's out of the screen, 
			// then it can be released in the next loop
			mX = -10000;
			mY = -10000;
			return;
		}
		
		mX = mRocket.getX() - mOffsetX;
		mY = mRocket.getY() - mOffsetY;
	}

	@Override
	protected void updateUnbound() {
		mX += mSpeedX;
		mY += mSpeedY;		
		
		if (isTimeout()) {
			return; // the reward will fly out of the screen
		}
		
		if (mX < 0) {
			mSpeedX = Math.abs(mSpeedX);
		} else if (mX > mCanvasWidth - mWidth) {
			mSpeedX = -Math.abs(mSpeedX);
		}		
		if (mY < 0) {
			mSpeedY = Math.abs(mSpeedY);
		} else if (mY > mCanvasHeight - mHeight) {
			mSpeedY = -Math.abs(mSpeedY);
		}	
	}

	protected int mBoundIndex = 0;
	@Override
	protected void drawBound(Canvas c) {
		if (mBoundIndex == sImages.size()) {
			mBoundIndex = IMAGE_BOUND_START;
		}
		c.drawBitmap(sImages.get(mBoundIndex++), mX, mY, null);
	}

	protected int mUnboundIndex = 0;
	@Override
	protected void drawUnbound(Canvas c) {
		if (mX + mWidth  <= 0 || mX >= mCanvasWidth ||
			mY + mHeight <= 0 || mY >= mCanvasHeight) {
			return; // not necessary to draw the invisible
		}
		
		if (mUnboundIndex == IMAGE_BOUND_START) {
			mUnboundIndex = 0;
		}
		c.drawBitmap(sImages.get(mUnboundIndex++), mX, mY, null);	
	}

	@Override
	protected void onBound() {
		setWidth(sImages.get(IMAGE_BOUND_START).getWidth());
		setHeight(sImages.get(IMAGE_BOUND_START).getHeight());	
		mOffsetX = (mWidth - mRocket.getWidth()) * 0.5f;
		mOffsetY = (mHeight - mRocket.getHeight()) * 0.5f;
	}
}
