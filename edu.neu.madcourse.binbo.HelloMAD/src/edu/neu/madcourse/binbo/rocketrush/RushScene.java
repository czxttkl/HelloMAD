package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.neu.madcourse.binbo.rocketrush.gameobjects.Asteroid;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.BackgroundFar;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.BackgroundNear;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.Rocket;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.SpeedBar;
import android.content.res.Resources;
import android.graphics.Canvas;


public class RushScene extends GameScene {	

	private Rocket mRocket = null;
	private SpeedBar mSpeedBar = null;
	private BackgroundFar  mBackgroundFar  = null;
	private BackgroundNear mBackgroundNear = null;
	private Random mRandom = new Random();
	
	public RushScene(Resources res) {
		super(res);
	}	

	public void load() {		
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
			mObjects.add(mRocket);
		}
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
		// remove invisible barriers
		List<GameObject> invisibles = new ArrayList<GameObject>();
		int edgeLeft  = -mWidth;
		int edgeRight = mWidth << 1;
		for (GameObject b : mBarriers) {
			int x = b.getX(), y = b.getY();
			if (x < edgeLeft || x > edgeRight || y > mHeight) {
				invisibles.add(b);
				b.release();
			}
		}
		mBarriers.removeAll(invisibles);
		mObjects.removeAll(invisibles);
		// generate static barrier
		int pstatic = 1000 / GameEngine.ENGINE_SPEED + 1;
		if (mRandom.nextInt(pstatic) == 1) {
			Asteroid ast = new Asteroid(mRes);
			ast.setX(mRandom.nextInt(mWidth - ast.getWidth() + 1));
			ast.setY(0 - ast.getHeight() << 1);
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
			ast.setSpeed(
				left2Right ? mRandom.nextInt(3) + 3 : -3 - mRandom.nextInt(3),   
				mRandom.nextInt(5) + 3
			);
			mBarriers.add(ast);
			mObjects.add(ast);
		}
		
		return mBarriers;
	}
}
