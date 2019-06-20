package com.itheima.mobileguard.receivers;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.util.List;

public class KillProcessReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
        int count = list.size();
        for (RunningAppProcessInfo info : list) {
            am.killBackgroundProcesses(info.processName);
        }
        if (count == am.getRunningAppProcesses().size()) {
            Toast.makeText(context, "已经是最佳状态", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "清理完毕", Toast.LENGTH_LONG).show();
        }
    }

}
