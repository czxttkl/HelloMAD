package edu.neu.madcourse.binbo.rocketrush;

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
}
