package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;

public class Alient extends Barrier {	
	protected final static int IMAGE_COUNT = 12; // the same size of the total number of bitmaps
	protected static boolean sImageLoaded = false;	
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();	
	protected Random mRand = new Random();
	protected boolean mSpeedUnchangeable = false;
	
	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inPurgeable = true;
        options.inPreferredConfig = Config.RGB_565;
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient01, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient02, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient03, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient04, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient05, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient06, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient07, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient08, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient09, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient10, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient11, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient12, options));
	}
	
	public Alient(Resources res) {
		super(res);		
		setKind(ALIENT);
		setMovable(true);	
		setZOrder(ZOrders.ALIENT);
		loadImages(res);
		setWidth(sImages.get(0).getWidth());
		setHeight(sImages.get(0).getHeight());	
	}

	public void initSpeeds(float x, float y, int accTime) {		
		float accSpeedY = y / (1000 / GameEngine.ENGINE_SPEED);			
		setSpeed(x, y + accSpeedY * accTime);
		setMinSpeed(x, y);
		setMaxSpeed(x, y * 3);
		setAccSpeed(0, accSpeedY);
	}

	protected int mCurIndex = 0;
	@Override
	public void doDraw(Canvas c) {
		if (mX + mWidth  <= 0 || mX >= mCanvasWidth ||
			mY + mHeight <= 0 || mY >= mCanvasHeight) {
			return; // not necessary to draw the invisible
		}
		
		if (mCurIndex == (IMAGE_COUNT << 1)) {
			mCurIndex = 0;
		}
		c.drawBitmap(sImages.get(mCurIndex++ >> 1), mX, mY, null);	
	}	

	@Override
	public void update() {	
		if (!mSpeedUnchangeable) {
			if (mAccMoveDuration > 0) {
				mSpeedY = Math.min(mSpeedY + mAccSpeedY, mMaxSpeedY);
				mAccMoveDuration -= GameEngine.ENGINE_SPEED;
			} else {
				mSpeedY = Math.max(mSpeedY - mAccSpeedY, mMinSpeedY);
			}
		}

		mX += mSpeedX;
		mY += mSpeedY;		
	}

	@Override
	public void triggerCollideEffect(int kind, float x, float y) {
		float cX = mX + mWidth * 0.5f;
		float cY = mY + mHeight * 0.5f;
		float offsetY = 0;
		
		if (kind == ROCKET) {
			offsetY = 10;
		} else if (kind == PROTECTION) {
			offsetY = 16;
		}
				
		if (cX <= x && cY <= y - offsetY) {
			mSpeedX = -8;
			mSpeedY = -8;
		} else if (cX <= x && cY <= y + offsetY) {
			mSpeedX = -12;
			mSpeedY = (mRand.nextInt(2) == 0 ? 2 : -2);
		} else if (cX <= x && cY > y + offsetY) {
			mSpeedX = -8;
			mSpeedY = 8;
		} else if (cX > x && cY <= y - offsetY) {
			mSpeedX = 8;
			mSpeedY = -8;
		} else if (cX > x && cY <= y + offsetY) {
			mSpeedX = 12;
			mSpeedY = (mRand.nextInt(2) == 0 ? 2 : -2);
		} else {
			mSpeedX = 8;
			mSpeedY = 8;
		}
		
		mSpeedUnchangeable = true;
	}
}
