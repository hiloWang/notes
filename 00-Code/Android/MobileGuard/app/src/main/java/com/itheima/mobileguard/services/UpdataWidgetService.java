package com.itheima.mobileguard.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.receivers.MyWidgetReceiver;
import com.itheima.mobileguard.utils.SystemInfoUtils;

public class UpdataWidgetService extends Service {
	
	/**
	 * 获取widget的状态并更新
	 */
	private AppWidgetManager widgetManager;
	private Timer timer;
	private TimerTask task;
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	/**
	 * 服务被创建
	 */
	public void onCreate() {
		widgetManager = AppWidgetManager.getInstance(this);//获取widget管理器
		
		final ComponentName provider = new ComponentName(this,MyWidgetReceiver.class);
	//把包名和 id告诉桌面应用
		final RemoteViews views = new RemoteViews(getPackageName(),R.layout.process_widget);
		timer = new Timer();
		task = new TimerTask() {
			public void run() {
				/**
				 * ContentViews：
				 * 远程View的描述信息 由远程的桌面应用根据Views的描述信息把View跟new出来
				 *  由另外一个进程 更新ui程 更新ui
				 */
				views.setTextViewText(R.id.process_count, "正在运行的进程"+SystemInfoUtils.getRunningProcessCount(UpdataWidgetService.this));
				views.setTextViewText(R.id.process_memory, "可用内存"+Formatter.formatFileSize(UpdataWidgetService.this, SystemInfoUtils.getAvailableMemory(UpdataWidgetService.this)));
				Intent intent = new Intent();
				intent.setAction("com.itheima.clearall");
				PendingIntent   pendingIntent = PendingIntent.getBroadcast(UpdataWidgetService.this, 0, intent, 0);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				widgetManager.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 0, 5000);
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		if(timer != null && task != null){
			timer.cancel();
			task.cancel();
			timer = null;
			task = null;
		}
		super.onDestroy();
	}
	
}




