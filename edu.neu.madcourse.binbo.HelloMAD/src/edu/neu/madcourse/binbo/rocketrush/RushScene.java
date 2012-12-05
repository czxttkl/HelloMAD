package edu.neu.madcourse.binbo.rocketrush;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.neu.madcourse.binbo.rocketrush.gameobjects.*;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.LifeBar.OnLifeChangedListener;
import edu.neu.madcourse.binbo.rocketrush.gameobjects.Odometer.OnOdometerUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Vibrator;


public class RushScene extends GameScene implements OnOdometerUpdateListener, 
										 			OnLifeChangedListener {	

	private Rocket   mRocket   = null;
	private LifeBar  mLifeBar  = null;
	private SpeedBar mSpeedBar = null;
	private BackgroundFar  mBackgroundFar  = null;
	private BackgroundNear mBackgroundNear = null;
	private Level mLevel = null;	
	private Odometer mOdometer = null;
	private int mCurLevel = 1;
	private int mCurLoop  = 1;
	private Random mRandom = new Random();
	private Context mContext = null;
	
	public RushScene(Context context) {
		super(context.getResources());
		mContext = context;
	}	
	
	@Override
	public void reset() {
		mCurLevel = 1;
		mCurLoop  = 1; 
		release();
		load();
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
			mLifeBar.setOnLifeChangedListener(this);
			mObjects.add(mLifeBar);
		}
		if (mWidth > 0 || mHeight > 0) {
			for (GameObject obj : mObjects) {
				obj.onSizeChanged(mWidth, mHeight);
			}
		}
		// order by Z
		orderByZ(mObjects);
		
		return mObjects;
	}
	
	public void release() {
		super.release();
		mBackgroundFar  = null;
		mBackgroundNear = null;
		mSpeedBar = null;
		mRocket   = null;
		mLevel    = null;
		mOdometer = null;
		mLifeBar  = null;
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
	public void updateBarriers() {
		// surface has not been created
		if (mWidth == 0 || mHeight == 0) { return; }
		// remove invisible barriers
		List<GameObject> invisibles = null;
		for (GameObject b : mBarriers) {
			float x = b.getX(), y = b.getY();
			if (x < -(mWidth >> 2) || x > (mWidth + (mWidth >> 2)) || y > mHeight) {
				if (invisibles == null) {
					invisibles = new ArrayList<GameObject>();
				}
				invisibles.add(b);
				b.release();
			}
		}
		if (invisibles != null) {
			mBarriers.removeAll(invisibles);
			mObjects.removeAll(invisibles);
		}
		// create barriers based on the current game progress
		if (mCurLevel == 1) {
			createBird(mProbBird);
		} else if (mCurLevel == 2) {
			createBird(mProbBird);
			createThunder(mProbThunder);
		} else if (mCurLevel == 3) {
			createBird(mProbBird << 2);
			createThunder(mProbThunder << 1);
			createAsteroid(mProbAster);
		} else if (mCurLevel == 4) {
			createThunder(mProbThunder << 2);
			createAsteroid(mProbAster << 1);
			createAlient(mProbAlient << 1);
		} else if (mCurLevel == 5) {	
			createThunder(mProbThunder << 1);
			createAsteroid(mProbAster);
			createAlient(mProbAlient << 1);
		} else if (mCurLevel == 6) {
			createThunder(mProbThunder);
			createAsteroid(mProbAster);
			createAlient(mProbAlient);
		}		
	}
	
	// probabilities for creating barriers
	private int mProbBird    = 80;
	private int	mProbAster   = 165;
	private int mProbAlient  = 90;
	private int mProbThunder = 180;
	
	private void createBird(int probability) {		
		// get the acceleration time 
		int accTime = mRocket.getAccTime();
		// generate flying red chicken
		if (mRandom.nextInt(probability) == 1) {
			boolean right = mRandom.nextBoolean();
			Bird b = new Bird(mRes, right);			
			b.setX(right ? -b.getWidth() : mWidth);
			b.setY(mRandom.nextInt(mHeight - (mHeight >> 1) - (accTime > 0 ? (mHeight >> 2) : 0)));
			b.initSpeeds(
				(right ? mRandom.nextInt(4) + 4 : -4 - mRandom.nextInt(4)) * mLevel.mSpeedScaleX,   
				3f,
				accTime
			);
			b.onSizeChanged(mWidth, mHeight);
			b.setOnCollideListener(this);
			mBarriers.add(b);
			mObjects.add(b);
			// order by Z
			orderByZ(mObjects);
		}			
	}
	
	private void createAsteroid(int probability) {
		// get the acceleration time 
		int accTime = mRocket.getAccTime();		
		// generate asteroid
		int type = mRandom.nextInt(probability);
		if (type == 1) {
			Asteroid ast = new Asteroid(mRes);
			ast.setX(mRandom.nextInt((int)(mWidth - ast.getWidth() + 1)));
			ast.setY(0 - ast.getHeight());
			ast.initSpeeds(0, (mRandom.nextInt(4) + 3) * mLevel.mSpeedScaleY, accTime);
			ast.onSizeChanged(mWidth, mHeight);
			ast.setOnCollideListener(this);
			mBarriers.add(ast);
			mObjects.add(ast);
			// order by Z
			orderByZ(mObjects);
		} else if (type == 2) {
			Asteroid ast = new Asteroid(mRes);
			boolean right = mRandom.nextBoolean();
			ast.setX(right ? -ast.getWidth() : mWidth);
			ast.setY(mRandom.nextInt(mHeight >> 3) - (accTime > 0 ? (mHeight >> 3) : 0));
			ast.initSpeeds(
				(right ? mRandom.nextInt(3) + 3 : -3 - mRandom.nextInt(3)) * mLevel.mSpeedScaleX,   
				(mRandom.nextInt(4) + 3) * mLevel.mSpeedScaleY,
				accTime
			);
			ast.onSizeChanged(mWidth, mHeight);
			ast.setOnCollideListener(this);
			mBarriers.add(ast);
			mObjects.add(ast);
			// order by Z
			orderByZ(mObjects);
		}		
	}
	
	private void createAlient(int probability) {
		// get the acceleration time 
		int accTime = mRocket.getAccTime();
		// generate alient
		int type = mRandom.nextInt(probability);
//		if (type == 1) {
//			Alient ali = new Alient(mRes);
//			ali.setX(mRandom.nextInt((int)(mWidth - ali.getWidth() + 1)));
//			ali.setY(0 - ali.getHeight());
//			ali.initSpeeds(0, (mRandom.nextInt(4) + 3) * mLevel.mSpeedScaleY, accTime);
//			ali.onSizeChanged(mWidth, mHeight);
//			ali.setOnCollideListener(this);
//			mBarriers.add(ali);
//			mObjects.add(ali);
//			// order by Z
//			orderByZ(mObjects);
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
//			// order by Z
//			orderByZ(mObjects);
//		}
		if (type == 3) {
			int aliType = mRandom.nextInt(2);
			Alient ali = new TrickyAlient(mRes, aliType);
			if (aliType == 0) {				
				boolean right = mRandom.nextBoolean();
				ali.setX(right ? -ali.getWidth() : mWidth);
				ali.setY(mRandom.nextInt(mHeight >> 5) - (accTime > 0 ? (mHeight >> 3) : 0));
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
					(mRandom.nextInt(5) + 2),
					accTime
				);
			}
			ali.onSizeChanged(mWidth, mHeight);
			ali.setOnCollideListener(this);
			mBarriers.add(ali);
			mObjects.add(ali);
			// order by Z
			orderByZ(mObjects);
		}		
	}

	private void createThunder(int probability) {
		// generate flying red chicken
		if (mRandom.nextInt(probability) == 1) {
			Thunder t = new Thunder(mRes);			
			t.setX(mRandom.nextInt((int) (mWidth - t.getWidth())));
			t.setY(-t.getHeight());
			t.initSpeeds(0, 3f, 0);
			t.onSizeChanged(mWidth, mHeight);
			t.setOnCollideListener(this);
			mBarriers.add(t);
			mObjects.add(t);
			// order by Z
			orderByZ(mObjects);
		}			
	}
	
	// probabilities for creating reward
	protected int mProbReward = 1200;
	
	public void updateReward() {
		if (mRandom.nextInt(mProbReward) == 0) {
			Field f = new Field(mRes);			
			f.setX(mRandom.nextInt((int) (mWidth - f.getWidth())));
			f.setY(-f.getHeight());			
			f.onSizeChanged(mWidth, mHeight);
			f.setOnCollideListener(this);
			mObjects.add(f);
			// order by Z
			orderByZ(mObjects);
		}		
	}
	
	@Override
	public void onCollide(GameObject obj, List<GameObject> collideWith) {
		int kind = obj.getKind();
		
		if (kind == GameObject.ROCKET) {
			Vibrator vibrator = (Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(30);
			mLifeBar.lifeChange(-0.334f);	
		}
	}

	public void onReachTarget(int odometer) {
		mLifeBar.lifeChange(0.01f);
	}

	public void onReachMilestone(int odometer) {
		// level up and update barrier probabilities
		mLevel.levelUp();		
		mCurLevel = mLevel.getValue() % 7;
		if (mCurLevel == 0) { 
			// the difficulty increases about 30% after each loop
			// algorithm: 
			// for speed: ...
			// for complexity: 1 / Math.pow(1.1, 6) * 1.363 / 1.1 Å 1 / 1.3
			mLevel.mSpeedScaleX *= 0.9;
			mLevel.mSpeedScaleY *= 0.9;
			mProbBird    *= 1.363;
			mProbAster   *= 1.363;
			mProbAlient  *= 1.363;
			mProbThunder *= 1.2;
			++mCurLoop;
		}
		mCurLevel = mCurLoop > 1 ? mCurLevel + 1 : mCurLevel;
		
		mProbBird    /= mLevel.mComplexityScale;
		mProbAster   /= mLevel.mComplexityScale;
		mProbAlient  /= mLevel.mComplexityScale;
		mProbThunder /= (mLevel.mComplexityScale - 0.05);
		
		// update the background according to the current level
		if (mCurLevel == 1) {
			;
		} else if (mCurLevel == 3 || mCurLevel == 5) {
			mBackgroundFar.switchToNext();
			mBackgroundNear.switchToNext();
		}
	}

	public void onLifeChanged(float life) {
		if (life == 0) { // compare a float, not good, modify later if neccessary
			GameEvent e = new StateEvent(StateEvent.STATE_OVER, "life is 0, game over");
			mEventHandler.handleGameEvent(e);
		}
	}
}
