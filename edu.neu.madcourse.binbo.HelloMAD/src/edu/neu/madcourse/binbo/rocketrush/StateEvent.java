package edu.neu.madcourse.binbo.rocketrush;

public class StateEvent extends GameEvent {
	
	public final static int STATE_OVER    = 0;
	public final static int STATE_PLAY    = 1;
	public final static int STATE_PAUSE   = 2;	
	public String mDescription = "";
	
	public StateEvent(int state, String description) {
		super(EVENT_STATE);
		mWhat = state;
		mDescription = description;
	}

}
