package edu.neu.madcourse.binbo.rocketrush;

import java.util.*;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;

public class GameScene implements GameObject.IDrawer, GameObject.OnCollideListener {	
	protected int mWidth  = 0;
	protected int mHeight = 0;
	protected Resources mRes = null;	
	protected List<GameObject> mObjects  = new ArrayList<GameObject>();
	protected List<GameObject> mBarriers = new ArrayList<GameObject>();
	
	public GameScene(Resources res) {
		mRes = res;
	}
	
	public void reset() {
		
	}
	
	public List<GameObject> load() { return mObjects; }
	
	public void release() {
		for (GameObject obj : mObjects) {
			obj.release();
		}
		for (GameObject obj : mBarriers) {
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
	
	// we may get game objects and generate barriers in different thread,
	// so a little synchronization is necessary here
	public List<GameObject> getGameObjects() { 
		return mObjects; 
	}
	
	public List<GameObject> updateBarriers() {
		return null;
	}
	
	protected void orderByZ(List<GameObject> objects) {		
		Collections.sort(objects, new ZOrderComparator());
	}
	
	protected class ZOrderComparator implements Comparator<GameObject> {
		public int compare(GameObject obj1, GameObject obj2) {
			return obj1.getZOrder() - obj2.getZOrder(); 
		}
	}

	public void onCollide(GameObject obj, List<GameObject> collideWith) {
		// TODO Auto-generated method stub
	}
}
