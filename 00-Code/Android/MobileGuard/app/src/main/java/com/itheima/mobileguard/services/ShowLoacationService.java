package com.itheima.mobileguard.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.R.layout;
import com.itheima.mobileguard.db.dao.NumberAddressDao;

public class ShowLoacationService extends Service {

	protected static final String TAG = "ShowLoacationService";
	private int[] itemsImageId = {R.drawable.call_locate_white,R.drawable.call_locate_orange,R.drawable.call_locate_blue
			,R.drawable.call_locate_gray,R.drawable.call_locate_green};
	private SharedPreferences sp;
	private TelephonyManager tm;// 通话服务
	private MyPhoneStateListener listener;// 监听器
	private OutCallingReceiver receiver;// 打出电话接收者
	private View view;
	// 声明窗体管理器
	private WindowManager windowManager;
	// 窗口管理器添加view需要的布局的参数
	private WindowManager.LayoutParams params;

	public IBinder onBind(Intent intent) {

		return null;
	}

	/**
	 * 服务开启就注册广播
	 */
	@Override
	public void onCreate() {
		receiver = new OutCallingReceiver();
		// 得到通话服务
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

		// 得到窗体管理器
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 代码注册广播
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(receiver, filter);
		listener = new MyPhoneStateListener();
		// 监听电话的通话状态
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		super.onCreate();
	}

	/**
	 * 监听打出的电话
	 * 
	 * @author Administrator
	 * 
	 */
	private class OutCallingReceiver extends BroadcastReceiver {

		public void onReceive(Context context, Intent intent) {
			String number = getResultData();
			// Toast.makeText(context,
			// NumberAddressDao.query(number, context),
			// Toast.LENGTH_LONG).show();
			showCustomToast(number);
		}

	}

	/**
	 * 显示自定义土司
	 * 
	 * @param msg
	 */
	private void showCustomToast(String msg) {
		view = View.inflate(this, R.layout.toast_showaddress, null);
		view.setBackgroundResource(itemsImageId[sp.getInt("item", 0)]);
		TextView tv = (TextView) view.findViewById(R.id.tv_toast_address);
		tv.setText(NumberAddressDao.query(msg, this));
		params = new LayoutParams();
//		获取土司的位置 为了方便计算 设置土司的对齐方法是 以屏幕的左上角(默认的对齐方式是屏幕的中间)
		params.gravity = Gravity.LEFT + Gravity.TOP;
		
		params.x = sp.getInt("startX", windowManager.getDefaultDisplay().getWidth()>>1);//土司的0 是屏幕的中间
		params.y = sp.getInt("startY",  windowManager.getDefaultDisplay().getHeight()>>1);
		
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
//				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE    需要被触摸
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		// params.windowAnimations =
		// com.android.internal.R.style.Animation_Toast; 渐变动画
		// params.type = WindowManager.LayoutParams.TYPE_TOAST; 土司窗体天生不响应触摸事件
		/**
		 * Window type: priority phone UI, which needs to be displayed even if
		 * the keyguard is active. These windows must not take input focus, or
		 * they will interfere with the keyguard. In multiuser systems shows on
		 * all users' windows. 
		 * 窗型：优先电话的用户界面，它需要显示即使
		 * 键盘锁是活动的。这些窗户必须不带输入
		 * 焦点，或者他们会妨碍键盘锁。
		 * 在多用户系统中显示所有用户的Windows。
		 */
//		需要权限 android.permission.SYSTEM_ALERT_WINDOW
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;// 只有这样两种窗体类型才能被使用
		// 其他的都是系统用的我们用会报错
		
		windowManager.addView(view, params);

		view.setOnTouchListener(new OnTouchListener() {
			int startX = 0;
			int startY = 0;
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:// 手指按下
					startX = (int) event.getRawX();// 手机按下记住x y 位置
					startY = (int) event.getRawY();
					Log.i(TAG, "起始位置 :x" + startX + ":Y" + startY);
					break;
				case MotionEvent.ACTION_MOVE:// 手指移动
					System.out.println("AAAAAAAAAAAAAAAAAAAAA"+view);
					System.out.println("AAAAAAAAAAAAAAAAAAAAA"+v);
					// 移动后的位置
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					Log.i(TAG, "新的位置 :x" + newX + ":Y" + newY);
					// 计算出移动的距离
					int dx = newX - startX;
					int dy = newY - startY;
					Log.i(TAG, "移动的距离位置 :x" + dx + ":Y" + dy);
					// 更新土司的位置  当前位置+ 移动的距离 = 新的位置
					params.x += dx;
					params.y += dy;
//					防止土司的位置超出屏幕
					if(params.x <=0){
						params.x = 0;
					}
					if(params.y <=0){
						params.y =0;
					}
					if(params.x >= windowManager.getDefaultDisplay().getWidth()-view.getWidth()){
						params.x = windowManager.getDefaultDisplay().getWidth()-view.getWidth();
					}
					if(params.y >= windowManager.getDefaultDisplay().getHeight() - view.getHeight()){
						params.y = windowManager.getDefaultDisplay().getHeight() - view.getHeight();
					}
					
					 windowManager.updateViewLayout(view, params);
					 //更新土司现在的位置
					 startX = (int) event.getRawX();// 手机按下记住x y 位置
					 startY = (int) event.getRawY();
					 Log.i(TAG, "新的起始位置 :x" + startX + ":Y" + startY);
					break;
				case MotionEvent.ACTION_UP:// 手机离开
//					手指离开记住土司的位置 
					sp.edit().putInt("startX", startX).putInt("startY", startY).commit();
					break;

				default:
					break;
				}
				return false;
			}
		});
	}

	/**
	 * 电话通话状态监听器
	 */
	public class MyPhoneStateListener extends PhoneStateListener {
		// 电话状态变化则调用
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:// 空闲状态
				if (view != null) {
					windowManager.removeView(view);
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:// 接通状态
				break;
			case TelephonyManager.CALL_STATE_RINGING:// 响铃状态
				showCustomToast(incomingNumber);
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
		receiver = null;
		super.onDestroy();
	}
}
