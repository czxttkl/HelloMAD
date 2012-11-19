package edu.neu.madcourse.binbo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.madcourse.binbo.boggle.Boggle;
import edu.neu.madcourse.binbo.persistentboggle.PBWelcome;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;
import edu.neu.madcourse.binbo.sudoku.Sudoku;
import edu.neu.mobileClass.*;

public class HelloMAD extends Activity implements OnClickListener {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Set up click listeners for all the buttons
		View teamButton = this.findViewById(R.id.about_button);
		teamButton.setOnClickListener(this);
		View sudokuButton = this.findViewById(R.id.sudoku_button);
		sudokuButton.setOnClickListener(this);
		View boggleButton = this.findViewById(R.id.boggle_button);
		boggleButton.setOnClickListener(this);
		View errorButton = this.findViewById(R.id.error_button);
		errorButton.setOnClickListener(this);
		View quitButton = this.findViewById(R.id.quit_button);
		quitButton.setOnClickListener(this);
		View pbgameButton = this.findViewById(R.id.persistent_boggle_button);
		pbgameButton.setOnClickListener(this);		
		View trickiestButton = this.findViewById(R.id.trickiest_button);
		trickiestButton.setOnClickListener(this);
		
		//PhoneCheckAPI.doAuthorization(this);
	}

	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
		case R.id.about_button:
			i = new Intent(this, About.class);
			startActivity(i);
			break;
		case R.id.sudoku_button:
			i = new Intent(this, Sudoku.class);
			startActivity(i);
			break;
		case R.id.boggle_button:
			i = new Intent(this, Boggle.class);
			startActivity(i);
			break;
		case R.id.error_button:
			createError();
			break;
		case R.id.quit_button:
			finish();
			break;
		case R.id.persistent_boggle_button:
			i = new Intent(this, PBWelcome.class);
			startActivity(i);
			break;
		case R.id.trickiest_button:
			i = new Intent(this, RocketRushActivity.class);
			startActivity(i);
			break;
		// More buttons go here (if any) ...
		}
	}

	private void createError() {
		int n = 0;
		n = 1 / 0;
	}
}