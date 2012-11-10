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
	private List<Bitmap> mAsteroidImages = new ArrayList<Bitmap>();
	private Vector<Asteroid> mAsteroids = null;
	
	public WaitingScene(Resources resources) {
		mResources = resources;
		loadResources(mResources);
		createGameObjects();
	}	

	public void loadResources(Resources resources) {
		// load iamges of all backgrounds
		mBkFarImage  = BitmapFactory.decodeResource(resources, R.drawable.background_a);
		mBkNearImage = BitmapFactory.decodeResource(resources, R.drawable.background_b);
		// load images of all asteroids 
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid01));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid02));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid03));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid04));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid05));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid06));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid07));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid08));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid09));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid10));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid11));
		mAsteroidImages.add(BitmapFactory.decodeResource(resources, R.drawable.asteroid12));				
	}

	private void createGameObjects() {
		mBackgroundFar  = new Background(mBkFarImage);
		mBackgroundNear = new Background(mBkNearImage);
	}	
	
	@Override
	public int doDraw(Canvas c) {
		c.drawBitmap(mBkFarImage, 0, 0, null);
		c.drawBitmap(mBkNearImage, 0, 0, null);
		
		return super.doDraw(c);
	}

	@Override
	protected void updateSceneSize(int width, int height) {
		// TODO Auto-generated method stub
		super.updateSceneSize(width, height);
		
		mBkFarImage  = Bitmap.createScaledBitmap(mBkFarImage,  mWidth, mHeight << 1, true);
		mBkNearImage = Bitmap.createScaledBitmap(mBkNearImage, mWidth, mHeight << 1, true);
	}

}
