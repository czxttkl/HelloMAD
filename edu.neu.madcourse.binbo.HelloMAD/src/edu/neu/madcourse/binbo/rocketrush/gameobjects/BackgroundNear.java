package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;

public class BackgroundNear extends Background {

	private int mAccMoveDuration = 0;
	public final static float DEFAULT_SPEED_X = 0;
	public final static float DEFAULT_SPEED_Y = 3f;
	
	public BackgroundNear(Resources res) {
		super(res);
		setSpeed(DEFAULT_SPEED_X, DEFAULT_SPEED_Y);
		setMaxSpeed(0, DEFAULT_SPEED_Y * 3);
		setAccSpeed(0, DEFAULT_SPEED_Y / (1000 / GameEngine.ENGINE_SPEED));
		setZOrder(ZOrders.BACKGROUND_NEAR);
		setImage(BitmapFactory.decodeResource(res, R.drawable.bg1_near));
	}
	
	public BackgroundNear(Resources res, Bitmap image) {
		super(res);
		setSpeed(DEFAULT_SPEED_X, DEFAULT_SPEED_Y);
		setMaxSpeed(0, DEFAULT_SPEED_Y * 3);
		setAccSpeed(0, DEFAULT_SPEED_Y / (1000 / GameEngine.ENGINE_SPEED));
		setZOrder(ZOrders.BACKGROUND_NEAR);
		setImage(image);
	}

	@Override
	public void onSizeChanged(int width, int height) {
		if (mImage == null) return;
		// scale the background according to the surface size
		float radio = mHeight / (float) mWidth;	
		int scaledWidth  = width;
		int scaledHeight = (int)(width * radio);
		scaledHeight += mSpeedY - scaledHeight % mSpeedY;
		
		Bitmap newImage = 
			Bitmap.createScaledBitmap(mImage, scaledWidth, scaledHeight, true);	
		mImage.recycle(); // explicit call to avoid out of memory
		setImage(newImage);
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

}
