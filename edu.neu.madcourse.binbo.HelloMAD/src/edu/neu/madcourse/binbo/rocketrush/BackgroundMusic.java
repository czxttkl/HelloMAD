package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.media.MediaPlayer;
import android.preference.PreferenceManager;

public class BackgroundMusic {
	private static MediaPlayer mPlayer = null;
	private static int mSeekPos = 0;
	private static BackgroundMusic sBackgroundMusic = null;
	private static boolean sInitialized = false;
	/** Stop old song and start new one */
	
	private BackgroundMusic() {}
	
	public static BackgroundMusic getInstance() {
		if (!sInitialized) {
			sBackgroundMusic = new BackgroundMusic();
			sInitialized = true;
		}
		return sBackgroundMusic;
	}

	public void create(Context context, int resource) {
		stop();
		// Start music only if not disabled in preferences
//		if (Prefs.getMusic(context)) {
			mPlayer = MediaPlayer.create(context, resource);
			if (mPlayer != null) {
				mPlayer.setLooping(true);
				float volume = 
					PreferenceManager.getDefaultSharedPreferences(context).getInt(Setting.SND_KEY, 40) / 100f;
				mPlayer.setVolume(volume, volume);				
			}
//		}
		mSeekPos = 0;
	}
	
	public void setLooping(boolean looping) {
		if (mPlayer != null) {
			mPlayer.setLooping(looping);
		}
	}
	
	public void setVolume(float leftVolume, float rightVolume) {
		if (mPlayer != null) {
			mPlayer.setVolume(leftVolume, rightVolume);				
		}
	}
	
	public void play() {
		if (mPlayer != null) {
			if (!mPlayer.isPlaying()) {
				mPlayer.seekTo(mSeekPos);
				mPlayer.start();						
			}			
		}
	}
	
	public void reset() {
		mSeekPos = 0;		
	}
	
	public void pause() {
		if (mPlayer != null) {
			if (mPlayer.isPlaying()) {
				mPlayer.pause();
				mSeekPos = mPlayer.getCurrentPosition();
			}			
		}
	}

	/** Stop the music */
	public void stop() {
		if (mPlayer != null) {
			mPlayer.stop();
			mPlayer.release();
			mPlayer = null;
			System.gc();
		}
	}
}