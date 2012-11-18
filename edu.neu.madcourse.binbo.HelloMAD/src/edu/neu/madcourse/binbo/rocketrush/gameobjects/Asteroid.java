package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Asteroid extends GameObject implements GameObject.IDrawer {
	final static int IMAGE_COUNT = 12; // the same size of the total number of bitmaps
	protected Bitmap mImage = null;
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	
	public Asteroid(Resources res) {
		super(res);
		setMovable(true);
		setSpeed(0, 3);
		setZOrder(ZOrders.ASTEROID);
		setImage(loadImage());
	}

	public Asteroid(Resources res, Bitmap image) {
		super(res);
		setMovable(true);
		setSpeed(0, 3);
		setZOrder(ZOrders.ASTEROID);
		setImage(image);		
	}
	
	public void setImage(Bitmap image) {
		mImage = image;
		setWidth(image.getWidth());
		setHeight(image.getHeight());
	}

	@Override
	public void doDraw(Canvas c) {
		c.drawBitmap(mImage, mX, mY, null);
	}

	protected Bitmap loadImage() {
		Bitmap image = null;
		Random rand = new Random();
		
		// randomly load image of an asteroid		
		switch (rand.nextInt(IMAGE_COUNT)) {
		case 0: 
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid01);
			break;
		case 1:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid02);
			break;
		case 2:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid03);
			break;
		case 3:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid04);
			break;
		case 4:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid05);
			break;
		case 5:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid06);
			break;
		case 6:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid07);
			break;
		case 7:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid08);
			break;
		case 8:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid09);
			break;
		case 9:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid10);
			break;
		case 10:			
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid11);
			break;
		case 11:
			image = BitmapFactory.decodeResource(mRes, R.drawable.asteroid12);
			break;
		}
		
		return image;
	}

	@Override
	public void update() {
		mX += mSpeedX;
		mY += mSpeedY;
	}

	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;
		
	}
}
