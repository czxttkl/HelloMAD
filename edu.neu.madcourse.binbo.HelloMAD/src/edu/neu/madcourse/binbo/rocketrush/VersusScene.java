package edu.neu.madcourse.binbo.rocketrush;

import java.util.Vector;

import edu.neu.madcourse.binbo.rocketrush.gameobjects.Asteroid;
import android.content.res.Resources;
import android.graphics.Canvas;

public class VersusScene extends GameScene {

	private Resources mResources;
	private Vector<Asteroid> mAsteroids = null;
	
	public VersusScene(Resources resources) {
		mResources = resources;
	}
	
	@Override
	public int doDraw(Canvas c) {
		// TODO Auto-generated method stub
		return super.doDraw(c);
	}

	@Override
	protected void updateSceneSize(int width, int height) {
		// TODO Auto-generated method stub
		super.updateSceneSize(width, height);
	}

}
