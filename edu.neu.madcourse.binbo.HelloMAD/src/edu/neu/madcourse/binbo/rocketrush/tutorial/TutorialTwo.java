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

public class TutorialTwo extends Fragment implements OnClickListener {
	
	private Context mContext = null;
	private TutorialView mView   = null;
	private ImageButton  mButton = null;
	
	public static Fragment newInstance(Context context) {
		TutorialTwo f = new TutorialTwo(context);	
		
		return f;
	}
	
	public TutorialTwo(Context context) {
		super();
		mContext = context;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.tutorial_2, null);
		mView = (TutorialView)root.findViewById(R.id.tutorialView2);
		mButton = (ImageButton)root.findViewById(R.id.skipButton);
		mButton.setOnClickListener(this);		
		return root;
	}
	
	public void onClick(View v) {
		Intent i = null;
		TutorialActivity activity = (TutorialActivity) mContext;
		
		switch (v.getId()) {
		case R.id.skipButton:	
			if (activity.mStartNewActivity) {
				i = new Intent(activity, RocketRushActivity.class);
				startActivity(i);
			}
			activity.finish();
			break;		
		}
	}
	
	@Override
	public void onPause() {
		mView.onPause();
		super.onPause();
	}
}
