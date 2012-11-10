package edu.neu.madcourse.binbo.rocketrush;

/**
 * Base class for any external event passed to the RocketRushThread. This can
 * include user input, system events, network input, etc.
 */

public class GameEvent {
	protected long mEventTime;
	
    public GameEvent() {        
    	mEventTime = System.currentTimeMillis();        
    }
}
