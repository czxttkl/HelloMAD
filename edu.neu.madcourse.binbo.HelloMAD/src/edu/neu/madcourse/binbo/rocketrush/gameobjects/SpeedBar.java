package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class SpeedBar extends GameObject implements GameObject.IDrawer {
	protected Bitmap mImage = null;

	public SpeedBar(Resources res) {
		super(res);
	}
	
	public SpeedBar(Resources res, Bitmap image) {
		super(res);
		setImage(image);
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
	}

	@Override
	public void doDraw(Canvas c) {
		// TODO Auto-generated method stub
	}
}
