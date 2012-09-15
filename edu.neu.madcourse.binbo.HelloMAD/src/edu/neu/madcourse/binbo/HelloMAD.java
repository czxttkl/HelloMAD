package edu.neu.madcourse.binbo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import edu.neu.mobileClass.*;

public class HelloMAD extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Set up click listeners for all the buttons
        View teamButton = this.findViewById(R.id.team_button); 
        teamButton.setOnClickListener(this);
        View gameButton = this.findViewById(R.id.game_button); 
        gameButton.setOnClickListener(this);
        View errorButton = this.findViewById(R.id.error_button); 
        errorButton.setOnClickListener(this);
        
        //PhoneCheckAPI.doAuthorization(this);
    }
    
    public void onClick(View v) { 
    	Intent i = null;
    	
    	switch (v.getId()) {
    	case R.id.team_button:
    		i = new Intent(this, TeamMembers.class); 
    		startActivity(i);
    		break;
    	case R.id.game_button: 
    		i = new Intent(this, Sudoku.class);
    		startActivity(i);
    		break;
    	case R.id.error_button: 
    		createError();
    		break;
    		// More buttons go here (if any) ...
    	} 
    }
    
    private void createError() {
    	int n = 0;
    	n = 1 / 0;
    }
}