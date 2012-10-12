package edu.neu.madcourse.binbo;

import android.content.Context;
import android.media.MediaPlayer;

public class BoggleMusic {
	private static MediaPlayer mp = null;
	private static int seekPos = 0;
	/** Stop old song and start new one */

	public static void create(Context context, int resource) {
		stop(context);

		// Start music only if not disabled in preferences
		if (BogglePrefs.getMusic(context)) {
			mp = MediaPlayer.create(context, resource);
			if (mp != null) {
				mp.setLooping(true);
				mp.setVolume(0.2f, 0.2f);				
			}
		}
	}
	
	public static void play() {
		if (mp != null) {
			if (!mp.isPlaying()) {
				mp.start();
			}
			mp.seekTo(seekPos);
		}
	}
	
	public static void reset() {
		if (mp != null) {
			seekPos = 0;
		}
	}
	
	public static void pause() {
		if (mp != null) {
			if (mp.isPlaying()) {
				mp.pause();
				seekPos = mp.getCurrentPosition();
			}			
		}
	}

	/** Stop the music */
	public static void stop(Context context) {
		if (mp != null) {
			mp.stop();
			mp.release();
			mp = null;
		}
	}
}
