package com.itheima.mobileguard.domain;

import android.graphics.drawable.Drawable;

public class TrafficInfo {
	
	private String appName;
	private String packageName;
	private Drawable icon;
	private int uid;
	private long upTraffic;
	private long downTraffic;
	
	public long getUpTraffic() {
		return upTraffic;
	}
	public void setUpTraffic(long upTraffic) {
		this.upTraffic = upTraffic;
	}
	public long getDownTraffic() {
		return downTraffic;
	}
	public void setDownTraffic(long downTraffic) {
		this.downTraffic = downTraffic;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	@Override
	public String toString() {
		return "TrafficInfo [appName=" + appName + ", packageName="
				+ packageName + ", icon=" + icon + ", uid=" + uid + "]";
	}
	
}
