package edu.neu.madcourse.binbo.rocketrush.splash;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.BaseThread;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;
import edu.neu.madcourse.binbo.rocketrush.tutorial.TutorialActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

public class SplashActivity extends Activity {

	private SplashView mView = null;
	private SplashThread mThread = null;
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
        mThread = new SplashThread(mHandler);
        mThread.start();
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
	
	private final static int MSG_THREAD_JOIN = 1;
	
    // create handler for splash thread
    private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_THREAD_JOIN) {
				try {
					mThread.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				startActivity(mIntent);
				finish();
			}
		}    		
	};
	
	protected class SplashThread extends BaseThread {
		
		public SplashThread(Handler handler) {
			super(handler);
		}

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
                Message msg = mHandler.obtainMessage();     	
                msg.what = MSG_THREAD_JOIN;
                mHandler.sendMessage(msg);                
            }
		}
		
	}
}
