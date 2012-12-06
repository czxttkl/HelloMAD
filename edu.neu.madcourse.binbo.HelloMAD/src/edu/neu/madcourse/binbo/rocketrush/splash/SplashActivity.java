package edu.neu.madcourse.binbo.rocketrush.splash;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.BaseThread;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;
import edu.neu.madcourse.binbo.rocketrush.tutorial.TutorialActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;

public class SplashActivity extends Activity {

	private SplashView mView = null;
	private SplashThread mThread = null;
	private boolean mActive = true;
	private int mSplashTime = 2000;	
	private Intent mIntent = null;
	private boolean mFirst = true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN
		); 
        // get flag which indicate whether the app is opened for the first time
        mFirst = getPreferences(MODE_PRIVATE).getBoolean("first", true);
        
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
		super.onPause();
	}

	@Override
	protected void onResume() {		
		super.onResume();
	}


	@Override
	protected void onDestroy() {
		getPreferences(MODE_PRIVATE).edit().putBoolean("first", false).commit();
		mView.release();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}	
	
	protected void createIntent() {
		if (isFirstOpened()) {
			mIntent = new Intent(this, TutorialActivity.class);
		} else {
			mIntent = new Intent(this, RocketRushActivity.class);
		}
	}
	
	protected boolean isFirstOpened() {
		return mFirst;
	}		
	
	private final static int MSG_THREAD_JOIN     = 1;
	private final static int MSG_UPDATE_PROGRESS = 2;
	
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
			} else if (msg.what == MSG_UPDATE_PROGRESS) {		
				int progress = mView.getProgress();
				mView.updateProgress(progress + 1);
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
                	synchronized (this) {
                		wait(20);
	                    if (mActive) {
	                        waited += 20;
	                    }
                	}
                	Message msg = mHandler.obtainMessage();     	
                    msg.what = MSG_UPDATE_PROGRESS;
                    mHandler.sendMessage(msg); 
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
