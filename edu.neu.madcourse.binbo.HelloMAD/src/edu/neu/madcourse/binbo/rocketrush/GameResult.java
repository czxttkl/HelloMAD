package edu.neu.madcourse.binbo.rocketrush;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


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
	
	public String getDate() {
		SimpleDateFormat dt = new SimpleDateFormat("MMM dd, yyyy"); 
		Date date;
		try {
			date = dt.parse(mDate);
			SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
			return dt1.format(date).toString();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return mDate;
		} 
	}
}