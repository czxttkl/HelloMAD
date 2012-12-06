package edu.neu.madcourse.binbo.rocketrush.gameobjects;

import android.content.res.Resources;
import edu.neu.madcourse.binbo.rocketrush.GameObject;

public class Utility extends GameObject {
	protected boolean mEnable = true;

	protected Utility(Resources res) {
		super(res);
		// TODO Auto-generated constructor stub
	}

	public void setEnable(boolean enable) {
		mEnable = enable;
	}
	
	public boolean isEnabled() {
		return mEnable;
	}
}
