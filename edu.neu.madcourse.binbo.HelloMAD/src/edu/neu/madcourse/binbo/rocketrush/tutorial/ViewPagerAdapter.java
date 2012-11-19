package edu.neu.madcourse.binbo.rocketrush.tutorial;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class ViewPagerAdapter extends FragmentPagerAdapter {

	private Context mContext;
	
	public ViewPagerAdapter(Context context, FragmentManager fm) {
		super(fm);	
		mContext = context;		
	}

	@Override
	public Fragment getItem(int position) {
		Fragment f = null;
		
		switch (position){
		case 0:
			f = TutorialOne.newInstance(mContext);	
			break;
		case 1:
			f = TutorialTwo.newInstance(mContext);	
			break;
		case 2:
			f = TutorialThree.newInstance(mContext);
			break;
		}
		
		return f;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
