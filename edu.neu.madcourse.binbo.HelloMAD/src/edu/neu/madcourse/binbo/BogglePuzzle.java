package edu.neu.madcourse.binbo;

import org.json.JSONException;
import org.json.JSONObject;

// There is little difference between the word lists I used for persistent boggle
// and the normal boggle, so I haven't updated the statistics below.
// However, this time I have not deleted any words over 16 letters.

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

public class BogglePuzzle {			
	protected char[] mPuzzle = null;
	protected int mSize = 4;
	protected IBoggleGame mGame = null;
	
	private final int FREQUENCY[] = {
		326395, 73910, 169177, 129257, 437303, 46188, 98557, 104164, 
		355476, 6416, 33829, 215219, 119469, 285562, 280921, 127706, 
		6784, 280998, 326647, 262874, 146434, 37786, 27811, 11837, 74044, 17531
	};
	private final int SUM_OF_CHARS = 4002295;	
	
	public BogglePuzzle(IBoggleGame game, int size) {
		assert(game != null);		
		mGame = game;
		makePuzzle(size);
	}
	
	public BogglePuzzle(IBoggleGame game, char[] puzzle) {
		mGame = game;
		mPuzzle = puzzle;
		mSize = (int)Math.sqrt(puzzle.length);
	}
	
	public BogglePuzzle(IBoggleGame game, String jsonString) {
		mGame = game;
		fromJSONString(jsonString);
	}
	
	public void makePuzzle(int size) {	
		mSize = size;
		
		StringBuffer sf = new StringBuffer();		
		int count = mSize * mSize;		
		char letters[] = new char[count];			
		
		for (int i = 0; i < count; ++i) {
			letters[i] = ' ';
		}
		for (int i = 0; i < count; ++i) {
			char letter = makeLetter();
			letters[i] = letter;
			while (isTooMuchRepeated(letters, letter)) {
				letter = makeLetter(); // try to make another letter
				letters[i] = letter;
			}
			sf.append(String.valueOf(letter));
		}

		mPuzzle = sf.toString().toCharArray();
	}
	
	public char[] getPuzzle() {
		return mPuzzle;
	}
	
	public void rotatePuzzle() {
		char[] puzzle = new char[mSize * mSize];  	
		
		int k = 0;
		for (int i = 0; i < mSize; ++i) {
			for (int j = mSize - 1; j >= 0; --j) {
				puzzle[mSize * j + i] = mPuzzle[k++];
			}
		}
		
		mPuzzle = puzzle;		
	}
	
	public int getPuzzleSize() {
		return mSize;
	}
	
	public String getTileString(int x, int y) {
		char c = mPuzzle[mSize * y + x];
		return String.valueOf(c);
	}
	
	protected void fromJSONString(String jsonString) {		
		try {
			JSONObject obj = new JSONObject(jsonString);
			mSize   = obj.getInt("size");
			mPuzzle = obj.getString("boggle_puzzle").toCharArray();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}						
	}
	
	public String toJSONString() {
		JSONObject obj = new JSONObject();		

        try {
			obj.put("boggle_puzzle", String.valueOf(mPuzzle));
			obj.put("size", mSize);	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
                
        return obj.toString();
	}

	protected boolean isTooMuchRepeated(char[] letters, char letter) {
		int count = 0;
		
		for (int i = 0; i < letters.length; ++i) {
			if (letters[i] == letter) {
				++count;
			}
		}
		if (count > 3) {
			return true;
		} else if (count == 3) {
			int occurs[] = new int[26];
			for (int i = 0; i < 26; ++i) {
				occurs[i] = 0;
			}
			// get the occurrence of each letter
			for (int i = 0; i < letters.length; ++i) {
				int k = (int)letters[i] - 65;
				if (k >= 0) {
					occurs[k]++;
				}				 
			}		
			// check whether some other letter already occurred 3 times,
			// if so, the repeated letters are too much.
			for (int i = 0; i < 26; ++i) {
				if (occurs[i] > 2) {
					if ((char)(i + 65) == letter) // skip the current letter 
						continue;
					return true; // more than one letter occurred 3 times
				}
			}			
		}
		
		return false;
	}
	
	/** Make every letter */
	protected char makeLetter() {
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
}
