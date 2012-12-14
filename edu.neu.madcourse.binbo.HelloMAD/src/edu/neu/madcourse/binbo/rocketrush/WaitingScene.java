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
			mBackgroundFar  = new MenuBackgroundFar(mContext.getResources());
			mObjects.add(mBackgroundFar);
		}
		if (mBackgroundNear == null) {
			mBackgroundNear = new MenuBackgroundNear(mContext.getResources());			
			mObjects.add(mBackgroundNear);
		}
		if (mWidth > 0 || mHeight > 0) {
			for (GameObject obj : mObjects) {
				obj.onSizeChanged(mWidth, mHeight);
			}
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
}
