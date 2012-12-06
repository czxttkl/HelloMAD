package edu.neu.madcourse.binbo.rocketrush;

import java.util.List;

import edu.neu.madcourse.binbo.R;
import edu.neu.madcourse.binbo.boggle.BogglePuzzleView;
import edu.neu.madcourse.binbo.persistentboggle.PBPlayerInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class GameRankActivity extends FragmentActivity implements OnClickListener {
	
	protected TableLayout mTable = null;
	protected Button mBackButton = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.rocket_rush_rank);
        // get views and set listeners
		setupViews();
		// adjust layouts according to the screen resolution
		adjustLayout();        
	}
	
	private void setupViews() {
		mTable = (TableLayout) findViewById(R.id.tableLayoutRocketRank);
		// Set up click listeners for all the buttons
		mBackButton = (Button) findViewById(R.id.rocketRankButtonBack);		
		mBackButton.setOnClickListener(this);
	}
	
	private void adjustLayout() {
		getWindow().setFlags(
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_FULLSCREEN
		); 
		// get screen resolution
		DisplayMetrics dm = new DisplayMetrics();  
        Display display = getWindowManager().getDefaultDisplay(); 		
        display.getMetrics(dm);        
        // adjust the layout according to the screen resolution
// 		LinearLayout main = (LinearLayout)findViewById(R.id.rocketrankRoot);				   
		LayoutParams laParams = null;
		ScrollView scrollView = (ScrollView)findViewById(R.id.rocketrankScrollView);
		laParams = scrollView.getLayoutParams();
		laParams.height = (int) (dm.heightPixels * 0.7f);
		scrollView.setLayoutParams(laParams);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rocketRankButtonBack:
			finish();
			break;
		}
	}

	@Override
	protected void onResume() {
		for (int i = 0; i < 50; ++i) {    		
	    	TableRow tablerow = new TableRow(getApplicationContext());  
            tablerow.setBackgroundColor(Color.rgb(255, 255, 255));
                              
                TextView textViewRank = new TextView(getApplicationContext());
                textViewRank.setTextSize(18);
                textViewRank.setTextColor(getApplicationContext().getResources().getColor(R.color.rr_grey));
                textViewRank.setPadding(10, 10, 10, 10);
                textViewRank.setWidth(30);
                textViewRank.setText(String.valueOf(i + 1));           
                tablerow.addView(textViewRank);
                
//                TextView textViewScore = new TextView(getApplicationContext());  
//                textViewName.setText(player.getName());                
//                tablerow.addView(textViewName);
//                
//                TextView textViewTime = new TextView(getApplicationContext());  
//                textViewBest.setText(String.valueOf(player.getBestScore()));                
//                tablerow.addView(textViewBest);
              
            mTable.addView(tablerow, new TableLayout.LayoutParams(  
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
    	}         
		super.onResume();
	}
}
