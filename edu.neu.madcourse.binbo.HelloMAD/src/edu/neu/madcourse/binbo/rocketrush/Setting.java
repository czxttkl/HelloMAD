package edu.neu.madcourse.binbo.rocketrush;

import edu.neu.madcourse.binbo.R;
import android.app.Activity;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class Setting extends Activity implements OnSeekBarChangeListener {
	public final static String SND_KEY = "sound volume";
	public final static String SFX_KEY = "sfx volume";
	
	protected SeekBar mSndSeek = null;
	protected SeekBar mSfxSeek = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting);

		setupViews();
		adjustLayout();
	}
	
	private void setupViews() {
		mSndSeek = (SeekBar) findViewById(R.id.volume_seek);
		mSfxSeek = (SeekBar) findViewById(R.id.sfx_seek);
		mSndSeek.setOnSeekBarChangeListener(this);
		mSfxSeek.setOnSeekBarChangeListener(this);
			
		mSndSeek.setProgress(
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(SND_KEY, 40)
		);
		mSfxSeek.setProgress(
			PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getInt(SFX_KEY, 40)
		);
	}
	
	private void adjustLayout() {
		DisplayMetrics dm = new DisplayMetrics();  
        Display display = getWindowManager().getDefaultDisplay(); 		
        display.getMetrics(dm);
        
        LayoutParams laParams = null;
		laParams = mSndSeek.getLayoutParams();
		laParams.width = (int) (dm.widthPixels * 0.5f);
		mSndSeek.setLayoutParams(laParams);
		laParams = mSfxSeek.getLayoutParams();
		laParams.width = (int) (dm.widthPixels * 0.5f);
		mSfxSeek.setLayoutParams(laParams);
//		getWindow().setLayout((int)(dm.widthPixels * 0.9f), (int)(dm.heightPixels * 0.9f));
	}

	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();		
		
		if (seekBar == mSndSeek) {
			editor.putInt(SND_KEY, progress);
		} else if (seekBar == mSfxSeek) {
			editor.putInt(SFX_KEY, progress);
		}
		
		editor.commit();
	}

	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

}
