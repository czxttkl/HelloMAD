package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Rocket extends GameObject implements GameObject.IDrawer  {
	protected List<Bitmap> mImages = new ArrayList<Bitmap>();
	protected int mImageWidth  = 0;
	protected int mImageHeight = 0;	

	public Rocket(Resources res) {
		super(res);
		addImage(BitmapFactory.decodeResource(res, R.drawable.ship2_1));
		addImage(BitmapFactory.decodeResource(res, R.drawable.ship2_2));
		addImage(BitmapFactory.decodeResource(res, R.drawable.ship2_3));
		addImage(BitmapFactory.decodeResource(res, R.drawable.ship2_4));
		mImageWidth  = mImages.get(0).getWidth();
		mImageHeight = mImages.get(0).getHeight();
	}
	
	public Rocket(Resources res, List<Bitmap> images) {
		super(res);
		for (Bitmap image : images) {
			addImage(image);
		}
	}

	public void addImage(Bitmap image) {
		mImages.add(image);
	}

	protected int mCurIndex = 0;
	@Override
	public void doDraw(Canvas c) {
		if (mCurIndex == 4) {
			mCurIndex = 0;
		}
		
		Bitmap image = mImages.get(mCurIndex++);
		c.drawBitmap(image, mX, mY, null);				
	}

	@Override
	public void update() {		
	}

	@Override
	public void release() {
		for (Bitmap image : mImages) {
			image.recycle();				
		}
		super.release();
	}
	
	@Override
	public void onSizeChanged(int width, int height) {
		mX = (width - mImageWidth) / 2;
		mY = (height - mImageHeight) / 2 + height / 4;
	}
}
