package edu.neu.madcourse.binbo.rocketrush;

import java.util.*;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;

public class GameScene implements GameObject.IDrawer {	
	protected int mWidth  = 0;
	protected int mHeight = 0;
	protected Resources mRes = null;
	protected List<GameObject> mObjects = new ArrayList<GameObject>();
		
	public List<GameObject> getGameObjects() { return mObjects; }
	
	public GameScene(Resources res) {
		mRes = res;
	}
	
	public void load() {}
	
	public void release() {
		for (GameObject obj : mObjects) {
			obj.release();
		}
	}
	
	protected void onSizeChanged(int width, int height) {
		mWidth  = width;
		mHeight = height;
		
		for (GameObject obj : mObjects) {
			obj.onSizeChanged(width, height);
		}
	}
	
	public void doDraw(Canvas c) {
		c.drawColor(Color.BLUE);
	}
}
