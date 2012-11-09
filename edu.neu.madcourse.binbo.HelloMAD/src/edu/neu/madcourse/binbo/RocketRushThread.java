package edu.neu.madcourse.binbo;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.os.Handler;
import android.view.SurfaceHolder;

public class RocketRushThread extends Thread {
	
	protected SurfaceHolder mHolder = null;
	protected Handler mHandler = null;	
	protected ConcurrentLinkedQueue<GameEvent> mEventQueue;
	
	protected boolean mRun = false;
	protected GameEngine mEngine = null;

	public RocketRushThread(SurfaceHolder holder, Handler handler) {
		mHolder  = holder;
		mHandler = handler;
		mEventQueue = new ConcurrentLinkedQueue<GameEvent>();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		while (mRun) {
		
		}
	}
	
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
	public synchronized void start() {
		mRun = true;
		super.start();
	}

}
