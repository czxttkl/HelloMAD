package edu.neu.madcourse.binbo.rocketrush.tutorial;


import java.util.List;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.boggle.BogglePuzzleView;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;
import android.content.Intent;
import android.graphics.Point;
import android.opengl.Visibility;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class TutorialActivity extends FragmentActivity implements OnClickListener {
	private ViewPager mViewPager; // container for all tab views
	private ViewPagerAdapter mAdapter;
	private ProgressView mProgView;
	//private ImageButton  mButton = null;
	public boolean mStartNewActivity = true;	
	protected OnTutorialChangedListener mListener = null;
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
    
    public void setOnTutorialChangedListener(OnTutorialChangedListener listener) {
    	mListener = listener;
    }
    
    private void setupView(){    	
    	mViewPager = (ViewPager)findViewById(R.id.viewPager);
    	mAdapter = new ViewPagerAdapter(this, getSupportFragmentManager());
	    mViewPager.setAdapter(mAdapter);
	    mViewPager.setCurrentItem(0);
	    
	    mProgView = (ProgressView)findViewById(R.id.progView);
	    setOnTutorialChangedListener(mProgView);
 		
 		//mButton = (ImageButton) findViewById(R.id.skipButton); 		
		//mButton.setOnClickListener(this);
//		mButton.setVisibility(View.GONE);
    }
    
    private void setTab() {
    	mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
				    		
			public void onPageScrollStateChanged(int position) {}

			public void onPageScrolled(int arg0, float arg1, int arg2) {}

			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				if (mListener != null) {
					mListener.OnTutorialChanged(position);
				}
//				if (position == 2) {
//					mButton.setVisibility(View.VISIBLE);
//				} else {
//					mButton.setVisibility(View.GONE);
//				}
				
//				switch(position) { // the following code is useful when the indicator is used
//				case 0:					
//					findViewById(R.id.first_tab).setVisibility(View.VISIBLE);
//					findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
//					findViewById(R.id.third_tab).setVisibility(View.INVISIBLE);
//					break;					
//				case 1:
//					findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
//					findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
//					findViewById(R.id.third_tab).setVisibility(View.INVISIBLE);
//					break;
//				case 2:
//					findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
//					findViewById(R.id.second_tab).setVisibility(View.INVISIBLE);
//					findViewById(R.id.third_tab).setVisibility(View.VISIBLE);
//					break;
//				}
			}
				
		});    	

    }
    
    public void onClick(View v) {
		Intent i = null;
		
//		switch (v.getId()) {
//		case R.id.endTutorialButton:	
//			if (mStartNewActivity) {
//				i = new Intent(this, RocketRushActivity.class);
//				startActivity(i);
//			}
//			finish();
//			break;		
//		}
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
    
    public interface OnTutorialChangedListener {
		void OnTutorialChanged(int position);
	}
}
