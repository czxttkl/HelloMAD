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
	private int mCurGameLoop = 1;
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
		int edgeLeft  = -(mWidth >> 2);
		int edgeRight = mWidth + (mWidth >> 2);
		for (GameObject b : mBarriers) {
			float x = b.getX(), y = b.getY();
			if (x < edgeLeft || x > edgeRight || y > mHeight) {
				invisibles.add(b);
				b.release();
			}
		}
		mBarriers.removeAll(invisibles);
		mObjects.removeAll(invisibles);
		// create barriers based on the current game progress
		if (mCurGamePart <= 2) {
			createBird();								
		} else if (mCurGamePart <= 4) {
			createAsteroid();
		} else if (mCurGamePart <= 6){			
			createAlient();
		}					
		if (mCurGameLoop > 1) {
			createThunder();
		}
		
		return mBarriers;
	}
	
	// probabilities for creating barriers
	private int mProbBird    = 100; // 1 / 100
	private int	mProbAster   = 190; // 1 / 90
	private int mProbAlient  = 90;  // 1 / 80
	private int mProbThunder = 270;
	
	private void createBird() {		
		// get the acceleration time 
		int accTime = mRocket.getAccTime();
		// generate flying red chicken
		if (mRandom.nextInt(mProbBird) == 1) {
			boolean right = mRandom.nextBoolean();
			Bird b = new Bird(mRes, right);			
			b.setX(right ? -b.getWidth() : mWidth);
			b.setY(mRandom.nextInt(mHeight - (mHeight >> 1)));
			b.initSpeeds(
				(right ? mRandom.nextInt(4) + 5 : -5 - mRandom.nextInt(4)) * mLevel.mSpeedScaleX,   
				3f,
				accTime
			);
			b.onSizeChanged(mWidth, mHeight);
			b.setOnCollideListener(this);
			mBarriers.add(b);
			mObjects.add(b);
		}	
		// order by Z
		orderByZ(mObjects);
	}
	
	private void createAsteroid() {
		// get the acceleration time 
		int accTime = mRocket.getAccTime();		
		// generate asteroid
		int type = mRandom.nextInt(mProbAster);
		if (type == 1) {
			Asteroid ast = new Asteroid(mRes);
			ast.setX(mRandom.nextInt((int)(mWidth - ast.getWidth() + 1)));
			ast.setY(0 - ast.getHeight());
			ast.initSpeeds(0, (mRandom.nextInt(4) + 3) * mLevel.mSpeedScaleY, accTime);
			ast.onSizeChanged(mWidth, mHeight);
			ast.setOnCollideListener(this);
			mBarriers.add(ast);
			mObjects.add(ast);
		} else if (type == 2) {
			Asteroid ast = new Asteroid(mRes);
			boolean right = mRandom.nextBoolean();
			ast.setX(right ? -ast.getWidth() : mWidth);
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
		// order by Z
		orderByZ(mObjects);
	}
	
	private void createAlient() {
		// get the acceleration time 
		int accTime = mRocket.getAccTime();
		// generate alient
		int type = mRandom.nextInt(mProbAster);
//		if (type == 1) {
//			Alient ali = new Alient(mRes);
//			ali.setX(mRandom.nextInt((int)(mWidth - ali.getWidth() + 1)));
//			ali.setY(0 - ali.getHeight());
//			ali.initSpeeds(0, (mRandom.nextInt(4) + 3) * mLevel.mSpeedScaleY, accTime);
//			ali.onSizeChanged(mWidth, mHeight);
//			ali.setOnCollideListener(this);
//			mBarriers.add(ali);
//			mObjects.add(ali);
//		} else if (type == 2) {
//			Alient ali = new Alient(mRes);
//			boolean right = mRandom.nextBoolean();
//			ali.setX(right ? -ali.getWidth() : mWidth + ali.getWidth());
//			ali.setY(mRandom.nextInt(mHeight >> 3));
//			ali.initSpeeds(
//				(right ? mRandom.nextInt(3) + 3 : -3 - mRandom.nextInt(3)) * mLevel.mSpeedScaleX,   
//				(mRandom.nextInt(4) + 3) * mLevel.mSpeedScaleY,
//				accTime
//			);
//			ali.onSizeChanged(mWidth, mHeight);
//			ali.setOnCollideListener(this);
//			mBarriers.add(ali);
//			mObjects.add(ali); 
//		}
		if (type == 3) {
			int aliType = mRandom.nextInt(2);
			Alient ali = new TrickyAlient(mRes, aliType);
			if (aliType == 0) {				
				boolean right = mRandom.nextBoolean();
				ali.setX(right ? -ali.getWidth() : mWidth);
				ali.setY(mRandom.nextInt(mHeight >> 5));
				ali.initSpeeds(
					(right ? mRandom.nextInt(6) + 7 : -7 - mRandom.nextInt(6)),   
					mRandom.nextInt(4) + 2,
					accTime
				);
			} else if (aliType == 1) {
				float offset = ali.getWidth();				
				ali.setX(offset + mRandom.nextInt((int) (mWidth - offset - offset - offset)));
				ali.setY(-ali.getHeight());
				ali.initSpeeds(
					6,   
					(mRandom.nextInt(4) + 2) * mLevel.mSpeedScaleY,
					accTime
				);
			}
			ali.onSizeChanged(mWidth, mHeight);
			ali.setOnCollideListener(this);
			mBarriers.add(ali);
			mObjects.add(ali);
		}
		// order by Z
		orderByZ(mObjects);
	}

	private void createThunder() {
		// generate flying red chicken
		if (mRandom.nextInt(mProbThunder) == 1) {
			Thunder t = new Thunder(mRes);			
			t.setX(mRandom.nextInt((int) (mWidth - t.getWidth())));
			t.setY(-t.getHeight());
			t.initSpeeds(0, 3f, 0);
			t.onSizeChanged(mWidth, mHeight);
			t.setOnCollideListener(this);
			mBarriers.add(t);
			mObjects.add(t);
		}	
		// order by Z
		orderByZ(mObjects);
	}
	
	@Override
	public void onCollide(GameObject obj, List<GameObject> collideWith) {
		Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
		vibrator.vibrate(30);
		mLifeBar.lifeChange(-0.334f);
	}

	public void onReachTarget(int odometer) {
		mLifeBar.lifeChange(0.01f);
	}

	public void onReachMilestone(int odometer) {
		// level up and update barrier probabilities
		mLevel.levelUp();
		mCurGamePart = mLevel.getValue() % 7;
		if (mCurGamePart == 0) { 
			// the difficulty increases about 30% after each loop
			// algorithm: 
			// for speed: ...
			// for complexity: 1 / Math.pow(1.1, 6) * 1.363 / 1.1 Å 1 / 1.3
			mLevel.mSpeedScaleX *= 0.9;
			mLevel.mSpeedScaleY *= 0.9;
			mProbBird   *= 1.363;
			mProbAster  *= 1.363;
			mProbAlient *= 1.363;
			mCurGamePart = 1;
			++mCurGameLoop;
		}
		mProbBird   /= mLevel.mComplexityScale;
		mProbAster  /= mLevel.mComplexityScale;
		mProbAlient /= mLevel.mComplexityScale;
		if (mCurGameLoop >= 1) {
			mProbThunder /= mLevel.mComplexityScale;
		}
	}
}
