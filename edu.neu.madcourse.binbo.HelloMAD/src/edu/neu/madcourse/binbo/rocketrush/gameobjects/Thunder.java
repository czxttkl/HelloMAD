package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.R;

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
		
		BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inPurgeable = true;
        options.inPreferredConfig = Config.RGB_565; 
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.thunder, options));
	}

	public Thunder(Resources res) {
		super(res);		
		setKind(THUNDER);
		setMovable(true);	
		setVisible(false);
		setCollidable(false);
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
		
		if (mVisible && (mPeriod & 3) != 0) {		
			c.drawBitmap(sImages.get(0), mX, mY, null);			
		}
	}	

	@Override
	public void update() {			
		if (++mPeriod == 60) {
			mVisible = true;
			mCollidable = true;
		} else if (mPeriod == 100) {
			mPeriod = 0;
			mVisible = false;
			mCollidable = false;
		}
		mY += mSpeedY;		
	}
}
