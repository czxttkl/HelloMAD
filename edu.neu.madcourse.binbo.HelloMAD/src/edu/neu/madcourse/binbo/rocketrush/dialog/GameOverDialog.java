package edu.neu.madcourse.binbo.rocketrush.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import edu.neu.madcourse.binbo.R;

public class GameOverDialog extends Activity implements OnClickListener {
	
	//protected RocketRushActivity mActivity = null;
	//protected GameMode mGameMode = null;
	public final static int BACK_TO_MENU = RESULT_FIRST_USER + 0;
	public final static int RESTART_GAME = RESULT_FIRST_USER + 1;
	protected ImageButton mRetryButton = null;
	protected ImageButton mExitButton  = null;	
	protected TextView mTextView = null;
	protected int mDistance = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rocket_rush_gameover);
		mDistance = getIntent().getIntExtra("distance", 0);
		// setup subviews
	    mTextView    = (TextView) findViewById(R.id.gameover_distanceText);
	    mRetryButton = (ImageButton) findViewById(R.id.gameover_restartButton);
		mExitButton  = (ImageButton) findViewById(R.id.gameover_exitButton);		
		mRetryButton.setOnClickListener(this);
		mExitButton.setOnClickListener(this);
				
		mTextView.setTextColor(Color.RED);
		mTextView.getPaint().setFakeBoldText(true);
		mTextView.setText(String.valueOf(mDistance));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gameover_restartButton:
			setResult(RESTART_GAME);
			finish();
			break;
		case R.id.gameover_exitButton:
			setResult(BACK_TO_MENU);
			finish();
			break;		
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:		
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
