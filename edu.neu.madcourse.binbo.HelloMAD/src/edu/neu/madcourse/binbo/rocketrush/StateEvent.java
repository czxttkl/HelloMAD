package edu.neu.madcourse.binbo.rocketrush;

public class StateEvent extends GameEvent {
	
	public final static int STATE_OVER    = 0;
	public final static int STATE_PLAY    = 1;
	public final static int STATE_PAUSE   = 2;	
	public final static String NO_LIFE = "life is 0, game over";
	public final static String NO_TIME = "time up, game over";
	public String mDescription = "";
	
	public StateEvent(int state, String description) {
		super(EVENT_STATE);
		mWhat = state;
		mDescription = description;
	}

}
