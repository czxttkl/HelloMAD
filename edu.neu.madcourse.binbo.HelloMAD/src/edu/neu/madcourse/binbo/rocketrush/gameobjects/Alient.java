package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Alient extends Barrier {	
	protected final static int IMAGE_COUNT = 12; // the same size of the total number of bitmaps
	protected static boolean sImageLoaded = false;	
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();			
	
	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient01));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient02));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient03));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient04));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient05));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient06));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient07));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient08));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient09));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient10));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient11));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.alient12));
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
		
		if (mCurIndex == IMAGE_COUNT) {
			mCurIndex = 0;
		}
		c.drawBitmap(sImages.get(mCurIndex++), mX, mY, null);	
	}	

	@Override
	public void update() {		
		if (mAccMoveDuration > 0) {
			mSpeedY = Math.min(mSpeedY + mAccSpeedY, mMaxSpeedY);
			mAccMoveDuration -= GameEngine.ENGINE_SPEED;
		} else {
			mSpeedY = Math.max(mSpeedY - mAccSpeedY, mMinSpeedY);
		}
		
		mX += mSpeedX;
		mY += mSpeedY;		
	}
}
