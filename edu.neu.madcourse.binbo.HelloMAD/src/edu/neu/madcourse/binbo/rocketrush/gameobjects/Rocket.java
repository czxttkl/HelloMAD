package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Rocket extends GameObject implements GameObject.IDrawer  {
	protected List<Bitmap> mImages = new ArrayList<Bitmap>();
	protected int mImageWidth  = 0;
	protected int mImageHeight = 0;	
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	protected int mLeftDuration  = 0;
	protected int mRightDuration = 0;
	protected int mUpDuration    = 0;

	public Rocket(Resources res) {
		super(res);
		setMovable(true);
		setSpeed(7, 5);
		addImage(BitmapFactory.decodeResource(res, R.drawable.ship2_1));
		addImage(BitmapFactory.decodeResource(res, R.drawable.ship2_2));
		addImage(BitmapFactory.decodeResource(res, R.drawable.ship2_3));
		addImage(BitmapFactory.decodeResource(res, R.drawable.ship2_4));
		mImageWidth  = mImages.get(0).getWidth();
		mImageHeight = mImages.get(0).getHeight();
	}
	
	public Rocket(Resources res, List<Bitmap> images) {
		super(res);
		setMovable(true);
		for (Bitmap image : images) {
			addImage(image);
		}
	}

	public void addImage(Bitmap image) {
		mImages.add(image);
	}

	protected int mCurIndex = 0;
	@Override
	public void doDraw(Canvas c) {
		if (mCurIndex == 4) {
			mCurIndex = 0;
		}
		
		Bitmap image = mImages.get(mCurIndex++);
		c.drawBitmap(image, mX, mY, null);				
	}

	@Override
	public void update() {		
		if (mLeftDuration > 0) {
			mX = Math.max(mX - mSpeedX, 0);
			mLeftDuration -= GameEngine.ENGINE_SPEED;
		} else if (mRightDuration > 0) {
			mX = Math.min(mX + mSpeedX, mCanvasWidth - mImageWidth); 
			mRightDuration -= GameEngine.ENGINE_SPEED;
		}
		
		if (mUpDuration > 0) {
			mY = Math.max(mY - mSpeedY, mCanvasHeight / 4);
			mUpDuration -= GameEngine.ENGINE_SPEED;
		} else {
			mY = Math.min(mY + mSpeedY / 2, 
					(mCanvasHeight - mImageHeight) / 2 + mCanvasHeight / 4);
		}
	}

	@Override
	public void release() {
		for (Bitmap image : mImages) {
			image.recycle();				
		}
		super.release();
	}
	
	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;
		mX = (width - mImageWidth) / 2;
		mY = (height - mImageHeight) / 2 + height / 4;
	}

	@Override
	public void operate(GameCtrl ctrl) {
		int command = ctrl.getCommand();
		
		if (command == GameCtrl.MOVE_LEFT) {
			mLeftDuration = 20;
			mRightDuration = 0;
		} else if (command == GameCtrl.MOVE_RIGHT) {
			mRightDuration = 20; 
			mLeftDuration = 0;
		} else if (command == GameCtrl.MOVE_UP) {
			mUpDuration = 1000;
		} else if (command == GameCtrl.MOVE_DOWN) {
			
		}
	}
}
