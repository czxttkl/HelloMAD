package edu.neu.madcourse.binbo.rocketrush.tutorial;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class TutorialThree extends Fragment {

	private Context mContext = null;
	private TutorialView mView   = null;	
	
	public static Fragment newInstance(Context context) {
		TutorialThree f = new TutorialThree(context);	
		
		return f;
	}
	
	public TutorialThree(Context context) {
		super();
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.tutorial_3, null);
		mView = (TutorialView)root.findViewById(R.id.tutorialView3);		
		return root;
	}		
	
	@Override
	public void onPause() {
		mView.onPause();
		super.onPause();
	}
}
