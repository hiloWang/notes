package com.itheima.mobileguard.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.activities.HomeActivity;

public class GuardServer1 extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Intent contentIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 100, contentIntent, 0);
        final Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle("小卫士保护您的安全")
                .setContentText("守护模式 小卫士为您站岗")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);
        startForeground(100, builder.build());
        super.onCreate();
    }

    public void onDestroy() {
        Intent intent = new Intent(this, GuardServer2.class);
        startService(intent);
        super.onDestroy();
    }
}
