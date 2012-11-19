package edu.neu.madcourse.binbo.rocketrush.tutorial;

import edu.neu.madcourse.binbo.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TutorialThree extends Fragment {

	private TutorialView mView = null;
	
	public static Fragment newInstance(Context context) {
		TutorialThree f = new TutorialThree();	
		
		return f;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		ViewGroup root = (ViewGroup) inflater.inflate(R.layout.tutorial_3, null);
		mView = (TutorialView)root.findViewById(R.id.tutorialView3);
		mView.setText("Tutorial 3");
		return root;
	}
	
}
