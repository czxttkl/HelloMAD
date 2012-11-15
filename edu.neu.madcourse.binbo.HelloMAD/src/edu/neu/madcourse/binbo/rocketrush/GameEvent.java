package edu.neu.madcourse.binbo.rocketrush;

public class GameEvent {
	public static final int EVENT_NORMAL  = 0;
	public static final int EVENT_CONTROL = 1;	
	protected int  mEventType;
	public static final int SENSOR_ACCELEROMETER = 1;
	protected int  mWhat = 0;
	protected long mEventTime;	
	
    public GameEvent(int eventType) {   
    	setEventType(eventType);
    	mEventTime = System.currentTimeMillis();        
    }
    
    public void setEventType(int eventType) {
    	mEventType = eventType;
    }
    
    public int getEventType() {
    	return mEventType;
    }
    
    public long getEventTime() {
    	return mEventTime;
    }
}
