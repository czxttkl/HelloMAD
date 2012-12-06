package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Rocket extends GameObject implements GameObject.IDrawer  {
	protected final static int IMAGE_COUNT = 4; // the same size of the total number of bitmaps
	protected static boolean sImageLoaded = false;	
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();	
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	protected int mLeftDuration  = 0;
	protected int mRightDuration = 0;
	protected int mUpDuration    = 0;
	protected int mVibrateDuration = 0;	
	protected int mVibrateCount = 0;
	protected final static int MIN_VIBRATE_DURATION = 160;
	protected final static int MAX_VIBRATE_DURATION = 240;
	protected float mUpper  = 0;
	protected float mBottom = 0;	
	protected float mCollideArea[] = new float[4];
	public final static float DEFAULT_SPEED_X = 8;
	public final static float DEFAULT_SPEED_Y = 4;
	// rocket's area used to detect collision
	protected Rect mRect = new Rect();
	protected List<GameObject> mCollideWith = new ArrayList<GameObject>();
	// reward list notes the rewards bounding to this rocket
	protected List<Reward> mRewards = new ArrayList<Reward>();

	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.ship2_1));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.ship2_2));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.ship2_3));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.ship2_4));
	}
	
	public Rocket(Resources res) {
		super(res);
		setKind(ROCKET);
		setMovable(true);	
		setSpeed(DEFAULT_SPEED_X, DEFAULT_SPEED_Y);
		setMaxSpeed(DEFAULT_SPEED_X, DEFAULT_SPEED_Y);			
		setZOrder(ZOrders.ROCKET);
		loadImages(res);
		setWidth(sImages.get(0).getWidth());
		setHeight(sImages.get(0).getHeight());				
	}

	public int getAccTime() {
		return (int)((mBottom - mY) / mSpeedY);
	}
	
	public void bindReward(Reward reward) {
		reward.setSpeed(mSpeedX, mSpeedY);
		reward.setMinSpeed(mMinSpeedX, mMinSpeedY);
		reward.setMaxSpeed(mMaxSpeedX, mMaxSpeedY);
		reward.setAccSpeed(mAccSpeedX, mAccSpeedY);
		mRewards.add(reward);
	}
	
	public void unbindReward(Reward reward) {
		mRewards.remove(reward);
	}

	protected int mCurIndex = 0;
	@Override
	public void doDraw(Canvas c) {
		if (mCurIndex == IMAGE_COUNT) {
			mCurIndex = 0;
		}
		c.drawBitmap(sImages.get(mCurIndex++), mX, mY, null);				
	}
	
	@Override
	public void update() {		
		if (mLeftDuration > 0) {
			mX = Math.max(mX - mSpeedX, 0);
			mLeftDuration -= GameEngine.ENGINE_SPEED;
		} else if (mRightDuration > 0) {
			mX = Math.min(mX + mSpeedX, mCanvasWidth - mWidth); 
			mRightDuration -= GameEngine.ENGINE_SPEED;
		}
		
		if (mUpDuration > 0) {
			mY = Math.max(mY - mSpeedY, mUpper);
			mUpDuration -= GameEngine.ENGINE_SPEED;
		} else {
			mY = Math.min(mY + mSpeedY, mBottom);
		}
		
		if (mVibrateDuration > 0) {					
			if (mVibrateDuration == MIN_VIBRATE_DURATION) { // first
				mVibrateCount = 0;
				mX += -3;				
			} else if (mVibrateDuration == GameEngine.ENGINE_SPEED) {
				mX += 3;
			} else {
				mX += (mVibrateCount & 1) == 0 ? -6 : 6;  
			}
			++mVibrateCount;
			mVibrateDuration -= GameEngine.ENGINE_SPEED;
		}
		
		mRect.left   = (int)(mX + mCollideArea[0]);
		mRect.top    = (int)(mY + mCollideArea[1]);
		mRect.right  = (int)(mX + mCollideArea[2]);
		mRect.bottom = (int)(mY + mCollideArea[3]);
	}

	@Override
	public void release() {
//		for (Bitmap image : sImages) {
//			image.recycle();				
//		}
		super.release();
	}

	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;
		mX = (width - mWidth) / 2;
		mY = (height - mHeight) / 2 + height / 4;
				
		mCollideArea[0] = mWidth * 0.25f;
		mCollideArea[1] = mHeight * 0.3f;
		mCollideArea[2] = mWidth * 0.75f;
		mCollideArea[3] = mHeight * 0.5f;
	
		mUpper  = (mCanvasHeight - mHeight) * 9 / 20;
		mBottom = (mCanvasHeight - mHeight) / 2 + mCanvasHeight / 4;
		setSpeed(DEFAULT_SPEED_X, (mBottom - mUpper) / (float)(3000 / GameEngine.ENGINE_SPEED));
		setMaxSpeed(DEFAULT_SPEED_X, (mBottom - mUpper) / (float)(3000 / GameEngine.ENGINE_SPEED));
	}

	@Override
	public void operate(GameCtrl ctrl) {
		int command = ctrl.getCommand();
		
		if (command == GameCtrl.MOVE_LEFT) {
			mLeftDuration  = GameEngine.ENGINE_SPEED;
			mRightDuration = 0;
		} else if (command == GameCtrl.MOVE_RIGHT) {
			mRightDuration = GameEngine.ENGINE_SPEED; 
			mLeftDuration  = 0;
		} else if (command == GameCtrl.MOVE_VERT) {
			mUpDuration = 1000;
		} 
	}

	@Override
	public void detectCollision(List<GameObject> objects) {						
		
		for (GameObject obj : objects) {
			// won't collide to itself
			if (obj == this) {
				continue;
			}
			if (!obj.getCollidable()) {
				continue;
			}

			boolean intersects = mRect.intersects(
				(int)obj.getX(), (int)obj.getY(), 
				(int)(obj.getX() + obj.getWidth()), (int)(obj.getY() + obj.getHeight()));
			if (intersects) {				
				if (obj.getKind() == PROTECTION) {
					((Reward) obj).bindRocket(this);
				} else {
					// rocket may vibrate for a little bit of time
					mVibrateDuration = MIN_VIBRATE_DURATION;
				}
				mCollideWith.add(obj);				
			}
		}				
		
		if (mCollideWith.size() > 0) {
			if (mOnCollideListener != null) {
				mOnCollideListener.onCollide(this, mCollideWith);				
			}
			for (GameObject obj : mCollideWith) {
				obj.setCollidable(false);
			}
			mCollideWith.clear();			
		}
	}
}
