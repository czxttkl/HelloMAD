package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.rocketrush.GameObject;
import edu.neu.madcourse.binbo.rocketrush.IDrawer;

public class Asteroid extends GameObject {
	private Bitmap mImage = null;

	public Asteroid(Bitmap image) {
		setImage(image);
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
	}

	@Override
	public int doDraw(Canvas c) {
		// TODO Auto-generated method stub
		return super.doDraw(c);
	}

}
