package edu.neu.madcourse.binbo.rocketrush;

import java.io.Serializable;


public class GameResult implements Comparable<GameResult>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4204839233543315909L;
	public int 	  mScore = 0;
	public String mDate  = "";
	
	public GameResult(int score, String date) {
		mScore = score;
		mDate  = date;
	}
	
	public int compareTo(GameResult another) {
		return mScore - another.mScore;
	}
}