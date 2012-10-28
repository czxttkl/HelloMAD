package edu.neu.madcourse.binbo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class PBInvite extends Activity implements OnClickListener {
	private PBPlayerInfo mHostInfo = null;
	private PBPlayerInfo mOpponentInfo = null;
	private PBInviteInfo mInviteInfo = null;
	private PBInviteInfo mComingInviteInfo = null;

	private ListView mLVPlayerList;
	private View mBtnBack;
	private ProgressDialog mpDialog;

	private GetInfoThread mAcquirer = null;
	private CheckInviteThread mInviteAcquirer = null;
	private PBNameList mNames = new PBNameList();
	private ArrayList<PBPlayerInfo> mPlayerInfoList = new ArrayList<PBPlayerInfo>();
	private PBInviteHelper mPBInviteHelper = new PBInviteHelper();
	private ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	private PBInviteSimpleAdapter listItemAdapter;
	
	private static final String ACCOUNT_NAME = "account_name";
	public  static final String HOST_INFO     = "host_info";
	public  static final String OPPONENT_INFO = "opponent_info";
	private static final int SERVER_UNAVAILABLE = 0;
	private static final int UPDATE_LISTVIEW    = 1;
	private static final int INVITE_RECEIVE     = 2;
	private static final int INVITE_EXPIRE      = 3;
	private static final int INVITE_ACCEPT      = 4;
	private static final int INVITE_DECLINE     = 5;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pb_invite);
		
		//save data of the current user locally
		try {
			Intent intent = getIntent();
			JSONObject hostObj = new JSONObject(intent.getStringExtra(HOST_INFO));
			this.mHostInfo = new PBPlayerInfo(hostObj.getString("name"));
			this.mHostInfo.setScore(hostObj.getInt("score"));
			this.mHostInfo.setBestScore(hostObj.getInt("best_score"));
			this.mHostInfo.setSelLetters(hostObj.getString("selected_letters"));
			this.mHostInfo.setStatus(hostObj.getString("status"));
			this.mHostInfo.setUpdateTime(hostObj.getLong("update_time"));	
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		// mark the current user as online in invite screen
		(new CommitTask(mHandler,this.mHostInfo)).execute();
		
		// create a thread to fetch all three types of data
		if (this.mAcquirer == null) {
			this.mAcquirer = new GetInfoThread();
		}
		this.mAcquirer.start();

		// Set up click listeners for all the buttons
		this.mBtnBack = this.findViewById(R.id.pbinvite_back_button);
		this.mBtnBack.setOnClickListener(this);
		
		// set up an empty player list
		this.mLVPlayerList = (ListView) this.findViewById(R.id.pbinvite_user_list);
		listItemAdapter = new PBInviteSimpleAdapter(this, listItem,
				R.layout.pb_invite_list_item, new String[] { "ItemName",
						"ItemStatus" }, new int[] { R.id.pbinvite_name,
						R.id.pbinvite_invite_button });

		this.mLVPlayerList.setAdapter(listItemAdapter);
	}
	
	protected void onPause(Bundle savedInstanceState) {
		if (this.mAcquirer != null) {
			this.mAcquirer.end();
			this.mAcquirer = null;
		}
		
		if (this.mInviteAcquirer != null) {
			this.mInviteAcquirer.end();
			this.mInviteAcquirer = null;
		}
	}
	
	protected void onResume(Bundle savedInstanceState) {
		if (this.mAcquirer == null) {
			this.mAcquirer = new GetInfoThread();
		}
		this.mAcquirer.start();
		
		if (this.mInviteAcquirer == null) {
			this.mInviteAcquirer = new CheckInviteThread();
		}
		this.mInviteAcquirer.start();
	}

	public void onClick(View v) {
		Intent i = null;

		switch (v.getId()) {
		case R.id.pbinvite_back_button:

			updatePlayerStatus(mHostInfo, "offline");
			
			if (this.mAcquirer != null) {
				this.mAcquirer.end();
				this.mAcquirer = null;
			}
			
			i = new Intent(this, PBMain.class);
			i.putExtra(ACCOUNT_NAME, this.mHostInfo.getName());
			finish();
			startActivity(i);
			break;
		}
	}
	
	public void updateInviteStatus(PBInviteInfo inviteInfo, String strStatus) {
		inviteInfo.setStatus(strStatus);
		(new CommitTask(mHandler, inviteInfo)).execute();
	}
	
	public void updatePlayerStatus(PBPlayerInfo playerInfo, String strStatus) {
		playerInfo.setStatus(strStatus);
		playerInfo.setUpdateTime((new Date()).getTime());
		(new CommitTask(mHandler, playerInfo)).execute();
	}

	public void updatePlayerList(ArrayList<PBPlayerInfo> playerInfoList) {
		for (int i = 0; i < playerInfoList.size(); i++) {
			boolean finditem = false;
			PBPlayerInfo info = playerInfoList.get(i);
			for(int j=0;j<listItem.size();j++){
				HashMap<String, String> map = listItem.get(j);
				if(map.get("ItemName").toString().equals(info.mName)){
					map.put("ItemStatus", new PBInviteHelper().convert2InviteStatus(info));
					listItem.set(j, map);
					finditem=true;
				}
			}
			if(!finditem){
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("ItemName", info.getName().toString());
				map.put("ItemStatus", new PBInviteHelper().convert2InviteStatus(info));
				listItem.add(map);
			}
		}
		listItemAdapter.notifyDataSetChanged();
	}
	
	public void showInviteDialog() {
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setMessage(mComingInviteInfo.getPoster() + " " + getString(R.string.pb_invite_msg));
		ad.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				updatePlayerStatus(mHostInfo, "playing");
				updateInviteStatus(mComingInviteInfo, "accepted");
				startGame();
			}
		});
		ad.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int i) {
				updatePlayerStatus(mHostInfo, "online");
				updateInviteStatus(mComingInviteInfo, "declined");
			}
		});
		ad.setOnKeyListener(new android.content.DialogInterface.OnKeyListener() {
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK)
					return true;
				return false;
			}
		});
		ad.show();
	}
	
	public void startGame() {
		Intent intent = new Intent(this, PBGame.class);
		Bundle bundle = new Bundle();  
	    bundle.putBoolean("new game", true);		    
	    bundle.putSerializable(HOST_INFO, mHostInfo);
	    bundle.putSerializable(OPPONENT_INFO, mOpponentInfo); 
	    intent.putExtras(bundle);
	    
		finish();	
		startActivity(intent);
	}
	
	public void onInviteAccept(PBInviteInfo inviteInfo) {
		updatePlayerStatus(mHostInfo, "playing");
		try {
			inviteInfo.delete();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (mInviteAcquirer != null) {
        	mInviteAcquirer.end();
		}
        mInviteAcquirer = null;

		this.startGame();
	}
	
	public void onInviteDecline(PBInviteInfo inviteInfo) {
		updatePlayerStatus(mHostInfo, "online");
		try {
			inviteInfo.delete();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (mInviteAcquirer != null) {
        	mInviteAcquirer.end();
		}
        mInviteAcquirer = null;

		this.mpDialog.dismiss();
	}
	
	private final Handler mHandler = new Handler(Looper.getMainLooper()) {

		public void handleMessage(Message msg) {

			switch (msg.arg1) {
				case SERVER_UNAVAILABLE:
					Toast.makeText(getApplicationContext(),
							"Server connection lost.", Toast.LENGTH_LONG).show();
					break;
				case UPDATE_LISTVIEW:
					updatePlayerList(mPBInviteHelper.updatePlayerStatusList(
							mPlayerInfoList,(ArrayList<PBPlayerInfo>)msg.obj));
					mPlayerInfoList = (ArrayList<PBPlayerInfo>)msg.obj;
					break;
				case INVITE_RECEIVE:
					mComingInviteInfo = (PBInviteInfo) msg.obj;
					showInviteDialog();
					break;
				case INVITE_EXPIRE:
					//deal with invite expiring
					break;
				case INVITE_ACCEPT:
					onInviteAccept((PBInviteInfo) msg.obj);
					break;
				case INVITE_DECLINE:
					onInviteDecline((PBInviteInfo) msg.obj);
					break;
				default:
					break;
			}
		}
	};
	
	// Used to download all data to refresh player list
	// and check whether someone invites me
	public class GetInfoThread extends Thread {
	    private boolean mRun = false;
	    private boolean mEnd = true;
	    private PBNameList nameList = new PBNameList();
	    private ArrayList<PBPlayerInfo> playerInfoList = new ArrayList<PBPlayerInfo>();
	    
	    public GetInfoThread() {}
	    
	    public void run() {
	    	mRun = true;
	    	mEnd = false;
	    	while (mRun){
				try {
					if (nameList.acquire()) {
						this.getPlayerInfo();
						
						// send message to update player list
						Message msg = new Message();
						msg.arg1 = UPDATE_LISTVIEW;
						msg.obj = playerInfoList;
						mHandler.sendMessage(msg);
						
						// commit new host player info with current status
						String strStatus = mHostInfo.getStatus();
						if(!strStatus.equals("inviting")) strStatus = "online";
						updatePlayerStatus(mHostInfo, strStatus);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				finally {        	
					try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	    	mEnd = true;
		}
		
		// get player info of each player in the name list
	    public void getPlayerInfo () throws JSONException {
			for (int i = 0; i < nameList.size(); i++) {
				String name = nameList.get(i);
				if (!mHostInfo.getName().equals(name)) {
					PBPlayerInfo playerInfo = new PBPlayerInfo(name);
					if (playerInfo.acquire()) {
						playerInfoList.add(playerInfo);
						this.getInviteInfo(name,playerInfo);
					}
				}
			}
		}
		
		// get invite info of the given player
		public void getInviteInfo (String name, PBPlayerInfo playerInfo) throws JSONException {
			PBInviteInfo inviteInfo = new PBInviteInfo(name);
			if (inviteInfo.acquire()) {
				// check whether the current host user is invited
				if (mPBInviteHelper.isInvited(mHostInfo.getName(), inviteInfo)) {
					if(!inviteInfo.getHasNotified()){
						updatePlayerStatus(mHostInfo, "inviting");
						inviteInfo.setHasNotified(true);	
						(new CommitTask(mHandler, inviteInfo)).execute();
						mOpponentInfo = playerInfo;
						
						// send message to show invite dialog
						Message msg = new Message();
						msg.arg1 = INVITE_RECEIVE;
						msg.obj = inviteInfo;
						mHandler.sendMessage(msg);
					}
				}
			}
		}
		
		public synchronized void end() {
			mRun = false;	
			
			try {
				while (mEnd == false) {
					sleep(30);
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	// Used to check the status of the invitation I posted
	public class CheckInviteThread extends Thread {
		private boolean mRun = false;
	    private boolean mEnd = true;
	    private PBInviteInfo inviteInfo = new PBInviteInfo(mHostInfo.getName());
	    
	    public CheckInviteThread() {}
	    
	    public void run() {
	    	mRun = true;
	    	mEnd = false;
	    	while (mRun){
				try {
					if (inviteInfo.acquire()) {
						Message msg = new Message();
						msg.arg1 = 100;
						String strStatus = inviteInfo.getStatus();
						if (strStatus.equals("unknown")) {
							if (inviteInfo.isExpired()) {
								msg.arg1 = INVITE_EXPIRE;
							}
						} else if (strStatus.equals("accepted")) {
							msg.arg1 = INVITE_ACCEPT;
						} else if (strStatus.equals("declined")) {
							msg.arg1 = INVITE_DECLINE;
						}
						msg.obj = inviteInfo;
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				finally {        	
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
	    	mEnd = true;
		}
		
		public synchronized void end() {
			mRun = false;	
			
			try {
				while (mEnd == false) {
					sleep(30);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// Adapter for player list item
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
	        	itemButton.setTextColor(context.getResources().getColor(R.color.pb_gray1));
	        	itemButton.setText(strStatus.toUpperCase());
	        } else {
	        	itemButton.setEnabled(true);
	        	itemButton.setTextColor(context.getResources().getColor(R.color.pb_black));
				itemButton.setText(context.getResources()
						.getString(R.string.pb_invite_label).toUpperCase());
	        }
	        itemButton.setTag(position);
	        
	        itemButton.setOnClickListener(new OnClickListener() {  
	        	public void onClick(View v) {
	                // Add spinner
	        		mpDialog = new ProgressDialog(context);   
	                mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	                mpDialog.setMessage(context.getResources().getString(R.string.pb_invite_wait_msg));   
	                mpDialog.setIndeterminate(false);
	                mpDialog.setCancelable(false); 
	                mpDialog.show();
	                
	                // Start thread to check whether the invitation is handled
	                if (mInviteAcquirer == null) {
	                	mInviteAcquirer = new CheckInviteThread();
	        		}
	                mInviteAcquirer.start();
	        		
	        		// Send invitation
	        		PBInviteInfo inviteInfo = new PBInviteInfo(mHostInfo.getName());
	        		int pos = Integer.parseInt(v.getTag().toString());
	        		String strReceiver = mPlayerList.get(pos).get("ItemName").toString();
	        		inviteInfo.setReceiver(strReceiver);
	        		inviteInfo.setPostTime((new Date()).getTime());
	        		(new CommitTask(mHandler, inviteInfo)).execute();
	        		updatePlayerStatus(mHostInfo, "inviting");
	            }  
	        });  

	        return view;  
	    }
	}
}
