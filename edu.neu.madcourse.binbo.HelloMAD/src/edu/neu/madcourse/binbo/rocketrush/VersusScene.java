package edu.neu.madcourse.binbo.rocketrush;


import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;


public class VersusScene extends GameScene {
	
	public VersusScene(Resources res) {
		super(res);
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
