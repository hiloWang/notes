package com.itheima.mobileguard.services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class KeepLifeService extends Service {

	private AlarmManager mAlarmManager;
	private PendingIntent mPendingIntent;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onCreate()
	{
		//start the service through alarm repeatly
	     Intent intent = new Intent(getApplicationContext(), KeepLifeService.class);        
		mAlarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
		mPendingIntent = PendingIntent.getService(this, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
		long now = System.currentTimeMillis();
		mAlarmManager.setInexactRepeating(AlarmManager.RTC, now, 60000, mPendingIntent);
		super.onCreate();
	}

}
