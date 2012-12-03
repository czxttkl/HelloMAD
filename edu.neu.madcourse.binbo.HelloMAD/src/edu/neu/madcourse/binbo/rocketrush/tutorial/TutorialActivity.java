package edu.neu.madcourse.binbo.rocketrush.tutorial;


import edu.neu.madcourse.binbo.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

public class TutorialActivity extends FragmentActivity {
	private ViewPager mViewPager; // container for all tab views
	private ViewPagerAdapter mAdapter;
	public boolean mStartNewActivity = true;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tutorial);

		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN
		);  
    	
        String creator = getIntent().getStringExtra("edu.neu.madcourse.binbo.rocketrush.Main");
        if (creator != null) {
        	// some activity starts this, no need to start a new
        	// activity when this activity is finished
        	mStartNewActivity = false; 
        }
        
        setupView();
        setTab();
    }
    
    private void setupView(){    	
    	mViewPager = (ViewPager)findViewById(R.id.viewPager);
    	mAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
	    mViewPager.setAdapter(mAdapter);
	    mViewPager.setCurrentItem(0);
    }
    
    private void setTab() {
    	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				    		
			public void onPageScrollStateChanged(int position) {}

			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:
					findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.third_tab).setVisibility(View.INVISIBLE);
					break;					
				case 1:
					findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
					findViewById(R.id.third_tab).setVisibility(View.INVISIBLE);
					break;
				case 2:
					findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.third_tab).setVisibility(View.VISIBLE);
					break;
				}
			}
				
		});    	

    }
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (mStartNewActivity)
				return true;
			finish();
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
