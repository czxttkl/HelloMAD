package edu.neu.madcourse.binbo.rocketrush;

public class GameCtrl {
	public final static int MOVE_NONE  = 0;
	public final static int MOVE_LEFT  = 1;
	public final static int MOVE_RIGHT = 2;
	public final static int MOVE_UP    = 3;
	public final static int MOVE_DOWN  = 4;
	public final static int MOVE_HORI  = 5;
	public final static int MOVE_VERT  = 6;
	
	protected int mCommand = MOVE_NONE;
	
	public GameCtrl(int command) {
		setCommand(command);
	}
	
	public void setCommand(int command) {
		mCommand = command;
	}
	
	public int getCommand() { return mCommand; }
}
