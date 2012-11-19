package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.Asteroid;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.BackgroundNear;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.MenuBackgroundFar;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.MenuBackgroundNear;

public class WaitingScene extends GameScene {

	private MenuBackgroundFar  mBackgroundFar  = null;
	private MenuBackgroundNear mBackgroundNear = null;
	// generate game elements according to the scene configuration
    private Random mRandom = new Random();
    private Context mContext = null;
	
	public WaitingScene(Context context) {
		super(context.getResources());
		mContext = context;
	}	

	public List<GameObject> load() {
		if (mBackgroundFar == null) {
			mBackgroundFar  = new MenuBackgroundFar(mRes);
			mObjects.add(mBackgroundFar);
		}
		if (mBackgroundNear == null) {
			mBackgroundNear = new MenuBackgroundNear(mRes);			
			mObjects.add(mBackgroundNear);
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
		/*// surface has not been created
		if (mWidth == 0 || mHeight == 0) {
			return null;
		}
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
		// order by Z
		orderByZ(mObjects);*/
		
		return new ArrayList<GameObject>();
	}
}
