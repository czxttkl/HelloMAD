package edu.neu.madcourse.binbo.rocketrush.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.rocketrush.GameMode;
import edu.neu.madcourse.binbo.rocketrush.RocketRushActivity;

public class RushModeDialogFragment extends DialogFragment {
	protected RocketRushActivity mActivity = null;
	protected GameMode mGameMode = null;
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mActivity = (RocketRushActivity) getActivity();		
		mGameMode = mActivity.getCurrentGameMode();
		mGameMode.pause();
	    // Use the Builder class for convenient dialog construction
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setItems(R.array.rush_mode_dialog_item, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int which) {		    			    	
		    	// The 'which' argument contains the index position
		    	// of the selected item
		    	switch (which) {
		    	case 0: // restart
		    		mGameMode.restart();
		    		break;		    		
//			    case 1: // restart
//			    	Intent i = new Intent(mActivity, Setting.class);
//			    	startActivity(i);
//			    	break;
		    	case 1: // exit		    		
		    		mActivity.switchGameMode(0); // waiting mode
		    		break;
		    	}
		    }
	    });
	    return builder.create();
	}
	
	@Override
	public void onDestroy() {
		mGameMode.resume();
		super.onDestroy();
	}

}
