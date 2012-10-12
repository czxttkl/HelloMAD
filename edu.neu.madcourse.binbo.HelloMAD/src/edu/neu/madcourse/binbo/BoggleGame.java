package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/* after deleting words whose length are over 16, 
   the number of occurrence for each letter is below: 
	a: 326395	8.1552% *
	b: 73910	1.84669%
	c: 169177	4.227%
	d: 129257	3.22957%
	e: 437303	10.9263% *
	f: 46188	1.15404%
	g: 98557	2.46251%
	h: 104164	2.60261%
	i: 355476	8.8818% *
	j: 6416		0.160308%
	k: 33829	0.84524%
	l: 215219	5.37739% *
	m: 119469	2.98501%
	n: 285562	7.13496% *
	o: 280921	7.019% *
	p: 127706	3.19082%
	q: 6784		0.169503%
	r: 280998	7.02092% *
	s: 326647	8.16149% *
	t: 262874	6.56808% *
	u: 146434	3.65875%
	v: 37786	0.944108%
	w: 27811	0.694876%
	x: 11837	0.295755%
	y: 74044	1.85004%
	z: 17531	0.438024%
   the sum of all occurrences is 4002295 
*/

public class BoggleGame extends Activity implements OnClickListener {
	private static final String TAG = "Boggle";	
	private static final String BOGGLE_PUZZLE = "puzzle";
	private static final String BOGGLE_TIME = "time_left";
	private static final String BOGGLE_GAME_PAUSED = "game_paused";
	private static final String BOGGLE_GAME_OVER = "game_over";
	private static final String BOGGLE_GAME_SCORE = "game_score";
	private static final String BOGGLE_GAME_BEST_SCORE = "game_best_score";
	private static final String BOGGLE_WORDS = "words";
	private static final String BOGGLE_WORDS_SIZE = "words_size";
	private static final int DEFAULT_GAME_TIME = 179; 
	public static final String KEY_COMMAND = "edu.neu.madcourse.binbo.boggle.command";
	public static final int NEW_GAME = 0;
	public static final int CONTINUE = 1;	
	
	private NativeDictionary dict = null;
	private char puzzle[] = null;
	private String boggleString = "";
	private BogglePuzzleView puzzleView;
	
	private final int FREQUENCY[] = {
		326395, 73910, 169177, 129257, 437303, 46188, 98557, 104164, 
		355476, 6416, 33829, 215219, 119469, 285562, 280921, 127706, 
		6784, 280998, 326647, 262874, 146434, 37786, 27811, 11837, 74044, 17531
	};
	private final int SUM_OF_CHARS = 4002295;
	private final String high_frequency[] = { 
		"a", "e", "i", "l", "n", "o", "r", "s", "t"
	};
	
	private final float GOLDEN_DIVIDE = 0.618f;
	
	private int game_time = DEFAULT_GAME_TIME;
	private boolean paused = false;
	private boolean game_over = false;
	private TextView timeView  = null;	
	private TextView bestView  = null;
	private TextView scoreView = null;
	private Button pauseButton = null;
	//private Button resetButton = null;
	private ListView listView  = null;
	
	private int game_score = 0; 
	private int game_best_score = 0;	
	private List<String> wordsFound = new ArrayList<String>();
	
	private ToneGenerator tonePlayer = null;  
	private int defTextColor = Color.WHITE;
	
	Handler colorHandler = new Handler();
	Runnable colorRunnable = new Runnable() {
		public void run() {
			int curColor = timeView.getCurrentTextColor(); 
			
			timeView.setTextColor(
				(curColor == defTextColor ? Color.RED : defTextColor));
			colorHandler.postDelayed(this, 500);
		}
	};
	
	Handler textHandler  = new Handler();
	Runnable textRunnable = new Runnable() {
	    public void run() {	        
	    	// format and set the time
	    	game_time--;
	    	String minute = "" + game_time / 60;
	    	String second = "" + game_time % 60;
	    	if (second.length() == 1) {
	    		second = "0" + second;
	    	}
	    	
	    	if (game_time <= 20) {
	    		if (game_time == 20) {
	    			colorHandler.postDelayed(colorRunnable, 500);
	    		}
	    		if (tonePlayer != null) {
	    			tonePlayer.startTone(ToneGenerator.TONE_DTMF_5, 100);
	    		}	    		
	    	} 
	    	timeView.setText(minute + ":" + second);	    	
	    	
	    	if (game_time > 0) { 
	    		textHandler.postDelayed(this, 1000);
	    	} else {	    	
	    		timeView.setTextColor(defTextColor);
	    		doGameOver();
	    	}
	    }
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				
		int command = getIntent().getIntExtra(KEY_COMMAND, NEW_GAME);
		puzzle = getPuzzle(command);
		
		loadPreferences(command);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_FULLSCREEN
		);
		setContentView(R.layout.boggle_game);					
        
		// get views
		timeView  = (TextView)findViewById(R.id.textViewTime);
		bestView  = (TextView)findViewById(R.id.textViewBestScore);
		scoreView = (TextView)findViewById(R.id.textViewScore);
		listView  = (ListView)findViewById(R.id.listView);
		//resetButton = (Button)findViewById(R.id.boggle_reset_button); 		
 		pauseButton = (Button)findViewById(R.id.boggle_pause_button);
 		// get current text color of timeView
 		defTextColor = timeView.getCurrentTextColor();
	
 		// register events handler
 		//resetButton.setOnClickListener(this);
		pauseButton.setOnClickListener(this);				      
        
		// adjust layouts according to the screen resolution
		DisplayMetrics dm = new DisplayMetrics();  
        Display display = getWindowManager().getDefaultDisplay(); 		
        display.getMetrics(dm);
		if (dm.heightPixels >= dm.widthPixels) {
			adjustPortraitLayout(dm.widthPixels, dm.heightPixels);
		} else if (dm.widthPixels > dm.heightPixels) {
			adjustLandscapeLayout(dm.widthPixels, dm.heightPixels);
		}		

        // load dictionaries of high frequency
		dict = new NativeDictionary(getAssets());
        for (int i = 0; i < high_frequency.length; ++i) {        	
        	// use ".mpg" to make sure that android won't consider the files in the pkg
        	// as the compressed files, or it won't be available to get a valid native fd.
        	// these files have been compressed to huffman coding.
        	// due to the singleton pattern used in NDK code, if the corresponding wordlist
        	// has been loaded before, it won't be loaded again when the activity is created again.
        	String dictName = "" + high_frequency[i];
        	if (!dict.isLoaded(dictName)) {
        		dict.load("wordlist_" + dictName + ".mpg", dictName, getAssets());
        	}
        }                
 
 		// If the activity is restarted, do a continue next time
 		getIntent().putExtra(KEY_COMMAND, CONTINUE); 	
 		tonePlayer = new ToneGenerator(AudioManager.STREAM_MUSIC, 70); 
 		
 		// create the boggle music
 		BoggleMusic.create(this, R.raw.boggle_game);
 		if (command == NEW_GAME) {
 			BoggleMusic.reset();
 		}
	}
	
	@Override
	protected void onDestroy() {
		BoggleMusic.stop(this);
		// TODO Auto-generated method stub
		super.onDestroy();		
	}
	
	private void loadPreferences(int command) {
		if (command == CONTINUE) {
			game_time = getPreferences(MODE_PRIVATE).getInt(BOGGLE_TIME, DEFAULT_GAME_TIME);
			game_score = getPreferences(MODE_PRIVATE).getInt(BOGGLE_GAME_SCORE, 0);
			paused = getPreferences(MODE_PRIVATE).getBoolean(BOGGLE_GAME_PAUSED, false);
			game_over = getPreferences(MODE_PRIVATE).getBoolean(BOGGLE_GAME_OVER, false);
			game_over = (game_time > 0) ? false : true;
			int size = getPreferences(MODE_PRIVATE).getInt(BOGGLE_WORDS_SIZE, 0);
			for (int i = 0; i < size; ++i) {								
				String word = getPreferences(MODE_PRIVATE).getString(BOGGLE_WORDS + i, "");
				wordsFound.add(word);
			}	
		} else if (command == NEW_GAME) {
			game_time = DEFAULT_GAME_TIME;
			game_score = 0;			
			paused = false;
			game_over = false;
			wordsFound.clear();
		}
	
		game_best_score = getPreferences(MODE_PRIVATE).getInt(BOGGLE_GAME_BEST_SCORE, 0);
	}
	
	private void savePreferences() {
		// Save the time left
		getPreferences(MODE_PRIVATE).edit().putInt(BOGGLE_TIME, game_time).commit();
		// Save the game score
		getPreferences(MODE_PRIVATE).edit().putInt(BOGGLE_GAME_SCORE, game_score).commit();
		// Save the game best score
		getPreferences(MODE_PRIVATE).edit().putInt(BOGGLE_GAME_BEST_SCORE, game_best_score).commit();
		// Save the game state
		getPreferences(MODE_PRIVATE).edit().putBoolean(BOGGLE_GAME_PAUSED, paused).commit();
		// Save the words list BOGGLE_WORDS_SIZE
		getPreferences(MODE_PRIVATE).edit().putInt(BOGGLE_WORDS_SIZE, wordsFound.size()).commit();
		for (int i = 0; i < wordsFound.size(); ++i) {
			String word = wordsFound.get(i);
			getPreferences(MODE_PRIVATE).edit().putString(BOGGLE_WORDS + i, word).commit();
		}			
		// Save the current puzzle
		String puzzleToSave = "";
		for (int i = 0; i < puzzle.length; ++i) {
			puzzleToSave += puzzle[i];
		}		
		getPreferences(MODE_PRIVATE).edit().putString(BOGGLE_PUZZLE, puzzleToSave).commit();
	}
	
	private void adjustPortraitLayout(int width, int height) {
		// adjust the layout according to the screen resolution
		LinearLayout main = (LinearLayout)findViewById(R.id.linearLayoutRoot);
		puzzleView = new BogglePuzzleView(this);
		main.addView(puzzleView);				
      
        LayoutParams laParams = null;
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.relativeLayout);
        laParams = rl.getLayoutParams();
        laParams.height = height - width;
        rl.setLayoutParams(laParams);
        
        LinearLayout llLog = (LinearLayout)findViewById(R.id.linearLayoutLog);
        laParams = llLog.getLayoutParams();
        laParams.width = (int)(width * GOLDEN_DIVIDE);
        llLog.setLayoutParams(laParams);
        
        LinearLayout llTime = (LinearLayout)findViewById(R.id.linearLayoutTime);
        laParams = llTime.getLayoutParams();
        laParams.width = (int)(width * (1 - GOLDEN_DIVIDE));
        llTime.setLayoutParams(laParams);
	}
	
	private void adjustLandscapeLayout(int width, int height) {
		// adjust the layout according to the screen resolution
		LinearLayout main = (LinearLayout)findViewById(R.id.linearLayoutRoot);
		puzzleView = new BogglePuzzleView(this);
		main.addView(puzzleView);				
      
        LayoutParams laParams = null;
        RelativeLayout rl = (RelativeLayout)findViewById(R.id.relativeLayout);
        laParams = rl.getLayoutParams();
        laParams.width = width - height;
        rl.setLayoutParams(laParams);
        
        LinearLayout llTime = (LinearLayout)findViewById(R.id.linearLayoutTime);
        laParams = llTime.getLayoutParams();
        laParams.height = (int)(height * (1 - GOLDEN_DIVIDE - 0.1));
        llTime.setLayoutParams(laParams);
        
        LinearLayout llButtons = (LinearLayout)findViewById(R.id.linearLayoutButtons);
        laParams = llButtons.getLayoutParams();
        laParams.width = (int)((width - height) * (1 - GOLDEN_DIVIDE));
        llButtons.setLayoutParams(laParams);
        
        LinearLayout llLog = (LinearLayout)findViewById(R.id.linearLayoutLog);
        laParams = llLog.getLayoutParams();
        laParams.height = (int)(height * (GOLDEN_DIVIDE + 0.1));
        llLog.setLayoutParams(laParams);
	}
	
	private List<String> getData() {        
        return wordsFound;
    }

	@Override
	protected void onResume() {
		super.onResume();							
		
		if (game_over) {
			doGameOver();
		} else {
			pauseGame(paused);
		}
		
		// update text views
 		scoreView.setText("score: " + game_score);
 		bestView.setText("best: " + game_best_score);
 		String minute = "" + game_time / 60;
    	String second = "" + game_time % 60;
    	if (second.length() == 1) {
    		second = "0" + second;
    	}
    	timeView.setText(minute + ":" + second);
    	// fill list view
        listView.setAdapter(
		 	new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData())
		);         
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.d(TAG, "onPause");		
		savePreferences();		
		pauseGame(true);
	}
	
	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.boggle_reset_button:
//			resetGame();
//			break;
		// ...
		case R.id.boggle_pause_button:
			pauseGame(!paused);			
			break;
		}
	}
	
	private void doGameOver() {
		game_over = true;		
		textHandler.removeCallbacks(textRunnable);
		colorHandler.removeCallbacks(colorRunnable);
		pauseButton.setEnabled(false);
		puzzleView.invalidate();
	}
	
//	private void resetGame() {
//		
//	}
//	
	private void pauseGame(boolean paused) {
		this.paused = paused;
		if (paused) {
			pauseButton.setText("Resume");
			textHandler.removeCallbacks(textRunnable);
			colorHandler.removeCallbacks(colorRunnable);
			BoggleMusic.pause();
		} else {
			pauseButton.setText("Pause");			
			textHandler.postDelayed(textRunnable, 1000);
			if (game_time <= 20) {
				colorHandler.postDelayed(colorRunnable, 500);
			}
			BoggleMusic.play();
		}
		this.puzzleView.invalidate();
	}
	
	public boolean isGamePaused() {
		return paused;
	}
	
	public boolean isGameOver() {
		return game_over;
	}
	
	public void playClickSound() {
		if (tonePlayer != null) {
			tonePlayer.startTone(ToneGenerator.TONE_DTMF_2, 100);
		}
	}
	
	/** Come up with a new puzzle */
	private char[] getPuzzle(int command) {		
		StringBuffer sf = new StringBuffer();
		char[] new_puzzle = null;
		
		if (command == NEW_GAME) {
			char letters[] = new char[16];			
			
			for (int i = 0; i < 16; ++i) {
				letters[i] = ' ';
			}
			for (int i = 0; i < 16; ++i) {
				char letter = generateLetter();
				letters[i] = letter;
				while (isRepeatedTooMuch(letters, letter)) {
					letter = generateLetter();
					letters[i] = letter;
				}
				sf.append(String.valueOf(letter));
			}
			new_puzzle = sf.toString().toCharArray();
		} else if (command == CONTINUE) {
			String defaultPuzzle = "ABCDEFGHIJKLMNOP";
			String savedPuzzle = getPreferences(MODE_PRIVATE).getString(BOGGLE_PUZZLE, defaultPuzzle);
			new_puzzle = savedPuzzle.toCharArray();
		}		
		
		return new_puzzle;
	}
	
	private boolean isRepeatedTooMuch(char[] letters, char letter) {
		int count = 0;
		for (int i = 0; i < letters.length; ++i) {
			if (letters[i] == letter) {
				++count;
			}
		}
		return count > 3;
	}
	
	/** Generate every letter */
	private char generateLetter() {
		char letter = 'e';
		int l = 0, r = 0;
		int t = (int)(Math.random() * SUM_OF_CHARS);	
		
		int i;
		for (i = 0; i < 26; ++i) {
			r += FREQUENCY[i];
			if (l < t && t < r) {
				letter = (char)(65 + i);
				break;
			}
			l += FREQUENCY[i];
		}		
		assert(i < 26);
		
		return letter;
	}
	
	/** Return the tile at the given coordinates */
	private char getTile(int x, int y) {
		return puzzle[y * 4 + x];
	}

	/** Change the tile at the given coordinates */
//	private void setTile(int x, int y, char value) {
//		puzzle[y * 4 + x] = value;
//	}

	/** Return a string for the tile at the given coordinates */
	protected String getTileString(int x, int y) {
		char v = getTile(x, y);
		return String.valueOf(v);
	}
	
	private String listToString(List<Point> selList) {
		String s = "";		
		for (int i = 0; i < selList.size(); ++i) {
			Point pt = selList.get(i);
			s += getTileString(pt.x, pt.y);
		}		
		return s.toLowerCase();
	}

	protected void updateBoggleStringFromSelection(List<Point> selList) {
		boggleString = listToString(selList);
		Log.d(TAG, "boogleString = " + boggleString);
		// update list view
	}
	
	protected boolean isWordInDictionary(List<Point> selList) {
		String s = listToString(selList);
		if (lookupWordInDictionary(s)) {
			return true;
		}
		return false;
	}
	
	private boolean lookupWordInDictionary(String wordToLookup) {
		String dictName = "" + wordToLookup.charAt(0);
		
		if (!dict.isLoaded(dictName)) {
			dict.load("wordlist_" + dictName + ".mpg", dictName, getAssets());			
		} 
		
		if (wordsFound.contains(wordToLookup)) {
			Toast.makeText(this, "Oops! Repeated!", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		boolean found = dict.lookupWord(wordToLookup);		
		if (found) {
			wordsFound.add(wordToLookup);
			listView.setAdapter(
	        	new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, getData())
	        );			
			int bonus = measureBonus(wordToLookup);
			game_score += bonus;
			if (game_best_score < game_score) {
				game_best_score = game_score;
			}
			scoreView.setText("score: " + game_score);
			bestView.setText("best: " + game_best_score);
			String toastText = "";
			if (bonus <= 1) {
				toastText = "Good! +";
			} else if (bonus > 1 && bonus <= 4) {
				toastText = "Great! +";
			} else {
				toastText = "Excellent! +";
			}
			Toast.makeText(this, toastText + bonus, Toast.LENGTH_SHORT).show();
		}
		
		return found;
	}
	
	private int measureBonus(String wordFound) {
		int bonus  = 0;
		int length = wordFound.length();
		
		switch (length) {
		case 3: bonus = 1; break;
		case 4: bonus = 2; break;
		case 5: bonus = 4; break;
		case 6: bonus = 6; break;
		default: bonus = 10; break;
		}
		
		return bonus;
	}
	
}
