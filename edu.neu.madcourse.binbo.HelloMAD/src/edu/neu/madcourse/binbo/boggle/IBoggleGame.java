package edu.neu.madcourse.binbo.boggle;


public interface IBoggleGame {
	public boolean isGameOver();
	public boolean isGamePaused();		
	public boolean lookUpWord(String word);
	public void updateGameViews();
}
