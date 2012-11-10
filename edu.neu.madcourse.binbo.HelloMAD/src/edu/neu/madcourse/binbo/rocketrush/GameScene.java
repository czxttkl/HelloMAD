package edu.neu.madcourse.binbo.rocketrush;

import android.graphics.Canvas;

public class GameScene implements IDrawer {	
	protected int mWidth  = 0;
	protected int mHeight = 0;
	
	protected void updateSceneSize(int width, int height) {
		mWidth  = width;
		mHeight = height;
	}
	
	public int doDraw(Canvas c) {
		// TODO Auto-generated method stub
		return 0;
	}

}
