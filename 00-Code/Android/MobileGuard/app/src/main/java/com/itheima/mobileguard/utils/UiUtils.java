package com.itheima.mobileguard.utils;

import android.app.Activity;
import android.widget.Toast;

public class UiUtils {
	/**
	 * 弹出土司的工具方法 无需考虑子线程
	 * @param act
	 * @param msg
	 */
	public static void showToast(final Activity act,final String msg){
		if("main".equals(Thread.currentThread().getName())){
			Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
		}else{
			act.runOnUiThread(new Runnable(){
				public void run() {
					Toast.makeText(act, msg, Toast.LENGTH_LONG).show();
				}
			});
		}
	}
}
