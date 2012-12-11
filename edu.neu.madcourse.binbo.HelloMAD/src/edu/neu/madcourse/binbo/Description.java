package edu.neu.madcourse.binbo;

import edu.neu.madcourse.binbo.rocketrush.splash.SplashActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TableLayout;

public class Description extends Activity implements OnClickListener {
	protected Button mStartButton = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rocket_rush_desc);	
		// get views and set listeners
		setupViews();
		// adjust layouts according to the screen resolution
		adjustLayout();        
	}
	
	private void setupViews() {
		mStartButton = (Button) findViewById(R.id.start_rr);
		mStartButton.setOnClickListener(this);
	}
	
	private void adjustLayout() {
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN
		); 
		// get screen resolution
		DisplayMetrics dm = new DisplayMetrics();  
        Display display = getWindowManager().getDefaultDisplay(); 		
        display.getMetrics(dm);        
        // adjust the layout according to the screen resolution
//	 	LinearLayout main = (LinearLayout)findViewById(R.id.rocketrankRoot);				   
		LayoutParams laParams = null;
		ScrollView scrollView = (ScrollView)findViewById(R.id.ScrlViewDescription);
		laParams = scrollView.getLayoutParams();
		laParams.height = (int) (dm.heightPixels * 0.6f);
		scrollView.setLayoutParams(laParams);
	}

	public void onClick(View v) {
		Intent i = new Intent(this, SplashActivity.class);
		startActivity(i);
		finish();
	}

}
