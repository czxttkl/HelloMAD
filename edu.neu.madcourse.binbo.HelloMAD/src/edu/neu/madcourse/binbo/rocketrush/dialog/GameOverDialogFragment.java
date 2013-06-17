package edu.neu.madcourse.binbo.rocketrush.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameMode;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;

public class GameOverDialogFragment extends DialogFragment implements OnClickListener {
	protected RocketRushActivity mActivity = null;
	protected GameMode mGameMode = null;
	protected ImageButton mRestartButton = null;
	protected ImageButton mExitButton    = null;	
	protected TextView mDistanceTextView = null;
	protected int mDistance = 0;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mActivity = (RocketRushActivity) getActivity();
		mGameMode = mActivity.getCurrentGameMode();		
	    // Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
		final View layout = inflater.inflate(R.layout.rocket_rush_gameover, null);		
	    builder.setView(layout);
	    // setup subviews
	    mDistanceTextView = (TextView) layout.findViewById(R.id.gameover_distanceText);
	    mRestartButton = (ImageButton) layout.findViewById(R.id.gameover_restartButton);
		mExitButton    = (ImageButton) layout.findViewById(R.id.gameover_exitButton);		
		mRestartButton.setOnClickListener(this);
		mExitButton.setOnClickListener(this);
		
		mDistanceTextView.setText(String.valueOf(mDistance));
		mDistanceTextView.setTextColor(Color.RED);
		//AlertDialog dialog = builder.create();			    
	    return builder.create();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gameover_restartButton:
			mGameMode.restart();
			break;
		case R.id.gameover_exitButton:
			mActivity.switchGameMode(0);
			break;
		}
		dismiss();
	}

	public void setDistance(int distance) {
		mDistance = distance;	
	}
}
