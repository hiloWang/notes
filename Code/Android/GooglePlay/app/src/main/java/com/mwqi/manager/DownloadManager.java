package com.mwqi.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mwqi.bean.AppInfo;
import com.mwqi.bean.DownloadInfo;
import com.mwqi.http.HttpHelper;
import com.mwqi.http.HttpHelper.HttpResult;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.mwqi.utils.IOUtils;
import com.mwqi.utils.LogUtils;
import com.mwqi.utils.UIUtils;

public class DownloadManager {
	public static final int STATE_NONE = 0;
	/** 等待中 */
	public static final int STATE_WAITING = 1;
	/** 下载中 */
	public static final int STATE_DOWNLOADING = 2;
	/** 暂停 */
	public static final int STATE_PAUSED = 3;
	/** 下载完毕 */
	public static final int STATE_DOWNLOADED = 4;
	/** 下载失败 */
	public static final int STATE_ERROR = 5;
	
	private static DownloadManager instance;

	private DownloadManager() {
	}

	/** 用于记录下载信息，如果是正式项目，需要持久化保存 */
	private Map<Long, DownloadInfo> mDownloadMap = new ConcurrentHashMap<Long, DownloadInfo>();
	/** 用于记录观察者，当信息发送了改变，需要通知他们 */
	private List<DownloadObserver> mObservers = new ArrayList<DownloadObserver>();
	/** 用于记录所有下载的任务，方便在取消下载时，通过id能找到该任务进行删除 */
	private Map<Long, DownloadTask> mTaskMap = new ConcurrentHashMap<Long, DownloadTask>();

	public static synchronized DownloadManager getInstance() {
		if (instance == null) {
			instance = new DownloadManager();
		}
		return instance;
	}

	/** 注册观察者 */
	public void registerObserver(DownloadObserver observer) {
		synchronized (mObservers) {
			if (!mObservers.contains(observer)) {
				mObservers.add(observer);
			}
		}
	}

	/** 反注册观察者 */
	public void unRegisterObserver(DownloadObserver observer) {
		synchronized (mObservers) {
			if (mObservers.contains(observer)) {
				mObservers.remove(observer);
			}
		}
	}

	/** 当下载状态发送改变的时候回调 */
	public void notifyDownloadStateChanged(DownloadInfo info) {
		synchronized (mObservers) {
			for (DownloadObserver observer : mObservers) {
				observer.onDownloadStateChanged(info);
			}
		}
	}

	/** 当下载进度发送改变的时候回调 */
	public void notifyDownloadProgressed(DownloadInfo info) {
		synchronized (mObservers) {
			for (DownloadObserver observer : mObservers) {
				observer.onDownloadProgressed(info);
			}
		}
	}

	/** 下载，需要传入一个appInfo对象 */
	public synchronized void download(AppInfo appInfo) {
		// 先判断是否有这个app的下载信息
		DownloadInfo info = mDownloadMap.get(appInfo.getId());
		if (info == null) {// 如果没有，则根据appInfo创建一个新的下载信息
			info = DownloadInfo.clone(appInfo);
			mDownloadMap.put(appInfo.getId(), info);
		}
		// 判断状态是否为STATE_NONE、STATE_PAUSED、STATE_ERROR。只有这3种状态才能进行下载，其他状态不予处理
		if (info.getDownloadState() == STATE_NONE
				|| info.getDownloadState() == STATE_PAUSED
				|| info.getDownloadState() == STATE_ERROR) {
			// 下载之前，把状态设置为STATE_WAITING，因为此时并没有产开始下载，只是把任务放入了线程池中，当任务真正开始执行时，才会改为STATE_DOWNLOADING
			info.setDownloadState(STATE_WAITING);
			notifyDownloadStateChanged(info);// 每次状态发生改变，都需要回调该方法通知所有观察者
			DownloadTask task = new DownloadTask(info);// 创建一个下载任务，放入线程池
			mTaskMap.put(info.getId(), task);
			ThreadManager.getDownloadPool().execute(task);
		}
	}

	/** 暂停下载 */
	public synchronized void pause(AppInfo appInfo) {
		stopDownload(appInfo);
		DownloadInfo info = mDownloadMap.get(appInfo.getId());// 找出下载信息
		if (info != null) {// 修改下载状态
			info.setDownloadState(STATE_PAUSED);
			notifyDownloadStateChanged(info);
		}
	}

	/** 取消下载，逻辑和暂停类似，只是需要删除已下载的文件 */
	public synchronized void cancel(AppInfo appInfo) {
		stopDownload(appInfo);
		DownloadInfo info = mDownloadMap.get(appInfo.getId());// 找出下载信息
		if (info != null) {// 修改下载状态并删除文件
			info.setDownloadState(STATE_NONE);
			notifyDownloadStateChanged(info);
			info.setCurrentSize(0);
			File file = new File(info.getPath());
			file.delete();
		}
	}

	/** 安装应用 */
	public synchronized void install(AppInfo appInfo) {
		stopDownload(appInfo);
		DownloadInfo info = mDownloadMap.get(appInfo.getId());// 找出下载信息
		if (info != null) {// 发送安装的意图
			Intent installIntent = new Intent(Intent.ACTION_VIEW);
			installIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			installIntent.setDataAndType(Uri.parse("file://" + info.getPath()),
					"application/vnd.android.package-archive");
			UIUtils.getContext().startActivity(installIntent);
		}
		notifyDownloadStateChanged(info);
	}

	/** 启动应用，启动应用是最后一个 */
	public synchronized void open(AppInfo appInfo) {
		try {
			Context context = UIUtils.getContext();
			// 获取启动Intent
			Intent intent = context.getPackageManager()
					.getLaunchIntentForPackage(appInfo.getPackageName());
			context.startActivity(intent);
		} catch (Exception e) {
			LogUtils.e(e);
		}
	}

	/** 如果该下载任务还处于线程池中，且没有执行，先从线程池中移除 */
	private void stopDownload(AppInfo appInfo) {
		DownloadTask task = mTaskMap.remove(appInfo.getId());// 先从集合中找出下载任务
		if (task != null) {
			ThreadManager.getDownloadPool().cancel(task);// 然后从线程池中移除
		}
	}

	/** 获取下载信息 */
	public synchronized DownloadInfo getDownloadInfo(long id) {
		return mDownloadMap.get(id);
	}

	/** 下载任务 */
	public class DownloadTask implements Runnable {
		private DownloadInfo info;

		public DownloadTask(DownloadInfo info) {
			this.info = info;
		}

		@Override
		public void run() {
			info.setDownloadState(STATE_DOWNLOADING);// 先改变下载状态
			notifyDownloadStateChanged(info);
			File file = new File(info.getPath());// 获取下载文件
			HttpResult httpResult = null;
			InputStream stream = null;
			if (info.getCurrentSize() == 0 || !file.exists()
					|| file.length() != info.getCurrentSize()) {
				// 如果文件不存在，或者进度为0，或者进度和文件长度不相符，就需要重新下载
				info.setCurrentSize(0);
				file.delete();
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + info.getUrl());
			} else {
				// 文件存在且长度和进度相等，采用断点下载
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + info.getUrl() + "&range="
						+ info.getCurrentSize());
			}
			if (httpResult == null
					|| (stream = httpResult.getInputStream()) == null) {
				info.setDownloadState(STATE_ERROR);// 没有下载内容返回，修改为错误状态
				notifyDownloadStateChanged(info);
			} else {
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file, true);
					int count = -1;
					byte[] buffer = new byte[1024];
					while (((count = stream.read(buffer)) != -1)
							&& info.getDownloadState() == STATE_DOWNLOADING) {
						// 每次读取到数据后，都需要判断是否为下载状态，如果不是，下载需要终止，如果是，则刷新进度
						fos.write(buffer, 0, count);
						fos.flush();
						info.setCurrentSize(info.getCurrentSize() + count);
						notifyDownloadProgressed(info);// 刷新进度
					}
				} catch (Exception e) {
					LogUtils.e(e);// 出异常后需要修改状态并删除文件
					info.setDownloadState(STATE_ERROR);
					notifyDownloadStateChanged(info);
					info.setCurrentSize(0);
					file.delete();
				} finally {
					IOUtils.close(fos);
					if (httpResult != null) {
						httpResult.close();
					}
				}
				// 判断进度是否和app总长度相等
				if (info.getCurrentSize() == info.getAppSize()) {
					info.setDownloadState(STATE_DOWNLOADED);
					notifyDownloadStateChanged(info);
				} else if (info.getDownloadState() == STATE_PAUSED) {// 判断状态
					notifyDownloadStateChanged(info);
				} else {
					info.setDownloadState(STATE_ERROR);
					notifyDownloadStateChanged(info);
					info.setCurrentSize(0);// 错误状态需要删除文件
					file.delete();
				}
			}
			mTaskMap.remove(info.getId());
		}
	}

	public interface DownloadObserver {

		public void onDownloadStateChanged(DownloadInfo info);

		public void onDownloadProgressed(DownloadInfo info);
	}
}
