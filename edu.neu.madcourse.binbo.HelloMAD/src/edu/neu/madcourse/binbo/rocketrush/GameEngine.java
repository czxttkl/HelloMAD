package edu.neu.madcourse.binbo.rocketrush;

public class GameEngine {
	// game states, only one state is available at the exactly time
	protected int mState = STATE_STOP;
	public static final int STATE_STOP    = 0;
	public static final int STATE_PREPAIR = 1;
    public static final int STATE_PLAY    = 2;
    public static final int STATE_PAUSE   = 3;
    public static final int STATE_WIN     = 4;
    public static final int STATE_LOSE    = 5;
    
    public GameEngine() {
    	
    } 
    
    
}
