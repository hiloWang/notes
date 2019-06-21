package com.itheima.mobileguard.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class GuardServer2 extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		Intent intent = new Intent(this,GuardServer1.class);
		startService(intent);
		super.onDestroy();
	}
}
