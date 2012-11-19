package edu.neu.madcourse.binbo.rocketrush.tutorial;


import edu.neu.madcourse.binbo.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;

public class TutorialActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ViewPagerAdapter mAdapter;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setupView();
        setTab();
    }
    private void setupView(){    	
    	//mViewPager = (ViewPager) findViewById(R.id.viewPager);
    	mAdapter = new ViewPagerAdapter(getApplicationContext(), getSupportFragmentManager());
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
					break;
					
				case 1:
					findViewById(R.id.first_tab).setVisibility(View.INVISIBLE);
					findViewById(R.id.second_tab).setVisibility(View.VISIBLE);
					break;
				}
			}
				
		});    	

    }
}
