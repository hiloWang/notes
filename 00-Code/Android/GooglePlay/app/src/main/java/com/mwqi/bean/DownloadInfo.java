package com.mwqi.bean;

import com.mwqi.manager.DownloadManager;
import com.mwqi.utils.FileUtils;

public class DownloadInfo {

	private long id;//app的id，和appInfo中的id对应
	private String appName;//app的软件名称
	private long appSize = 0;//app的size
	private long currentSize = 0;//当前的size
	private int downloadState = 0;//下载的状态
	private String url;//下载地址
	private String path;//保存路径

	/** 从AppInfo中构建出一个DownLoadInfo */
	public static DownloadInfo clone(AppInfo info) {
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.id = info.getId();
		downloadInfo.appName = info.getName();
		downloadInfo.appSize = info.getSize();
		downloadInfo.currentSize = 0;
		downloadInfo.downloadState = DownloadManager.STATE_NONE;
		downloadInfo.url = info.getDownloadUrl();
		downloadInfo.path = FileUtils.getDownloadDir() + downloadInfo.appName + ".apk";
		return downloadInfo;
	}

	public String getPath() {
		return path;
	}

	public float getProgress() {
		if (getAppSize() == 0) {
			return 0;
		}
		return (getCurrentSize() + 0.0f) / getAppSize();
	}

	public synchronized String getUrl() {
		return url;
	}

	public synchronized void setUrl(String url) {
		this.url = url;
	}

	public synchronized long getId() {
		return id;
	}

	public synchronized void setId(long id) {
		this.id = id;
	}

	public synchronized String getAppName() {
		return appName;
	}

	public synchronized void setAppName(String appName) {
		this.appName = appName;
	}

	public synchronized long getAppSize() {
		return appSize;
	}

	public synchronized void setAppSize(long appSize) {
		this.appSize = appSize;
	}

	public synchronized long getCurrentSize() {
		return currentSize;
	}

	public synchronized void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
	}

	public synchronized int getDownloadState() {
		return downloadState;
	}

	public void setDownloadState(int downloadState) {
		this.downloadState = downloadState;
	}
}
