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
	
	protected final static int IMAGE_COUNT = 3; // the same size of the total number of bitmaps
	protected static boolean sImageLoaded = false;	
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();	
	
	private int mAccMoveDuration = 0;	
	public final static float DEFAULT_SPEED_X = 0;
	public final static float DEFAULT_SPEED_Y = 1f;	
	protected boolean mSwitching[] = { false, false };
	protected int mImageIndex[] = { 0, 0 };
	protected boolean mSubTransPart = false;
	protected int mTransPartHeight[] = { 0, 0, 0 };
	protected List<Rect> mRects = new ArrayList<Rect>();
	protected RectF mRectF = new RectF();
	
	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.bg1_far));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.bg2_far));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.bg3_far));
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
		mRectF = new RectF();
	}
	
	public void switchToNext() {
		mSwitching[0] = true;
		mSwitching[1] = true;
	}

	@Override
	public void onSizeChanged(int width, int height) {
		// scale the background according to the surface size				
		for (int i = 0; i < IMAGE_COUNT; ++i) {
			float radio = sImages.get(i).getHeight() / (float) sImages.get(i).getWidth();	
			int scaledWidth  = width;
			int scaledHeight = (int)(width * radio);
						
			if (i == 0) {
				mTransPartHeight[i] = 0; // no transition part for background 1
			} else {
				mTransPartHeight[i] = (int)(width * (200f / sImages.get(i).getWidth()));
			}
			
			Bitmap newImage = 
				Bitmap.createScaledBitmap(sImages.get(i), scaledWidth, scaledHeight, true);	
			sImages.get(i).recycle(); // explicit call to avoid out of memory
			sImages.set(i, newImage);
			
			// initialize rects for drawing
			Rect rect = new Rect(); 
			rect.left   = 0;
			rect.top    = newImage.getHeight() - mTransPartHeight[i];
			rect.right  = newImage.getWidth();
			rect.bottom = newImage.getHeight() - mTransPartHeight[i];
			mRects.add(rect);
		}
		mWidth  = sImages.get(0).getWidth();
		mHeight = sImages.get(0).getHeight();
		// initialize dest rect
		mRectF.left   = 0;
		mRectF.top    = 0;
		mRectF.right  = width;
		mRectF.bottom = 0;
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
		int maxHeight = sImages.get(mImageIndex[1]).getHeight() 
				- (mSubTransPart ? mTransPartHeight[mImageIndex[1]] : 0);
		
		if (mY >= maxHeight) {
			mY = 0;
			mRects.get(mImageIndex[1]).top = mRects.get(mImageIndex[1]).bottom; 
			mRectF.bottom = 0;
			if (mSwitching[0]) { // we need to draw the old first, so update mImageIndex[1]
				mImageIndex[1] = Math.min(mImageIndex[1] + 1, IMAGE_COUNT - 1);
				mSwitching[0] = false;
				mSubTransPart = false;
			} else if (mSwitching[1]) { // the old image has drawn up, update mImageIndex[0] to the new
				mImageIndex[0] = Math.min(mImageIndex[0] + 1, IMAGE_COUNT - 1);
				mSwitching[1] = false;
				mSubTransPart = true;
			}			
			c.drawBitmap(sImages.get(mImageIndex[0]), mX, mY, null);
		} else {
			if (!mSwitching[0] && mSwitching[1]) { // the time we still need to draw the old
				// the maxHeight here equals to the height of the bitmap with transition part
				c.drawBitmap(sImages.get(mImageIndex[1]), mX, mY - maxHeight, null);
			} else {
				// draw the bitmap without the transition part
				mRects.get(mImageIndex[1]).top = (int) (maxHeight - mY); 
				mRectF.bottom = mY;
				c.drawBitmap(sImages.get(mImageIndex[1]), mRects.get(mImageIndex[1]), mRectF, null);
			}
			// draw it directly, the transition part won't be displayed for this part  
			// because the non-transition part is longer than the height of the screen
			// based on the 16 : 9 screen. But if the screen's w/h radio is larger than
			// 16 : 9, the transition part may display, but it's very rare for the phone.
			c.drawBitmap(sImages.get(mImageIndex[0]), mX, mY, null);
		}

	}

	@Override
	public void release() {
		// TODO Auto-generated method stub
		super.release();
	}
	
}
