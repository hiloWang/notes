package com.itheima.mobileguard.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;

public class ClearProcessService extends Service {
	private LockScreenReceiver receiver;
	private Timer timer;
	private TimerTask task;
	private SharedPreferences sp;

	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 注册广播
		receiver = new LockScreenReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		registerReceiver(receiver, filter);

		/**
		 * 定时清理
		 */
		if (sp.getBoolean("killOnTime", false)) {
			timer = new Timer();
			task = new TimerTask() {
				public void run() {
					killAllProcess();
				}
			};

			timer.schedule(task, 0, sp.getLong("period", 0));
		}
		
		CountDownTimer cTimer = new CountDownTimer(0, 0) {
			public void onTick(long millisUntilFinished) {
				
			}
			public void onFinish() {
					//这里可以冲从新调用 达到循环的效果
			}
		};
		
		super.onCreate();
	}

	
	
	
	/**
	 * 锁屏广播接收者
	 * 
	 * @author Administrator
	 * 
	 */
	private class LockScreenReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			killAllProcess();
		}
	}

	private void killAllProcess() {
		ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		for (RunningAppProcessInfo info : am.getRunningAppProcesses()) {
			am.killBackgroundProcesses(info.processName);
		}
	}

	@Override
	public void onDestroy() {
		if(timer != null && task != null){
			timer.cancel();
			task.cancel();
			timer = null;
			task = null;
		}
		unregisterReceiver(receiver);
		receiver = null;
		super.onDestroy();
	}
}
