package edu.neu.madcourse.binbo.rocketrush;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.WindowManager;
import edu.neu.madcourse.binbo.R;

public class Prefs extends PreferenceActivity implements Preference.OnPreferenceChangeListener {
	// Option names and default values
	private static final String OPT_SOUND = "music";
	private static boolean OPT_SOUND_DEF = true;
	private static final String OPT_VIB = "vibrate";
	private static boolean OPT_VIB_DEF = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		addPreferencesFromResource(R.xml.rocket_rush_settings);
		//get the specified preferences using the key declared in preferences.xml
		final CheckBoxPreference musicPref   = (CheckBoxPreference) findPreference(OPT_SOUND);
		final CheckBoxPreference vibratePref = (CheckBoxPreference) findPreference(OPT_VIB);		
		musicPref.setOnPreferenceChangeListener(this); 		
		vibratePref.setOnPreferenceChangeListener(this); 
		
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN
		); 
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

	public boolean onPreferenceChange(Preference preference, Object newValue) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
		editor.putBoolean(preference.getKey(), (Boolean) newValue);
		editor.commit();
        return true;
	}
}
