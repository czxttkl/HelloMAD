package edu.neu.madcourse.binbo.rocketrush;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.Handler;

public class BaseThread extends Thread {
	
	protected boolean mRun = false;	
	protected Handler mHandler = null;	
	protected ConcurrentLinkedQueue<GameEvent> mEventQueue;
	
	protected BaseThread() {
		mEventQueue = new ConcurrentLinkedQueue<GameEvent>();
	}
	
	protected BaseThread(Handler handler) {
		mEventQueue = new ConcurrentLinkedQueue<GameEvent>();
		setHandler(handler);
	}
	
	public void setHandler(Handler handler) {
		mHandler = handler;
	}
	
	public Handler getHandler() {
		return mHandler;
	}
	
	// don't write "synchronized void end()", it may block the drawer thread
	// since both start and end are invoked by ui thread, it's fine just like this.
	public void end() { 
		mRun = false;		
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void start() {
		mRun = true;
		super.start();
	}
	
	public void notifyEvent(GameEvent e) {
		mEventQueue.add(e);		
	}
	
	protected GameEvent pollEvent() {
		return mEventQueue.poll();
	}
	
	protected GameEvent peekEvent() {
		return mEventQueue.peek();
	}
	
	protected void handleEvent(GameEvent e) {
		// do nothing here, subclass can override 
		// this method to deal with specified events
		return;
	}

}
