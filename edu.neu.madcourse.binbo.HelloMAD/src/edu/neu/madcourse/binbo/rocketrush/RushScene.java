package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.neu.madcourse.binbo.rocketrush.gameobjects.*;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.Odometer.OnOdometerUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;


public class RushScene extends GameScene implements OnOdometerUpdateListener {	

	private Rocket   mRocket   = null;
	private LifeBar  mLifeBar  = null;
	private SpeedBar mSpeedBar = null;
	private BackgroundFar  mBackgroundFar  = null;
	private BackgroundNear mBackgroundNear = null;
	private Level mLevel = null;	
	private Odometer mOdometer = null;
	private int mCurGamePart = 1;
	private Random mRandom = new Random();
	private Context mContext = null;
	
	public RushScene(Context context) {
		super(context.getResources());
		mContext = context;
	}	

	public List<GameObject> load() {
		// create game objects
		if (mBackgroundFar == null) {
			mBackgroundFar = new BackgroundFar(mRes);			
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
		if (mLevel == null) {
			mLevel = new Level(mRes);
			mObjects.add(mLevel);
		}
		if (mOdometer == null) {
			mOdometer = new Odometer(mRes);
			mOdometer.setOdometerUpdateListener(this);
			mObjects.add(mOdometer);
		}
		if (mLifeBar == null) {
			mLifeBar = new LifeBar(mRes);	
			mObjects.add(mLifeBar);
		}
		// order by Z
		orderByZ(mObjects);
		
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
		int edgeLeft  = -(mWidth >> 1);
		int edgeRight = mWidth + (mWidth >> 1);
		for (GameObject b : mBarriers) {
			float x = b.getX(), y = b.getY();
			if (x < edgeLeft || x > edgeRight || y > mHeight) {
				invisibles.add(b);
				b.release();
			}
		}
		mBarriers.removeAll(invisibles);
		mObjects.removeAll(invisibles);
		// create barriers based on the current game part	
		if (mCurGamePart <= 2) {
			createBird();			
		} else if (mCurGamePart <= 4) {
			createAsteroid();
		} else if (mCurGamePart <= 6){
			createAlient();
		}		
		// order by Z
		orderByZ(mObjects);
		
		return mBarriers;
	}
	
	// probabilities for creating barriers
	private int mProbBird   = 45; // 1 / 45
	private int	mProbAster  = 65; // 1 / 55
	private int mProbAlient = 40; // 1 / 40
	
	private void createBird() {		
		// get the acceleration time 
		int accTime = mRocket.getAccTime();
		// generate flying red chicken
		if (mRandom.nextInt(mProbBird) == 1) {
			boolean right = mRandom.nextBoolean();
			Bird b = new Bird(mRes, right);			
			b.setX(right ? -b.getWidth() : mWidth + b.getWidth());
			b.setY(mRandom.nextInt(mHeight >> 2));
			b.initSpeeds(
				(right ? mRandom.nextInt(5) + 4 : -4 - mRandom.nextInt(5)) * mLevel.mSpeedScaleX,   
				4f,
				accTime
			);
			b.onSizeChanged(mWidth, mHeight);
			b.setOnCollideListener(this);
			mBarriers.add(b);
			mObjects.add(b);
		}		
	}
	
	private void createAsteroid() {
		// get the acceleration time 
		int accTime = mRocket.getAccTime();
		// generate static asteroid
		if (mRandom.nextInt(mProbAster) == 1) {
			Asteroid ast = new Asteroid(mRes);
			ast.setX(mRandom.nextInt((int)(mWidth - ast.getWidth() + 1)));
			ast.setY(0 - ast.getHeight());
			ast.initSpeeds(0, (mRandom.nextInt(4) + 3) * mLevel.mSpeedScaleY, accTime);
			ast.onSizeChanged(mWidth, mHeight);
			ast.setOnCollideListener(this);
			mBarriers.add(ast);
			mObjects.add(ast);
		}		
		// generate dynamic asteroid
		if (mRandom.nextInt(mProbAster) == 1) {
			Asteroid ast = new Asteroid(mRes);
			boolean right = mRandom.nextBoolean();
			ast.setX(right ? -ast.getWidth() : mWidth + ast.getWidth());
			ast.setY(mRandom.nextInt(mHeight >> 3));
			ast.initSpeeds(
				(right ? mRandom.nextInt(3) + 3 : -3 - mRandom.nextInt(3)) * mLevel.mSpeedScaleX,   
				(mRandom.nextInt(4) + 3) * mLevel.mSpeedScaleY,
				accTime
			);
			ast.onSizeChanged(mWidth, mHeight);
			ast.setOnCollideListener(this);
			mBarriers.add(ast);
			mObjects.add(ast);
		}
	}
	
	private void createAlient() {
		
	}

	@Override
	public void onCollide(GameObject obj, List<GameObject> collideWith) {
		Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(30);
		mLifeBar.lifeDown(1 / 3f);
	}

	public void onReachTarget(int odometer) {
		mLifeBar.lifeUp(0.01f);
	}

	public void onReachMilestone(int odometer) {
		// level up and update barrier probabilities
		mLevel.levelUp();
		mCurGamePart = mLevel.getValue() % 7;
		mCurGamePart = mCurGamePart == 0 ? 1 : mCurGamePart;
		mProbBird   /= mLevel.mComplexityScale;
		mProbAster  /= mLevel.mComplexityScale;
		mProbAlient /= mLevel.mComplexityScale;
	}
}
