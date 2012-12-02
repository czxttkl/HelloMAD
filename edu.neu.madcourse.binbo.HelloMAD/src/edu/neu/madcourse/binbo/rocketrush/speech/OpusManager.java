package edu.neu.madcourse.binbo.rocketrush.speech;

import java.io.File;

import android.content.Context;
import android.widget.Toast;

public class OpusManager {
	// opus states
	protected int mState = OPUS_STATE_NONE;
	protected static final int OPUS_STATE_NONE       = 0;
	protected static final int OPUS_STATE_PLAYING    = 1;
	protected static final int OPUS_STATE_RECORDING  = 1 << 1;
	protected static final int OPUS_STATE_CONVERTING = 1 << 2;
	// native opus	
	protected NativeOpus mOpus = null;//new NativeOpus();
	// threads
	protected PlayTask    mPlayer    = null;
	protected RecordTask  mRecorder  = null;
	// listener
	protected OnStateUpdateListener mStateUpdateListener = null;
	// Context
	protected Context mContext = null;
	
	public OpusManager(Context context) {
		mContext = context;
	}
	
	public void startRecording(String fileOut, int bitrate, int samplerate, int channels) {
		if ((getState() & OPUS_STATE_RECORDING) == 0) {
			updateState(OPUS_STATE_RECORDING, 0);			
			mRecorder = new RecordTask(this, fileOut, bitrate, samplerate, channels);				
//			mRecorder.setApplication(mApplication);	
			if (mRecorder.doPrepair()) {
				mRecorder.execute();		
			} else { // failure
				Toast.makeText(mContext, "Can't record voice!\n", Toast.LENGTH_SHORT).show();
				updateState(0, OPUS_STATE_RECORDING);
				mRecorder = null;
			}
		}
	}
	
	public void stopRecording() {			
		mRecorder.cancel(true);
		mRecorder = null;
	}
	
	public void startPlaying(String fileIn) {
		if ((getState() & OPUS_STATE_PLAYING) == 0) {
			if (!(new File(fileIn).exists())) {
				Toast.makeText(mContext, fileIn + " does not exist!", Toast.LENGTH_SHORT).show();
				return;
			}
			updateState(OPUS_STATE_PLAYING, 0);
			mPlayer = new PlayTask(this, fileIn);
			if (mPlayer.doPrepair()) {
				mPlayer.execute();	
			} else { // failure
				Toast.makeText(mContext, "Can't play the bad file!\n", Toast.LENGTH_SHORT).show();
				updateState(0, OPUS_STATE_PLAYING);
				mPlayer = null;
			}				
		} 
	}
	
	public void stopPlaying() {
		mPlayer.cancel(true);
		mPlayer = null;			
	}
	
	protected void updateState(int stateAdd, int stateRemove) {
		setState(stateAdd, stateRemove);
		if (mStateUpdateListener != null) {
			mStateUpdateListener.onStateUpdate(getState());
		}
	}
	
	protected NativeOpus getNativeOpus() {		
		return mOpus;
	}
	
	synchronized public int getState() {
		return mState;
	}
	
	synchronized protected void setState(int stateAdd, int stateRemove) {
		mState &= ~stateRemove;
		mState |= stateAdd;
	}
	
	protected void onPause() {
		if (getState() == OPUS_STATE_PLAYING) {
			mPlayer.cancel(true);
		}
	}
	
	protected void onResume() {
	}
	
	public interface OnStateUpdateListener {
		void onStateUpdate(int state);
	}
}
