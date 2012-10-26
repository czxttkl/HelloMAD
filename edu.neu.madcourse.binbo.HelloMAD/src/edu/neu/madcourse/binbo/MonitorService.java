package edu.neu.madcourse.binbo;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;


public class MonitorService extends IntentService {  
	  
	public MonitorService() {  
		// give the work thread a name	
		super("MonitorService");  
	}  
	   
	@Override  
	protected void onHandleIntent(Intent intent) {  
		// Normally we would do some work here, like download a file.  
		// For our sample, we just sleep for 5 seconds.  
		long endTime = System.currentTimeMillis() + 5*1000;  
		while (System.currentTimeMillis() < endTime) {  
			synchronized (this) {  
				try {  
					wait(endTime - System.currentTimeMillis());  
				} catch (Exception e) {  
					
				}  
			}  
		}  
		Toast.makeText(getApplicationContext(), 
                "fucking service", Toast.LENGTH_LONG).show();
	}  
	
	@Override  
	public int onStartCommand(Intent intent, int flags, int startId) {  
	    Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();  
	    return super.onStartCommand(intent,flags,startId);  
	}
}