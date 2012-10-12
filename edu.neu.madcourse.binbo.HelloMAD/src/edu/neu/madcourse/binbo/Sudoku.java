/***
 * Excerpted from "Hello, Android",
 * published by The Pragmatic Bookshelf.
 * Copyrights apply to this code. It may not be used to create training material, 
 * courses, books, articles, and the like. Contact us if you are in doubt.
 * We make no guarantees that this code is fit for any purpose. 
 * Visit http://www.pragmaticprogrammer.com/titles/eband3 for more book information.
 ***/
package edu.neu.madcourse.binbo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Sudoku extends Activity implements OnClickListener {
	private static final String TAG = "Sudoku";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sudoku);

		// Set up click listeners for all the buttons
		View continueButton = findViewById(R.id.sudoku_continue_button);
		continueButton.setOnClickListener(this);
		View newButton = findViewById(R.id.sudoku_new_button);
		newButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.sudoku_about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.sudoku_exit_button);
		exitButton.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SudokuMusic.play(this, R.raw.main);
	}

	@Override
	protected void onPause() {
		super.onPause();
		SudokuMusic.stop(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sudoku_continue_button:
			startGame(SudokuGame.DIFFICULTY_CONTINUE);
			break;
		// ...
		case R.id.sudoku_about_button:
			Intent i = new Intent(this, SudokuAbout.class);
			startActivity(i);
			break;
		// More buttons go here (if any) ...
		case R.id.sudoku_new_button:
			openNewGameDialog();
			break;
		case R.id.sudoku_exit_button:
			finish();
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sudoku_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, SudokuPrefs.class));
			return true;
			// More items go here (if any) ...
		}
		return false;
	}

	/** Ask the user what difficulty level they want */
	private void openNewGameDialog() {
		new AlertDialog.Builder(this)
				.setTitle(R.string.sudoku_new_game_title)
				.setItems(R.array.difficulty,
						new DialogInterface.OnClickListener() {
							public void onClick(
									DialogInterface dialoginterface, int i) {
								startGame(i);
							}
						}).show();
	}

	/** Start a new game with the given difficulty level */
	private void startGame(int i) {
		Log.d(TAG, "clicked on " + i);
		Intent intent = new Intent(this, SudokuGame.class);
		intent.putExtra(SudokuGame.KEY_DIFFICULTY, i);
		startActivity(intent);
	}
}