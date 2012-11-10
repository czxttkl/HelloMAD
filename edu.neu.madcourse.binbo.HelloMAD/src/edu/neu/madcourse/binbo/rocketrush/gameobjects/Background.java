package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Background extends GameObject {
	private Bitmap mImage = null;

	public Background(Bitmap image) {
		setImage(image);
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
	}

	@Override
	public int doDraw(Canvas c) {
		c.drawBitmap(mImage, 0, 0, null);
		
		return super.doDraw(c);
	}
}
