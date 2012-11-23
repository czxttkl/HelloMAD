package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.neu.madcourse.binbo.rocketrush.gameobjects.Asteroid;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.BackgroundFar;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.BackgroundNear;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.Rocket;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.SpeedBar;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.os.Vibrator;


public class RushScene extends GameScene {	

	private Rocket mRocket = null;
	private SpeedBar mSpeedBar = null;
	private BackgroundFar  mBackgroundFar  = null;
	private BackgroundNear mBackgroundNear = null;
	private Random mRandom = new Random();
	private Context mContext = null;
	
	public RushScene(Context context) {
		super(context.getResources());
		mContext = context;
	}	

	public List<GameObject> load() {		
		if (mBackgroundFar == null) {
			mBackgroundFar  = new BackgroundFar(mRes);			
			mObjects.add(mBackgroundFar);
		}
		if (mBackgroundNear == null) {
			mBackgroundNear = new BackgroundNear(mRes);			
			mObjects.add(mBackgroundNear);
		}
		if (mSpeedBar == null) {
			mSpeedBar = new SpeedBar(mRes);
			mObjects.add(mSpeedBar);
		}
		if (mRocket == null) {
			mRocket = new Rocket(mRes);
			mRocket.setOnCollideListener(this);
			mObjects.add(mRocket);
		}
		
		return mObjects;
	}
	
	public void release() {
		super.release();
	}
	
	@Override
	public void doDraw(Canvas c) {
		for (GameObject obj : mObjects) {
			obj.doDraw(c);
		}
	}

	@Override
	protected void onSizeChanged(int width, int height) {		
		super.onSizeChanged(width, height);		
	}
	
	@Override
	public List<GameObject> updateBarriers() {
		// surface has not been created
		if (mWidth == 0 || mHeight == 0) {
			return null;
		}
		// remove invisible barriers
		List<GameObject> invisibles = new ArrayList<GameObject>();
		int edgeLeft  = -mWidth;
		int edgeRight = mWidth << 1;
		for (GameObject b : mBarriers) {
			float x = b.getX(), y = b.getY();
			if (x < edgeLeft || x > edgeRight || y > mHeight) {
				invisibles.add(b);
				b.release();
			}
		}
		mBarriers.removeAll(invisibles);
		mObjects.removeAll(invisibles);
		// get the acceleration time 
		int accTime = mRocket.getAccTime();
		// generate static barrier
		int pstatic = 1000 / GameEngine.ENGINE_SPEED * 3 / 2;
		if (mRandom.nextInt(pstatic) == 1) {
			Asteroid ast = new Asteroid(mRes);
			ast.setX(mRandom.nextInt((int)(mWidth - ast.getWidth() + 1)));
			ast.setY(0 - ast.getHeight() / 2);
			ast.initSpeeds(0, mRandom.nextInt(4) + 3, accTime);
			ast.onSizeChanged(mWidth, mHeight);
			ast.setOnCollideListener(this);
			mBarriers.add(ast);
			mObjects.add(ast);
		}		
		// generate dynamic barrier
		int pdynamic = (1000 / GameEngine.ENGINE_SPEED + 1) << 1;
		if (mRandom.nextInt(pdynamic) == 1) {
			Asteroid ast = new Asteroid(mRes);
			boolean left2Right = mRandom.nextBoolean();
			ast.setX(left2Right ? -ast.getWidth() : mWidth + ast.getWidth());
			ast.setY(mRandom.nextInt(mHeight >> 2));
			ast.initSpeeds(
				left2Right ? mRandom.nextInt(3) + 3 : -3 - mRandom.nextInt(3),   
				mRandom.nextInt(4) + 3,
				accTime
			);
			ast.onSizeChanged(mWidth, mHeight);
			ast.setOnCollideListener(this);
			mBarriers.add(ast);
			mObjects.add(ast);
		}
		// order by Z
		orderByZ(mObjects);
		
		return mBarriers;
	}

	@Override
	public void onCollide(GameObject obj, List<GameObject> collideWith) {
		Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(50);
	}
}
