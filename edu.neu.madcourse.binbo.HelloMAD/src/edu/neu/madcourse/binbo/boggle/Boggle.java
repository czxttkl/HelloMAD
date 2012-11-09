package edu.neu.madcourse.binbo.boggle;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.R.id;
import edu.neu.madcourse.binbo.R.layout;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;

public class Boggle extends Activity implements OnClickListener {

	private static final String TAG = "Boggle";
	private static final int BOGGLE_GAME_ACTIVITY = 1;
	private static final String BOGGLE_CONTINUE_CONDITION = "boggle_continue";
	
	private int enable_continue = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boggle);

		// Set up click listeners for all the buttons
		View continueButton = findViewById(R.id.boggle_continue_button);
		continueButton.setOnClickListener(this);
		Button newButton = (Button)findViewById(R.id.boggle_new_button);
		newButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.boggle_about_button);
		aboutButton.setOnClickListener(this);
		View acknowledgeButton = findViewById(R.id.boggle_acknowledge_button);
		acknowledgeButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.boggle_exit_button);
		exitButton.setOnClickListener(this);
		
		enable_continue = getPreferences(MODE_PRIVATE).getInt(BOGGLE_CONTINUE_CONDITION, 0);
		if (enable_continue == 0) {
			continueButton.setEnabled(false);
		}
	}

	public void onClick(View v) {
		Intent i = null;
		
		switch (v.getId()) {
		case R.id.boggle_continue_button:			
			i = new Intent(this, BoggleGame.class);		
			i.putExtra(BoggleGame.KEY_COMMAND, BoggleGame.CONTINUE);
			startActivity(i);
			break;
		case R.id.boggle_new_button:
			i = new Intent(this, BoggleGame.class);			
			startActivityForResult(i, BOGGLE_GAME_ACTIVITY);
			break;
		case R.id.boggle_about_button:
			i = new Intent(this, BoggleAbout.class);
			startActivity(i);
			break;
		case R.id.boggle_acknowledge_button:
			i = new Intent(this, BoggleAcknowledge.class);
			startActivity(i);
			break;		
		case R.id.boggle_exit_button:
			finish();
			break;
		}
	}

//	private void startGame(int i) {
//		Log.d(TAG, "clicked on " + i);
//		Intent intent = new Intent(this, BoggleGame.class);
//		startActivity(intent);
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		
		if (requestCode == BOGGLE_GAME_ACTIVITY) {
			enable_continue = 1;
			View continueButton = findViewById(R.id.boggle_continue_button);
			continueButton.setEnabled(true);
			getPreferences(MODE_PRIVATE).edit().putInt(
					BOGGLE_CONTINUE_CONDITION, enable_continue).commit();
		}		
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		// Save the time left
		getPreferences(MODE_PRIVATE).edit().putInt(
				BOGGLE_CONTINUE_CONDITION, enable_continue).commit();
	}
}
