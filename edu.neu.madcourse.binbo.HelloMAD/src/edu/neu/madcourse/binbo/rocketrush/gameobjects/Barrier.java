package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import edu.neu.madcourse.binbo.rocketrush.GameCtrl;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Barrier extends GameObject {

	protected int mCanvasWidth  = 0;
	protected int mCanvasHeight = 0;
	protected int mAccMoveDuration = 0;
	
	protected Barrier(Resources res) {
		super(res);		
	}

	public void triggerCollideEffect() {}
	
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
