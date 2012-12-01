package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Thunder extends Barrier {
	protected final static int IMAGE_COUNT = 1; // the same size of the total number of bitmaps
	protected static boolean sImageLoaded = false;	
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();	
	protected int mPeriod = 0;
	
	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.thunder));
	}

	public Thunder(Resources res) {
		super(res);		
		setKind(THUNDER);
		setMovable(true);	
		setZOrder(ZOrders.THUNDER);
		loadImages(res);
		setWidth(sImages.get(0).getWidth());
		setHeight(sImages.get(0).getHeight());	
	}

	public void initSpeeds(float x, float y, int accTime) {					
		setSpeed(x, y);
		setMinSpeed(x, y);
		setMaxSpeed(x, y);
		setAccSpeed(0, 0);
	}

	@Override
	public void doDraw(Canvas c) {
		if (mX + mWidth  <= 0 || mX >= mCanvasWidth ||
			mY + mHeight <= 0 || mY >= mCanvasHeight) {
			return; // not necessary to draw the invisible
		}
		
		if (mPeriod <= 75) {
			;
		} else {
			if ((mPeriod & 3) != 0) {
				c.drawBitmap(sImages.get(0), mX, mY, null);
			}
		}
	}	

	@Override
	public void update() {	
		mPeriod = (mPeriod <= 100) ? mPeriod + 1 : 0;
		mY += mSpeedY;		
	}
}
