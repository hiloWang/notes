package com.itheima.mobileguard.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class SystemInfoUtils {
	/**
	 * 判断一个服务是否处于运行状态
	 * @param context 上下文
	 * @return
	 */
	public static boolean isServiceRunning(Context context,String className){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(200);
		for(RunningServiceInfo info:infos){
			String serviceClassName = info.service.getClassName();
			if(className.equals(serviceClassName)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 获取总的内存
	 * @param context
	 * @return
	 */
	public static long getTotalMemory(Context context){
		try {
			FileInputStream fis = new FileInputStream("/proc/meminfo");
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String info = br.readLine();
			br.close();
			StringBuilder sb = new StringBuilder();
			for(int i = 0 ;i<info.length();i++){
				if(info.charAt(i) <= '9' && info.charAt(i) >= '0'){
					sb.append(info.charAt(i));
				}
			}
			long total = Long.parseLong(sb.toString()) * 1024;
			return total;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0l;
	}
	
	/**
	 * 获取系统当前剩余内存
	 * @param context
	 * @return
	 */
	public static long getAvailableMemory(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(outInfo);
		//System.out.println(outInfo.totalMem); 安卓4.0才支持的API
		return outInfo.availMem;
	}
	/**
	 * 获取系统正在运行的进程数量
	 * @param context
	 * @return
	 */
	public static int getRunningProcessCount(Context context){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		int count = am.getRunningAppProcesses().size();
		return count;
	}
}
