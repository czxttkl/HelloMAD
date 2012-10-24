package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PBInvite extends Activity implements OnClickListener  {
	private ListView mLVPlayerList;
	private PlayerInfoAcquirer mAcquirer = null;
	private ArrayList<PBPlayerInfo> mPlayerInfoList;
	private View mBtnBack;
	private String mAccName;
	private static final String ACCOUNT_NAME = "account_name";
	
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {
    	private static final int UPDATE_PLAYERS_INFO  = 0;   
        private static final int UPDATE_PLAYERS_ERROR = 1;
 
        @SuppressWarnings("unchecked")
		public void handleMessage(Message msg) {
        	if (mAcquirer != null) {
    			mAcquirer.end();
    			mAcquirer = null;
    		}
        	switch (msg.arg1) { 
	            case UPDATE_PLAYERS_INFO:                 
	            	mPlayerInfoList = (ArrayList<PBPlayerInfo>)msg.obj;
	            	updatePlayerList();
	                break;  
	            case UPDATE_PLAYERS_ERROR:  
	            	Toast.makeText(getApplicationContext(), 
		                       "Server connection lost during downloading data.",
		                       Toast.LENGTH_LONG).show();
	                break;
	            default:
	            	break;
            }
            
        } 
    };
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_invite);
		
		Intent intent = getIntent();
		this.mAccName = intent.getStringExtra(ACCOUNT_NAME);
		
		this.mLVPlayerList = (ListView) this.findViewById(R.id.pbinvite_user_list);

		if (mAcquirer == null) {
			mAcquirer = new PlayerInfoAcquirer(mHandler, true);
			mAcquirer.start();
		}
		
		// Set up click listeners for all the buttons
		this.mBtnBack = this.findViewById(R.id.pbinvite_back_button);
		this.mBtnBack.setOnClickListener(this);
	}
	
	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
			case R.id.pbinvite_back_button:
				i = new Intent(this, PBMain.class);
				i.putExtra(ACCOUNT_NAME, this.mAccName);
				finish();
				startActivity(i);
				break;
		}
	}
	
	public void updatePlayerList() {
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < this.mPlayerInfoList.size(); i++) {
			PBPlayerInfo info = this.mPlayerInfoList.get(i);
			if (!this.mAccName.equals(info.getName())) {
				HashMap<String, String> map = new HashMap<String, String>();  
	            map.put("ItemName", info.getName().toString());
	            map.put("ItemStatus", info.getStatus().toString());
	            listItem.add(map);
			}
		}

        SimpleAdapter listItemAdapter = new SimpleAdapter(this, listItem,
            R.layout.pb_invite_list_item,  
            new String[] {"ItemName","ItemStatus"},
            new int[] {R.id.pbinvite_name, R.id.pbinvite_status}
        );

        this.mLVPlayerList.setAdapter(listItemAdapter);  
	}
}
