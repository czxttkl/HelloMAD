package edu.neu.madcourse.binbo.rocketrush.tutorial;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;

public class TutorialEnd extends Fragment {
	private Context mContext = null;
	private TutorialView mView   = null;
	private ImageButton mButton = null;
	
	public static Fragment newInstance(Context context) {
		TutorialEnd f = new TutorialEnd(context);	
		
		return f;
	}
	
	public TutorialEnd(Context context) {
		super();
		mContext = context;				
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.tutorial_end, null);
		mView = (TutorialView)root.findViewById(R.id.tutorialEndView);		
		return root;
	}

	@Override
	public void onPause() {
		mView.onPause();
		super.onPause();
	}
		
}
