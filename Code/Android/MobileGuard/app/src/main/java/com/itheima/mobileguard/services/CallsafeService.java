package com.itheima.mobileguard.services;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.itheima.mobileguard.db.dao.BlackNumberDao;

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
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.util.Log;

public class CallsafeService extends Service {

	private TelephonyManager tm;// 通话服务
	private CallSafeReceiver receiver;// 短信接收者
	private BlackNumberDao dao;
	private MyPhoneStateListener listener;// 监听器
	private CallsContentObserver observer;// 内容观察者
	private ContentResolver resolver;// 内容解析器

	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * 服务开启就注册广播
	 */
	@Override
	public void onCreate() {
		receiver = new CallSafeReceiver();
		dao = new BlackNumberDao(this);
		// 得到通话服务
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		// 代码注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
		super.onCreate();

		listener = new MyPhoneStateListener();
		// 监听电话的通话状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	/**
	 * 电话通话状态监听器
	 */
	public class MyPhoneStateListener extends PhoneStateListener {
		// 电话状态变化则调用
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲状态

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接通状态

				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态 这个时候判断电话是否是黑名单 和
														// 拦截模式
				String mode = dao.findModeByNumber(incomingNumber);
				if ("1".equals(mode) || "3".equals(mode)) {// 全部拦截状态和电话拦截拦截状态
					// 挂断电话的方法被谷歌给隐藏了 需要用反射的方法来 调用这个方法
					System.out.println("电话来了 ");
					endCall(incomingNumber);
				}
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	/**
	 * 释放广播
	 */
	@Override
	public void onDestroy() {
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// 取消监听
		listener = null;
		unregisterReceiver(receiver);// 取消广播
		if (resolver != null && observer != null) {
			resolver.unregisterContentObserver(observer);// 释放资源
			observer = null;
		}
		receiver = null;
		super.onDestroy();
	}

	/**
	 * 挂断电话
	 */
	public void endCall(String incomingNumber) {
		try {
			// android.permission.CALL_PHONE 需要权限
			Class clazz = Class.forName("android.os.ServiceManager");
			Method method = clazz.getDeclaredMethod("getService", String.class);
			IBinder ibinder = (IBinder) method.invoke(null, TELEPHONY_SERVICE);// 获取电话服务
			ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);// 远程服务
			// Ibinder是 服务提供的接口
			iTelephony.endCall();// 挂断电话
			// 虽然屏蔽了黑名单号码 但是会在通话记录中留下未接记录 所以需要删除通话记录
			// android.permission.CALL_PHONE
			// 删除产生的通话记录
			resolver = getContentResolver();
			Uri uri = Uri.parse("content://call_log/calls");// 查询全部的记录
			observer = new CallsContentObserver(new Handler(), incomingNumber);
			resolver.registerContentObserver(uri, true, observer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private class CallsContentObserver extends ContentObserver {
		private String incomingNumber;

		public CallsContentObserver(Handler handler, String incomingNumber) {
			super(handler);
			this.incomingNumber = incomingNumber;
		}

		@Override
		public void onChange(boolean selfChange) {
			// 只有数据发送变化才会执行
			resolver.delete(Uri.parse("content://call_log/calls"), "number=?",
					new String[] { incomingNumber });
			super.onChange(selfChange);
		}

	}

	/**
	 * 通话安全广播接收者
	 * 
	 * @author Administrator
	 * 
	 */
	private class CallSafeReceiver extends BroadcastReceiver {
		public void onReceive(Context context, Intent intent) {
			// 得到短信
			Object[] objs = (Object[]) intent.getExtras().get("pdus");
			for (Object obj : objs) {
				SmsMessage sm = SmsMessage.createFromPdu((byte[]) obj);
				String number = sm.getOriginatingAddress();// 获取号码
				// 查数据库
				String mode = dao.findModeByNumber(number);
				if ("1".equals(mode) || "2".equals(mode)) {// 全部拦截状态和短信拦截状态
					abortBroadcast();// 拦截短信
				}
			}
		}
	}
}
