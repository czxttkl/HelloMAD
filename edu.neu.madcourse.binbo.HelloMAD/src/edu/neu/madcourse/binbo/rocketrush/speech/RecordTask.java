package edu.neu.madcourse.binbo.rocketrush.speech;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

// MediaCoder

class RecordTask extends AsyncTask<Void, Integer, Void>{ 
	
	protected static final String TAG = "Opus";
	protected int mSampleRate = 16000;	
	protected int mChannelConfig = AudioFormat.CHANNEL_IN_MONO;
	protected int mFormatConfig  = AudioFormat.ENCODING_PCM_16BIT;
	protected int mBitrateBPS    = 12000;
	protected int mApplication   = NativeOpus.OPUS_APPLICATION_AUDIO;
	
	protected AudioRecord mRecord = null;
	protected byte[] mBuffer = null;
	protected String mPath = "path for opus file";
	protected DataOutputStream mDOS = null;
	
	protected OpusManager mOpus = null;
	protected NativeOpus mNativeOpus = null;
	
	public RecordTask(OpusManager opus, String fileOut, int bitrate, int samplerate, int channels) {
		mOpus = opus;
		mNativeOpus = opus.getNativeOpus();
		setRecordFilePath(fileOut);
		setBitrate(bitrate);
		setSamplerate(samplerate);
		setChannelConfig(channels);
	}
	
	public void setBitrate(int bitrate) {
		mBitrateBPS = bitrate;
	}
	
	public void setChannelConfig(int channels) {
		mChannelConfig = channels == 1 ? AudioFormat.CHANNEL_IN_MONO :
										 AudioFormat.CHANNEL_IN_STEREO;
	}
	
	public void setSamplerate(int samplerate) {
		mSampleRate = samplerate;
	}
	
	public void setApplication(int application) {
		switch (application) {
		case 0:
			mApplication = NativeOpus.OPUS_APPLICATION_VOIP;
			break;
		case 1:
			mApplication = NativeOpus.OPUS_APPLICATION_AUDIO;
			break;
		case 2:
			mApplication = NativeOpus.OPUS_APPLICATION_RESTRICTED_LOWDELAY;
			break;
		}
	}
	
	public String setRecordFilePath(String path) {		
		if (path == null) { // make a default record file path
			File sdCard = Environment.getExternalStorageDirectory();
			mPath = sdCard.getAbsolutePath() + "/audioTest";
	        File dir = new File(mPath);
	        dir.mkdirs();
	        mPath += "/testAudio.opus";	        
		} else {
			mPath = path;
		}		
		
		return mPath;
	}
	
    @Override   
    protected Void doInBackground(Void... arg0) {     	    	    	
    	
        try {    
            // start recording    
            mRecord.startRecording();    
     
            while (!isCancelled()) {      
                int read = mRecord.read(mBuffer, 0, mBuffer.length);            
                if (read < mBuffer.length) {
                	Log.d(TAG, "Not enough data to fill the demanded frame size.");
                } else {
                	mNativeOpus.encode(mBuffer, read);
                }
            }                         
        } catch (Exception e) {
        	e.printStackTrace();    
        } finally {
        	doRelease();
        }
        
        return null;    
    }    
    
    protected void onCancelled(Void result) {   
    	mOpus.updateState(0, OpusManager.OPUS_STATE_RECORDING);
    }
         
    protected void onProgressUpdate(Integer...progress) {    
  
    }    
        
    protected void onPostExecute(Void result) {    
 
    }    
        
    protected void onPreExecute(){       
         
    }    
        
    protected boolean doPrepair() {

        int bufferSize = AudioRecord.getMinBufferSize(mSampleRate, mChannelConfig, mFormatConfig);    
        // initialize audio recorder  
        mRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 
        						  mSampleRate, 
        						  mChannelConfig, 
        						  mFormatConfig, 
        						  bufferSize);
        
        if (mRecord == null) {
        	return false;
        }
        
        if (mChannelConfig == AudioFormat.CHANNEL_IN_MONO) {
        	mChannelConfig = 1;
        } else if (mChannelConfig == AudioFormat.CHANNEL_IN_STEREO) {
        	mChannelConfig = 2;
        }
        
        // 320 = 16000 / 50 - the frame size that audio encoder required.              
        if (mNativeOpus.createEncoder(
        		mBitrateBPS, mSampleRate, mChannelConfig, mApplication, mPath) != 0) {
        	return false;
        }
        
        // allocate buffer size to hold recorded raw PCM
        int bufSize = mSampleRate / 50 * mChannelConfig * 2;        
        mBuffer = new byte[bufSize]; // total frame size during 20ms 
        
        return true;
    }
    
    protected void doRelease() {
    	if (mRecord != null) {
    		mRecord.stop();
    		mRecord.release();
    		mRecord = null;
    	}
    	mNativeOpus.destroyEncoder();
    }
}
