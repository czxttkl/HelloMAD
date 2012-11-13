package edu.neu.madcourse.binbo.rocketrush;

import edu.neu.madcourse.binbo.rocketrush.gameobjects.*;

import android.content.res.Resources;
import android.graphics.Canvas;

public class WaitingScene extends GameScene {
	
	private BackgroundFar  mBackgroundFar  = null;
	private BackgroundNear mBackgroundNear = null;
	
	public WaitingScene(Resources res) {
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
