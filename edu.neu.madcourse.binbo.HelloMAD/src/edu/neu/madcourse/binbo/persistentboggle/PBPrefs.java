package edu.neu.madcourse.binbo.persistentboggle;

import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import edu.neu.madcourse.binbo.R;

public class PBPrefs extends PreferenceActivity {
	// Option names and default values
		private static final String OPT_SOUND = "sound";
		private static final boolean OPT_SOUND_DEF = true;
		private static final String OPT_VIBRATE = "vibrate";
		private static final boolean OPT_VIBRATE_DEF = true;

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.pb_options);
		}

		/** Get the current value of the music option */

		public static boolean getSoundPref(Context context) {
			return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean(OPT_SOUND, OPT_SOUND_DEF);
		}

		/** Get the current value of the hints option */

		public static boolean getVibratePref(Context context) {
			return PreferenceManager.getDefaultSharedPreferences(context)
					.getBoolean(OPT_VIBRATE, OPT_VIBRATE_DEF);
		}
}
