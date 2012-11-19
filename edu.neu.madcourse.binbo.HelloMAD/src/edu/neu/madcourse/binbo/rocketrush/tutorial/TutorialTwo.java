package edu.neu.madcourse.binbo.rocketrush.tutorial;

import edu.neu.madcourse.binbo.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialTwo extends Fragment {
	
	private TutorialView mView = null;
	
	public static Fragment newInstance(Context context) {
		TutorialTwo f = new TutorialTwo();	
		
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.tutorial_2, null);
		mView = (TutorialView)root.findViewById(R.id.tutorialView2);
		mView.setText("Tutorial 2");
		return root;
	}
	
}
