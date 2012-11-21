package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameEngine;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Asteroid extends GameObject implements GameObject.IDrawer {
	final static int IMAGE_COUNT = 12; // the same size of the total number of bitmaps
	protected static boolean sImageLoaded = false;
	protected static List<Bitmap> sImages = new ArrayList<Bitmap>();
	protected Bitmap mImage = null;
	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	private int mAccMoveDuration = 0;
	
	public float DEFAULT_SPEED_X = 0;
	public float DEFAULT_SPEED_Y = 3;
	
	protected Random mRand = new Random();
	
	public static void loadImages(Resources res) {
		if (sImageLoaded) {
			return;
		}
		sImageLoaded = true;
		
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid01));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid02));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid03));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid04));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid05));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid06));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid07));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid08));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid09));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid10));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid11));
		sImages.add(BitmapFactory.decodeResource(res, R.drawable.asteroid12));
	}
	
	public Asteroid(Resources res) {
		super(res);
		loadImages(res);
		setKind(ASTEROID);
		setMovable(true);
		initSpeeds(DEFAULT_SPEED_X, DEFAULT_SPEED_Y, 0);		
		setZOrder(ZOrders.ASTEROID);		
		setImage(sImages.get(mRand.nextInt(IMAGE_COUNT)));
	}

	public Asteroid(Resources res, Bitmap image) {
		super(res);
		loadImages(res);
		setKind(ASTEROID);
		setMovable(true);
		initSpeeds(DEFAULT_SPEED_X, DEFAULT_SPEED_Y, 0);		
		setZOrder(ZOrders.ASTEROID);
		setImage(sImages.get(mRand.nextInt(IMAGE_COUNT)));		
	}
	
	public void initSpeeds(float x, float y, int accTime) {
		DEFAULT_SPEED_X = x;
		DEFAULT_SPEED_Y = y;		
		float accSpeedY = y / (1000 / GameEngine.ENGINE_SPEED);
		setSpeed(x, y + accSpeedY * accTime);
		setMaxSpeed(x, y * 3);
		setAccSpeed(0, accSpeedY);
	}

	public void setImage(Bitmap image) {
		mImage = image;
		setWidth(image.getWidth());
		setHeight(image.getHeight());
	}

	@Override
	public void doDraw(Canvas c) {
		if (mX + mWidth <= 0 || mX >= mCanvasWidth ||
			mY + mHeight <= 0 || mY >= mCanvasHeight) {
			return; // not necessary to draw the invisible
		}
		c.drawBitmap(mImage, mX, mY, null);
	}	

	@Override
	public void update() {		
		if (mAccMoveDuration > 0) {
			mSpeedY = Math.min(mSpeedY + mAccSpeedY, mMaxSpeedY);
			mAccMoveDuration -= GameEngine.ENGINE_SPEED;
		} else {
			mSpeedY = Math.max(mSpeedY - mAccSpeedY, DEFAULT_SPEED_Y);
		}
		
		mX += mSpeedX;
		mY += mSpeedY;		
	}
	
	@Override
	public void operate(GameCtrl ctrl) {
		int command = ctrl.getCommand();
		
		if (command == GameCtrl.MOVE_VERT) {
			mAccMoveDuration = 1000;
		} 
	}

	@Override
	public void onSizeChanged(int width, int height) {
		mCanvasWidth  = width;
		mCanvasHeight = height;		
	}

}
