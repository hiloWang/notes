package com.itheima.mobileguard.services;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

import com.itheima.mobileguard.activities.EnterPwdActivity;
import com.itheima.mobileguard.db.dao.LockedAppDao;
/**
 * 轮询当前处于栈顶的activity 判断是否加了程序锁
 * 
 * @author Administrator
 * 
 */
public class WatchDagService extends Service {

	private LockedAppDao dao;// 数据库查询
	private ActivityManager am;// 进程管理
	private RunningTaskInfo taskInfo;// 任务栈信息
	private List<String> packageNames;// 加了锁的包名集合
	private String packageName;
	private Intent intent;
	private String tempStopProtectPackageName;//临时取消保护的APP
	private WatchDogReceiver receiver;//看门狗用来接受信息的接收者
	private boolean flag;//是否轮询
	private String mySelf;//记住自己的包名
	private ContentResolver resolver;
	private LockAppDBObserver observer;
	
	public IBinder onBind(Intent intent) {
		return null;
	}

	public void onCreate() {
		resolver = getContentResolver();
		observer = new LockAppDBObserver(new Handler());
		resolver.registerContentObserver(Uri.parse("content://com.itheima.mobile.change"),true,observer);
		dao = new LockedAppDao(this);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		intent = new Intent(this, EnterPwdActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 服务中开启activity
														// 指定它自己管理任务栈信息
		//注册广播
		receiver = new WatchDogReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.itheima.mobileguard.stop");
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, filter);
		
		//查询数据库
		packageNames = dao.findAll();
		/**
		 * 看门狗核心逻辑 轮询任务栈
		 */
		startWatchDog();

		super.onCreate();
	}

	private void startWatchDog() {
		new Thread() {
			public void run() {
				/*
				 * 利用activityManager获取当前系统在所以认为站信息,为什么只取一个，因为只需要获取最近活动的任务栈，节约内存
				 * ，toActivity获取最近活动的任务栈栈顶的activity，然后获取其包名，
				 * 判断该activity所属Application是否加了程序锁 Return a list of the tasks
				 * that are currently running, with the most recent being first
				 * and older ones after in order.最近使用的任务栈在最上面
				 * 
				 */
				flag = true;
				while (flag) {
						packageName = am.getRunningTasks(1).get(0).topActivity
								.getPackageName();
						if (packageNames.contains(packageName)) {// 需要保护
							if(!( packageName.equals(tempStopProtectPackageName) || packageName.equals(mySelf))){
								//告诉输入密码的界面 现在保护的是哪个程序
								intent.putExtra("packname", packageName);
								WatchDagService.this.startActivity(intent);
							}
						}
						try {
							Thread.sleep(100);//必须要让看门狗休息一下 否则cpu占用率 会爆满
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				}
			}
		}.start();
	}
	
	/**
	 * 服务死亡 释放资源
	 */
	public void onDestroy() {
		unregisterReceiver(receiver);
		receiver = null;//方便回收资源
		flag = false;
		resolver.unregisterContentObserver(observer);
		observer = null;
		super.onDestroy();
	}

	
	/**
	 * 广播接受者 用于传递信息 和 省点优化
	 * @author Administrator
	 *
	 */
	private class WatchDogReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if("com.itheima.mobileguard.stop".equals(action)){
					String name = intent.getStringExtra("packname");
					if("com.itheima.mobileguard".equals(name)){
						mySelf = getPackageName();
					}else{
						tempStopProtectPackageName = name;
					}
				}else if(Intent.ACTION_SCREEN_OFF.equals(action)){
					//关闭了屏幕 可以停止轮询了 把锁屏之前设置的临时停止保护设置为空
					tempStopProtectPackageName = null;
					mySelf = null;
					flag = false;
				}else if(Intent.ACTION_SCREEN_ON.equals(action)){//又打开了屏幕 又可以开始轮询了
					startWatchDog();
				}
		}
	}
	
	/**
	 * 内容观察者 监听 程序锁数据库的变化 如果有变化就更新集合中的数据
	 * @author Administrator
	 *
	 */
	private class LockAppDBObserver extends ContentObserver{

		public LockAppDBObserver(Handler handler) {
			super(handler);
		}
		//数据库发送变化 更新集合
		public void onChange(boolean selfChange) {
			packageNames = dao.findAll();
			super.onChange(selfChange);
		}
		
	}
}
