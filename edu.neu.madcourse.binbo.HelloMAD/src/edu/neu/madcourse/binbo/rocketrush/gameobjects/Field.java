package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameObject;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Field extends Reward {
	protected final static int IMAGE_COUNT = 2; // the same size of the total number of bitmaps
	protected final static int IMAGE_UNBOUND_START = 0; 
	protected final static int IMAGE_BOUND_START   = 2;
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
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.single_protector_1));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.single_protector_2));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.protection_bubble));
	}
	
	public Field(Resources res) {
		super(res);
		loadImages(res);
		setKind(PROTECTION);
		setZOrder(ZOrders.PROTECTION);
		setWidth(sImages.get(IMAGE_UNBOUND_START).getWidth());
		setHeight(sImages.get(IMAGE_UNBOUND_START).getHeight());	
	}

	protected int mFlashDuration = 250; // 1000 / GameEngine.ENGINE_SPEED * 5	
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
		
		if (System.currentTimeMillis() - mBegTime > mBoundTimeout - 5000) {
			if (mFlashDuration-- % 5 == 0) {
				mVisible = !mVisible;
			}
		}
	}

	private int mUpdateUnbound = 0;
	@Override
	protected void updateUnbound() {
		if (++mUpdateUnbound > 32) {
			mUpdateUnbound = 0;
		}
	
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
		if (mVisible) {
			c.drawBitmap(sImages.get(IMAGE_BOUND_START), mX, mY - 11f, null);
		}
	}

	protected int mUnboundIndex = 0;
	@Override
	protected void drawUnbound(Canvas c) {
		if (mX + mWidth  <= 0 || mX >= mCanvasWidth ||
			mY + mHeight <= 0 || mY >= mCanvasHeight) {
			return; // not necessary to draw the invisible
		}
		c.drawBitmap(sImages.get(mUpdateUnbound <= 16 ? 0 : 1), mX, mY, null);	
	}

	@Override
	protected void onBound() {
		setWidth(sImages.get(IMAGE_BOUND_START).getWidth());
		setHeight(sImages.get(IMAGE_BOUND_START).getHeight());	
		mOffsetX = (mWidth - mRocket.getWidth()) * 0.5f;
		mOffsetY = (mHeight - mRocket.getHeight()) * 0.5f;
	}
	
//	@Override
//	public void detectCollision(List<GameObject> objects) {						
//		
//		for (GameObject obj : objects) {
//			// won't collide to itself
//			if (obj == this) {
//				continue;
//			}
//			if (!obj.getCollidable()) {
//				continue;
//			}
//
//			boolean intersects = mRect.intersects(
//				(int)obj.getX(), (int)obj.getY(), 
//				(int)(obj.getX() + obj.getWidth()), (int)(obj.getY() + obj.getHeight()));
//			if (intersects) {				
//				if (obj.getKind() == PROTECTION) {
//					((Reward) obj).bindRocket(this);
//				} else {
//					for (Reward reward : mRewards) {
//						if (reward.getKind() == PROTECTION) {
//							return;
//						}
//					}
//					mCollideWith.add(obj);
//				}
//			}
//		}				
//		
//		if (mCollideWith.size() > 0) {
//			if (mOnCollideListener != null) {
//				mOnCollideListener.onCollide(this, mCollideWith);
//				for (GameObject obj : mCollideWith) {
//					obj.setCollidable(false);
//				}
//				mCollideWith.clear();
//			}
//			// rocket may vibrate for a little bit of time
//			mVibrateDuration = MIN_VIBRATE_DURATION;
//		}
//	}
}
