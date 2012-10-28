package edu.neu.madcourse.binbo;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import edu.neu.madcourse.binbo.PBInvite.GetInfoThread;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.ListView;

public class InviteService extends Service {  
	  
	private Looper mServiceLooper;
	private ServiceHandler mServiceHandler;
	private static final int NOTIFICATION_ID = 1023; // seems ok
	
	private static final String SERVICE_COMMAND = "service_command";
	private static final int SERVICE_START = 1;
	private static final int SERVICE_END   = 2;
	private static final String HOST_INFO = "host_info";
	
	private boolean mRun = true;
	private boolean invitationfind = false;
    private PBNameList nameList = new PBNameList();
	private PBPlayerInfo mOpponentInfo = null;
	private PBInviteInfo mInviteInfo = null;
	private PBPlayerInfo mHostInfo = null;
	
	private ListView mLVPlayerList;
	private View mBtnBack;

	private GetInfoThread mAcquirer = null;
	private PBNameList mNames = new PBNameList();
	private ArrayList<PBPlayerInfo> mPlayerInfoList = new ArrayList<PBPlayerInfo>();
	private PBInviteHelper mPBInviteHelper = new PBInviteHelper();
    private ArrayList<PBPlayerInfo> playerInfoList = new ArrayList<PBPlayerInfo>();
	
	private final class ServiceHandler extends Handler {		
		
		public ServiceHandler(Looper looper) {
			super(looper);
		}
		@Override
		public void handleMessage(Message msg) {		
			
			while (mRun) {		
				try {
					if (nameList.acquire()) {
					getPlayerInfo();
					Thread.sleep(50);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			NotificationManager nm = 
					(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			nm.cancel(NOTIFICATION_ID);
			stopSelf(msg.arg1);
		}
		
		private void showNotification(String message) {
			int id = 2;
			NotificationManager nm = 
					(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			Notification notification = 
					new Notification(R.drawable.ic_launcher, "Persistent Boggle", System.currentTimeMillis());
			notification.flags = Notification.FLAG_AUTO_CANCEL;
			Intent intent = new Intent(getApplicationContext(), PBInvite.class);
			try {
				intent.putExtra(HOST_INFO, mHostInfo.obj2json());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
			PendingIntent contentIntent = PendingIntent.getActivity(
					getApplicationContext(), NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			notification.setLatestEventInfo(getApplicationContext(), "Persistent Boggle",
					message + " has invited you to play Persistent Boggle together.",
					contentIntent);
			nm.notify(NOTIFICATION_ID, notification);
		}
		
		private void removeNotification(){
			NotificationManager nm = 
					(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			nm.cancel(NOTIFICATION_ID);
		}
		
		// get player info of each player in the name list
	    public void getPlayerInfo () throws JSONException {
	    	invitationfind = false;
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
			if(!invitationfind)
				removeNotification();
		}
		
		// get invite info of the given player
		public void getInviteInfo (String name, PBPlayerInfo playerInfo) throws JSONException {
			PBInviteInfo inviteInfo = new PBInviteInfo(name);
			if (inviteInfo.acquire()) {
				// check whether the current host user is invited
				if (mPBInviteHelper.isInvited(mHostInfo.getName(), inviteInfo)) {
					invitationfind = true;
					showNotification(inviteInfo.getPoster());
				}
			}
		}
	}

	@Override
	public void onCreate() {

		HandlerThread thread = new HandlerThread("ServiceStartArguments", 0);
		thread.start();
		
		// Get the HandlerThread's Looper and use it for our Handler
		mServiceLooper = thread.getLooper();
		mServiceHandler = new ServiceHandler(mServiceLooper);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {		
		Bundle bundle = intent.getExtras();
		int cmd = bundle.getInt(SERVICE_COMMAND);
		
		if (cmd == SERVICE_START) {
			mRun = true;
			// give it an id, so we can know which one to stop after the work is done
			JSONObject hostObj;
			try {
				hostObj = new JSONObject(intent.getStringExtra(HOST_INFO));
				mHostInfo = new PBPlayerInfo(hostObj.getString("name"));
				mHostInfo.setScore(hostObj.getInt("score"));
				mHostInfo.setBestScore(hostObj.getInt("best_score"));
				mHostInfo.setSelLetters(hostObj.getString("selected_letters"));
				mHostInfo.setStatus(hostObj.getString("status"));
				mHostInfo.setUpdateTime(hostObj.getLong("update_time"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
			Message msg = mServiceHandler.obtainMessage();
			msg.arg1 = startId;
			mServiceHandler.sendMessage(msg);
		} else if (cmd == SERVICE_END) {
			mRun = false;
		}
		
		return START_STICKY; // restart if be killed
	}

	@Override
	public IBinder onBind(Intent intent) {
		// We don't provide binding, so return null
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
}