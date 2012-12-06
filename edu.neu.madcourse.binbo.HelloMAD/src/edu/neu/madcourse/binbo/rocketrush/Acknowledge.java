package edu.neu.madcourse.binbo.rocketrush;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import edu.neu.madcourse.binbo.R;

public class Acknowledge extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rocket_rush_ac);
		
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN
		); 
	}
}
