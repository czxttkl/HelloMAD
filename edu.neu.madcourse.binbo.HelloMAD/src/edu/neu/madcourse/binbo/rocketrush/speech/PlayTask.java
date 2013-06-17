package edu.neu.madcourse.binbo.rocketrush.speech;

import java.io.File;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.os.AsyncTask;
import android.os.Environment;

class PlayTask extends AsyncTask<Void, Integer, Void>{ 

	protected int mSampleRate = 16000;
	protected int mChannelConfig = AudioFormat.CHANNEL_OUT_MONO;
	protected int mFormatConfig  = AudioFormat.ENCODING_PCM_16BIT;
	
	protected AudioTrack mTrack = null;
	protected byte[] mBuffer = null;
	protected String mPath = null;
//	protected DataInputStream mDIS = null;
	
	protected OpusManager mOpus = null;
	protected NativeOpus mNativeOpus = null;
	
	public PlayTask(OpusManager opus, String fileIn) {
		mOpus = opus;
		mNativeOpus = opus.getNativeOpus();
		setDecodeFilePath(fileIn);
	}
	
	public void setChannelConfig(int channels) {
		mChannelConfig = channels == 1 ? AudioFormat.CHANNEL_OUT_MONO :
										 AudioFormat.CHANNEL_OUT_STEREO;
	}
	
	public void setSamplerate(int samplerate) {
		mSampleRate = samplerate;
	}
	
	public String setDecodeFilePath(String path) {		
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
            mTrack.play();    
   
            while (!isCancelled()) {    
            	if (mNativeOpus.decode(mBuffer, mBuffer.length) != 0) {
            		cancel(true);
            	}            	            	
                mTrack.write(mBuffer, 0, mBuffer.length);                  
            }     
        } catch (Exception e) {    
            // TODO: handle exception    
        } finally {
        	doRelease();        	  
        }
        
        return null;    
    }    
    
    protected void onCancelled(Void result) {    	
    	mOpus.updateState(0, OpusManager.OPUS_STATE_PLAYING);
    }
        
    protected void onPostExecute(Void result){      
    }    
        
    protected void onPreExecute(){                   
    }    
    
    public boolean doPrepair() {  
    	// get needed parameters for creating a new AudioTrack   
        if (mNativeOpus.createDecoder(mPath) != 0) {
        	return false;
        }
        Integer ret = (Integer) mNativeOpus.getParameter(NativeOpus.PARAM_DECODER_SAMPLERATE);
        mSampleRate = ret.intValue();
        ret = (Integer) mNativeOpus.getParameter(NativeOpus.PARAM_DECODER_CHANNELS);
        mChannelConfig = ret.intValue() == 1 ? AudioFormat.CHANNEL_OUT_MONO : AudioFormat.CHANNEL_OUT_STEREO;
        ret = (Integer) mNativeOpus.getParameter(NativeOpus.PARAM_DECODER_SAMPLEFORMAT);
        mFormatConfig = ret.intValue() == NativeOpus.OPUS_SAMPLE_FMT_U8 ?
        		AudioFormat.ENCODING_PCM_8BIT : AudioFormat.ENCODING_PCM_16BIT;
        // create a new AudioTrack
        int bufferSize = AudioTrack.getMinBufferSize(mSampleRate, mChannelConfig, mFormatConfig); 
        if (bufferSize <= 0) {
        	mNativeOpus.destroyDecoder();
        	return false;
        }
        
        mBuffer = new byte[bufferSize];        
        mTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 
        						mSampleRate, 
			            	    mChannelConfig, 
			                    mFormatConfig, 
			            	    bufferSize, 
			            	    AudioTrack.MODE_STREAM);
        if (mTrack == null) {
        	mNativeOpus.destroyDecoder();
        	return false;
        }
        
        if (mChannelConfig == AudioFormat.CHANNEL_OUT_MONO) {
        	mChannelConfig = 1;
        } else if (mChannelConfig == AudioFormat.CHANNEL_OUT_STEREO) {
        	mChannelConfig = 2;
        }        
        
        return true;
    }
    
    protected void doRelease() {
    	if (mTrack != null) {
    		mTrack.stop();
    		mTrack.release();
    		mTrack = null;
    	}
    	mNativeOpus.destroyDecoder();
    }
        
}    
