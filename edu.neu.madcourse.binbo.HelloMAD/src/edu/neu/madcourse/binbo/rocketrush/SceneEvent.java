package edu.neu.madcourse.binbo.rocketrush;

import android.os.Message;

public class SceneEvent extends GameEvent {

	public final static int SCENE_COLLIDE = 0;
	public final static int SCENE_LEVELUP = 1;
	public Message mMsg = null;
	
	public SceneEvent(int eventType, Message msg) {
		super(EVENT_SCENE);
		mWhat = eventType;
		mMsg  = msg;
	}


}
