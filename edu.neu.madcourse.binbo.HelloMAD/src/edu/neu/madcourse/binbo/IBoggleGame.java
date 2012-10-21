package edu.neu.madcourse.binbo;

import java.util.List;

import android.graphics.Point;

public interface IBoggleGame {
	public boolean isGameOver();
	public boolean isGamePaused();		
	public boolean lookUpWord(String word);
}
