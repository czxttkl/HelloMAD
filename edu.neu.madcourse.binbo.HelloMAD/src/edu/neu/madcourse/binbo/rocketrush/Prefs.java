package edu.neu.madcourse.binbo.rocketrush;

import edu.neu.madcourse.binbo.R;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Prefs extends PreferenceActivity {
	// Option names and default values
	private static final String OPT_SOUND = "sound";
	private static final boolean OPT_SOUND_DEF = true;
	private static final String OPT_VIB = "vibration";
	private static final boolean OPT_VIB_DEF = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.rocket_rush_settings);
	}

	/** Get the current value of the music option */
	public static boolean getMusic(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_SOUND, OPT_SOUND_DEF);
	}

	/** Get the current value of the hints option */
	public static boolean getHints(Context context) {
		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(OPT_VIB, OPT_VIB_DEF);
	}
}
