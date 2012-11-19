package edu.neu.madcourse.binbo.rocketrush.splash;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;
import edu.neu.madcourse.binbo.rocketrush.tutorial.TutorialActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	private SplashView mView = null;
	private boolean mActive = true;
	private int mSplashTime = 2000;
	private Intent mIntent = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        setupView();    
        createIntent();
        // thread for displaying the SplashScreen
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (mActive && (waited < mSplashTime)) {
                        sleep(100);
                        if (mActive) {
                            waited += 100;
                        }
                    }
                } catch(InterruptedException e) {
                    // do nothing
                } finally {
                    finish();
                    startActivity(mIntent);
                    //stop();
                }
            }
        };
        splashTread.start();
	}
	
	private void setupView() {
		mView = (SplashView) findViewById(R.id.splashView);
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	protected void createIntent() {
		if (isOpenedFirstTime()) {
			mIntent = new Intent(this, TutorialActivity.class);
		} else {
			mIntent = new Intent(this, RocketRushActivity.class);
		}
	}
	
	protected boolean isOpenedFirstTime() {
		return true;
	}
}
