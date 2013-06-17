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

public class Bird extends Barrier {
	protected final static int IMAGE_COUNT = 2; // the same size of the total number of bitmaps
	protected static boolean sImageLoaded = false;
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();
	protected Bitmap mImage = null;		
	protected Random mRand = new Random();
	
	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inPurgeable = true;
        options.inPreferredConfig = Config.RGB_565;   
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.bird_1, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.bird_2, options));
	}
	
	public Bird(Resources res, boolean right) {
		super(res);
		loadImages(res);
		setKind(BIRD);
		setMovable(true);	
		setZOrder(ZOrders.BIRD);		
		setImage(sImages.get(right ? 1 : 0));
	}
	
	public void initSpeeds(float x, float y, int accTime) {		
		float accSpeedY = y / (1000 / GameEngine.ENGINE_SPEED);		
		setSpeed(x, y + accSpeedY * accTime);
		setMinSpeed(x, y);
		setMaxSpeed(x, y + y + y);
		setAccSpeed(0, accSpeedY);
	}

	public void setImage(Bitmap image) {
		mImage = image;
		setWidth(image.getWidth());
		setHeight(image.getHeight());
	}

	@Override
	public void doDraw(Canvas c) {
		if (mX + mWidth  <= 0 || mX >= mCanvasWidth ||
			mY + mHeight <= 0 || mY >= mCanvasHeight) {
			return; // not necessary to draw the invisible
		}
		c.drawBitmap(mImage, mX, mY, null);
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

	@Override
	public void triggerCollideEffect(int kind, float x, float y) {
		mSpeedX = -mSpeedX;
		setImage(sImages.get(mSpeedX > 0 ? 1 : 0));
	}

}
