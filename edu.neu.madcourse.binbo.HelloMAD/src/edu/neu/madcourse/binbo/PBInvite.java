package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PBInvite extends Activity implements OnClickListener {
	private PBPlayerInfo mHostInfo;

	private ListView mLVPlayerList;
	private View mBtnBack;

	private GetInfoThread mAcquirer = null;
	private PBNameList mNames = new PBNameList();
	private ArrayList<PBPlayerInfo> mPlayerInfoList = new ArrayList<PBPlayerInfo>();
	private PBInviteHelper mPBInviteHelper = new PBInviteHelper();
	
	private static final String ACCOUNT_NAME = "account_name";
	private static final String HOST_INFO = "host_info";
	private static final int UPDATE_LISTVIEW = -1;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_invite);

		Intent intent = getIntent();
		this.mLVPlayerList = (ListView) this
				.findViewById(R.id.pbinvite_user_list);

		try {
			JSONObject hostObj = new JSONObject(intent.getStringExtra(HOST_INFO));
			this.mHostInfo = new PBPlayerInfo(hostObj.getString("name"));
			this.mHostInfo.setScore(hostObj.getInt("score"));
			this.mHostInfo.setBestScore(hostObj.getInt("best_score"));
			this.mHostInfo.setSelLetters(hostObj.getString("selected_letters"));
			this.mHostInfo.setStatus(hostObj.getString("status"));
			this.mHostInfo.setUpdateTime(hostObj.getLong("update_time"));	
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//create a thread to fetch all three types of data
		if (this.mAcquirer == null) {
			this.mAcquirer = new GetInfoThread();
		}
		this.mAcquirer.start();

		// Set up click listeners for all the buttons
		this.mBtnBack = this.findViewById(R.id.pbinvite_back_button);
		this.mBtnBack.setOnClickListener(this);
	}
	
	protected void onPause(Bundle savedInstanceState) {
		if (this.mAcquirer != null) {
			this.mAcquirer.stop();
			this.mAcquirer = null;
		}
	}
	
	protected void onResume(Bundle savedInstanceState) {
		if (this.mAcquirer == null) {
			this.mAcquirer = new GetInfoThread();
		}
		this.mAcquirer.start();
	}

	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
		case R.id.pbinvite_back_button:
			i = new Intent(this, PBMain.class);
			i.putExtra(ACCOUNT_NAME, this.mHostInfo.getName());
			finish();
			startActivity(i);
			break;
		}
	}
	
//	public void onServerUnavalaible() {
//		Toast.makeText(getApplicationContext(),
//				"Server connection lost.", Toast.LENGTH_LONG).show();
//	}

	public void updatePlayerList(ArrayList<PBPlayerInfo> playerInfoList) {
		ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < playerInfoList.size(); i++) {
			PBPlayerInfo info = playerInfoList.get(i);
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("ItemName", info.getName().toString());
			map.put("ItemStatus", new PBInviteHelper().convert2InviteStatus(info));
			listItem.add(map);
		}

		PBInviteSimpleAdapter listItemAdapter = new PBInviteSimpleAdapter(this, listItem,
				R.layout.pb_invite_list_item, new String[] { "ItemName",
						"ItemStatus" }, new int[] { R.id.pbinvite_name,
						R.id.pbinvite_invite_button });

		this.mLVPlayerList.setAdapter(listItemAdapter);
		listItemAdapter.notifyDataSetChanged();
	}
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {

		public void handleMessage(Message msg) {

			switch (msg.arg1) {
			case UPDATE_LISTVIEW:
				updatePlayerList(mPBInviteHelper.updatePlayerStatusList(
						mPlayerInfoList,(ArrayList<PBPlayerInfo>)msg.obj));
				mPlayerInfoList = (ArrayList<PBPlayerInfo>)msg.obj;
				break;
			}
		}
	};
	
	public class GetInfoThread extends Thread {
		public GetInfoThread() { }

		public void run() {
			PBNameList nameList = new PBNameList();
			ArrayList<PBPlayerInfo> playerInfoList = new ArrayList<PBPlayerInfo>();

			try {
				if (nameList.acquire()) {
					for (int i = 0; i < nameList.size(); i++) {
						if (!mHostInfo.getName().equals(nameList.get(i))) {
							PBPlayerInfo playerInfo = new PBPlayerInfo(nameList.get(i));
							if (playerInfo.acquire()) {
								playerInfoList.add(playerInfo);
								PBInviteInfo inviteInfo = new PBInviteInfo(nameList.get(i));
								if (inviteInfo.acquire()) {
									// check whether the current user is invited
									if (mPBInviteHelper.isInvited(mHostInfo.getName(), inviteInfo)) {
										// show invite dialog
									}
								}
							}
						}
					}
					// update player list
					Message msg = new Message();
					msg.arg1 = UPDATE_LISTVIEW;
					msg.obj = playerInfoList;
					mHandler.sendMessage(msg);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class PBInviteSimpleAdapter extends SimpleAdapter{  
	
	private List<? extends Map<String, ?>> mPlayerList;
    private Context context;
	public PBInviteSimpleAdapter(Context context,  
            List<? extends Map<String, ?>> data, int resource,  
            String[] from, int[] to) {  
        super(context, data, resource, from, to);
        this.mPlayerList = data;
        this.context = context;
    }  

    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  

        View view =  super.getView(position, convertView, parent);

        Button itemButton = (Button)view.findViewById(R.id.pbinvite_invite_button);

        String strStatus = this.mPlayerList.get(position).get("ItemStatus").toString();
        if (!strStatus.toLowerCase().equals("online")) {
        	itemButton.setEnabled(false);
        	itemButton.setTextColor(context.getResources().getColor(R.color.pb_gray2));
        	itemButton.setText(strStatus.toUpperCase());
        } else {
        	itemButton.setEnabled(true);
			itemButton.setText(context.getResources()
					.getString(R.string.pb_invite_label).toUpperCase());
        }
        
        itemButton.setOnClickListener(new View.OnClickListener() {  
        	public void onClick(View v) {
                // TODO Auto-generated method stub  
                ((Button)v).setText("clicked");  
            }  
        });  

        return view;  
    }  

}  
