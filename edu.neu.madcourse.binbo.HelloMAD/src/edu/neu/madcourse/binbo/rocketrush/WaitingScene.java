package edu.neu.madcourse.binbo.rocketrush;

import java.util.*;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.*;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class WaitingScene extends GameScene {
	
	private Resources mResources;
	private Bitmap mBkFarImage  = null;	
	private Bitmap mBkNearImage = null;
	private Background mBackgroundFar;
	private Background mBackgroundNear;
	private List<Bitmap> mAsterImages = new ArrayList<Bitmap>();	
	
	public WaitingScene(Resources resources) {
		mResources = resources;
		loadResources(mResources);
		createGameObjects();
	}	

	public void loadResources(Resources resources) {
		// load images of all backgrounds
		mBkFarImage  = BitmapFactory.decodeResource(resources, R.drawable.background_a);
		mBkNearImage = BitmapFactory.decodeResource(resources, R.drawable.background_b);
		// load images of all asteroids 
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid01));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid02));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid03));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid04));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid05));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid06));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid07));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid08));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid09));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid10));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid11));
		mAsterImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid12));				
	}
	
	public void releaseResources() {
		mBkFarImage.recycle();
		mBkNearImage.recycle();
		for (int i = 0; i < mAsterImages.size(); ++i) {
			mAsterImages.get(i).recycle();
		}
	}

	private void createGameObjects() {
		mBackgroundFar  = new Background(mBkFarImage);
		mBackgroundNear = new Background(mBkNearImage);
		mBackgroundFar.setSpeed(0, 1);
		mBackgroundNear.setSpeed(0, 2);
		mObjects.add(mBackgroundFar);
		mObjects.add(mBackgroundNear);
	}	
	
	@Override
	public int doDraw(Canvas c) {
		for (int i = 0; i < mObjects.size(); ++i) {
			mObjects.get(i).doDraw(c);
		}
		
		return super.doDraw(c);
	}

	@Override
	protected void updateSceneSize(int width, int height) {
		// TODO Auto-generated method stub
		super.updateSceneSize(width, height);
		// scale the background according to the surface size
		float radio = mBkFarImage.getHeight() / (float)mBkFarImage.getWidth();
		mBkFarImage  = Bitmap.createScaledBitmap(mBkFarImage,  mWidth, (int)(mWidth * radio), true);
		radio = mBkNearImage.getHeight() / (float)mBkNearImage.getWidth();
		mBkNearImage = Bitmap.createScaledBitmap(mBkNearImage, mWidth, (int)(mWidth * radio), true);
		// need to update to the new scaled image because it's a different reference
		mBackgroundFar.setImage(mBkFarImage);
		mBackgroundNear.setImage(mBkNearImage);
	}

}
