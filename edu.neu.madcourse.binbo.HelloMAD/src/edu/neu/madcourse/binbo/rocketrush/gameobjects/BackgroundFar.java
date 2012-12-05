package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.RectF;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;

public class BackgroundFar extends Background {
	
	protected final static int BACKGROUND_COUNT = 3; // the same size of the total number of bitmaps
	protected final static int TRANSITION_COUNT = 2;
	protected static boolean sImageLoaded = false;	
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();	
	
	private int mAccMoveDuration = 0;	
	public final static float DEFAULT_SPEED_X = 0;
	public final static float DEFAULT_SPEED_Y = 1f;	
	
	protected boolean mSwitching[] = { false, false };
	protected boolean mDrawTrans = false;
	protected int mImageIndex[] = { 0, 0 };
	protected int mTransIndex = BACKGROUND_COUNT - 1;
	
	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		BitmapFactory.Options options = new BitmapFactory.Options(); 
        options.inPurgeable = true;
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.bg1_far, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.bg2_far, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.bg3_far, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.b1_to_b2, options));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.b2_to_b3, options));
	}
	
	public BackgroundFar(Resources res) {
		super(res);
		setSpeed(DEFAULT_SPEED_X, DEFAULT_SPEED_Y);
		setMaxSpeed(0, DEFAULT_SPEED_Y * 3);
		setAccSpeed(0, DEFAULT_SPEED_Y / (1000 / GameEngine.ENGINE_SPEED));
		setZOrder(ZOrders.BACKGROUND_FAR);
		loadImages(res);
		setWidth(sImages.get(0).getWidth());
		setHeight(sImages.get(0).getHeight());
	}
	
	public void switchToNext() {
		mSwitching[0] = true;
		mSwitching[1] = true;
		mTransIndex = Math.min(mTransIndex + 1, BACKGROUND_COUNT + TRANSITION_COUNT - 1);
	}

	@Override
	public void onSizeChanged(int width, int height) {
		// scale the background according to the surface size				
		for (int i = 0; i < BACKGROUND_COUNT + TRANSITION_COUNT; ++i) {
			float radio = sImages.get(i).getHeight() / (float) sImages.get(i).getWidth();	
			int scaledWidth  = width;
			int scaledHeight = (int)(width * radio);
			
			if (scaledWidth == sImages.get(i).getWidth() && 
				scaledHeight == sImages.get(i).getHeight()) {
				continue;
			}
			
			Bitmap newImage = 
				Bitmap.createScaledBitmap(sImages.get(i), scaledWidth, scaledHeight, true);	
			sImages.get(i).recycle(); // explicit call to avoid out of memory
			sImages.set(i, newImage);

			System.gc();
		}
		mWidth  = sImages.get(0).getWidth();
		mHeight = sImages.get(0).getHeight();
	}
	
	@Override
	public void operate(GameCtrl ctrl) {
		int command = ctrl.getCommand();
		
		if (command == GameCtrl.MOVE_VERT) {
			mAccMoveDuration = 1000;
		} 
	}

	@Override
	public void update() {
		if (mAccMoveDuration > 0) {
			mSpeedY = Math.min(mSpeedY + mAccSpeedY, mMaxSpeedY);
			mY += mSpeedY;
			mAccMoveDuration -= GameEngine.ENGINE_SPEED;
		} else {
			mSpeedY = Math.max(mSpeedY - mAccSpeedY, DEFAULT_SPEED_Y);
			mY += mSpeedY;
		}
	}

	@Override
	public void doDraw(Canvas c) {	
		int transHeight = sImages.get(mTransIndex).getHeight();
		int maxHeight = sImages.get(mImageIndex[1]).getHeight() + (mDrawTrans ? transHeight : 0);
		
		if (mY >= maxHeight) {
			mY = 0;
			if (mSwitching[0]) { // we need to draw the old first, so update mImageIndex[1]
				mImageIndex[1] = Math.min(mImageIndex[1] + 1, BACKGROUND_COUNT - 1);
				mSwitching[0] = false;
				mDrawTrans = true;
			} else if (mSwitching[1]) { // the old image has drawn up, update mImageIndex[0] to the new
				mImageIndex[0] = Math.min(mImageIndex[0] + 1, BACKGROUND_COUNT - 1);
				mSwitching[1] = false;
				mDrawTrans = false;
			}			
			c.drawBitmap(sImages.get(mImageIndex[0]), mX, mY, null);
		} else {
			c.drawBitmap(sImages.get(mImageIndex[1]), mX, mY - maxHeight, null);
			if (mDrawTrans) {
				c.drawBitmap(sImages.get(mTransIndex), mX, mY - transHeight, null);
			}			
			c.drawBitmap(sImages.get(mImageIndex[0]), mX, mY, null);
		}

	}

	@Override
	public void release() {
		super.release();
	}
	
}
