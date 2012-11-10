package edu.neu.madcourse.binbo.rocketrush;

import android.graphics.Canvas;
import android.os.Handler;

public class RushMode extends BaseMode implements IDrawer {

	public RushMode(GameEngine engine, Handler handler) {
		setEngine(engine);
		setHandler(handler);
	}
	
	public int doDraw(Canvas c) {
		return 0;
	}
	
	@Override
	public void run() {			

		while (mRun) {
			handleEvent(mEventQueue.poll());			
			
			// update the game context with the engine
			// ...
			
			try {
				sleep(30); // draw 33 times per second, it's more than enough
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	} // end of run
}
