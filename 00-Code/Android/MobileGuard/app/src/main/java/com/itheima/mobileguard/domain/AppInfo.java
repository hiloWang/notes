package com.itheima.mobileguard.domain;

import android.graphics.drawable.Drawable;

public class AppInfo {
	
	

	/**
	 * 安装路径
	 */
	private String apkPath;
	
	/**
	 * 应用程序的图标
	 */
	
	private Drawable appIcon;
	/**
	 * 应用程序名称
	 */
	private String appName ;
	/**
	 * 应用程序安装的位置，true手机内存 ，false外部存储卡
	 */
	private boolean isInRom;
	/**
	 * 应用程序的大小
	 */
	private long appSize;
	/**
	 * 是否是用户程序  true 用户程序 false 系统程序
	 */
	private boolean isUserApp;
	/**
	 * 应用程序的包名
	 */
	private String packageName;
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public boolean isInRom() {
		return isInRom;
	}
	public void setInRom(boolean isInRom) {
		this.isInRom = isInRom;
	}
	public long getAppSize() {
		return appSize;
	}
	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}
	public boolean isUserApp() {
		return isUserApp;
	}
	public void setUserApp(boolean isUserApp) {
		this.isUserApp = isUserApp;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getApkPath() {
		return apkPath;
	}
	public void setApkPath(String apkPath) {
		this.apkPath = apkPath;
	}
	@Override
	public String toString() {
		return "AppInfo [appName=" + appName + ", isInRom=" + isInRom
				+ ", appSize=" + appSize + ", isUserApp=" + isUserApp
				+ ", packageName=" + packageName + "]";
	}
	 
	
}
