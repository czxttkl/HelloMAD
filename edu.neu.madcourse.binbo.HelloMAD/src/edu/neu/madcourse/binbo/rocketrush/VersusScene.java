package edu.neu.madcourse.binbo.rocketrush;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;


public class VersusScene extends GameScene {

	private Context mContext = null;
	
	public VersusScene(Context context) {
		super(context.getResources());
		mContext = context;
	}
	
	@Override
	public void reset() {
		release();
		load();
	}

	@Override
	public void doDraw(Canvas c) {		
		c.drawColor(Color.GRAY);		
	}

	@Override
	protected void onSizeChanged(int width, int height) {
		// TODO Auto-generated method stub
		super.onSizeChanged(width, height);
	}

}
