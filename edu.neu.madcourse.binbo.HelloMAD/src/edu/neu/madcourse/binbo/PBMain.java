package edu.neu.madcourse.binbo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class PBMain extends Activity implements OnClickListener {
	private TextView mTVAccName;
	private String mAccName;
	private static final String ACCOUNT_NAME = "account_name";
	
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_main);

		Intent intent = getIntent();
		this.mAccName = intent.getStringExtra(ACCOUNT_NAME);
		this.mTVAccName = (TextView) this.findViewById(R.id.pbmain_account_name_label);
		this.mTVAccName.setText("Hi, " + this.mAccName);
		
		// Set up click listeners for all the buttons
		View btnNewGame = this.findViewById(R.id.pbmain_new_game_button);
		btnNewGame.setOnClickListener(this);
		View btnRank = this.findViewById(R.id.pbmain_rank_button);
		btnRank.setOnClickListener(this);
		View btnOptions = this.findViewById(R.id.pbmain_options_button);
		btnOptions.setOnClickListener(this);
		View btnAbout = this.findViewById(R.id.pbmain_about_button);
		btnAbout.setOnClickListener(this);
		View btnExit = this.findViewById(R.id.pbmain_exit_button);
		btnExit.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
			case R.id.pbmain_new_game_button:
				i = new Intent(this, PBInvite.class);
				i.putExtra(this.ACCOUNT_NAME, this.mAccName);
				finish();
				startActivity(i);
				break;
			case R.id.pbmain_exit_button:
				i = new Intent(this, HelloMAD.class);
				finish();
				startActivity(i);
				break;
		}
	}
}
