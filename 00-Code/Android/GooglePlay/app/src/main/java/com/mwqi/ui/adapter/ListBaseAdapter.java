package com.mwqi.ui.adapter;

import android.content.Intent;
import android.widget.AbsListView;
import com.mwqi.bean.AppInfo;
import com.mwqi.bean.DownloadInfo;
import com.mwqi.manager.DownloadManager;
import com.mwqi.ui.activity.BaseActivity;
import com.mwqi.ui.activity.DetailActivity;
import com.mwqi.ui.holder.BaseHolder;
import com.mwqi.ui.holder.ListBaseHolder;
import com.mwqi.utils.UIUtils;

import java.util.List;

/**
 * Created by mwqi on 2014/6/12.
 */
public abstract class ListBaseAdapter extends DefaultAdapter<AppInfo> implements DownloadManager.DownloadObserver {
	public ListBaseAdapter(AbsListView listView, List<AppInfo> datas) {
		super(listView, datas);
	}

	@Override
	protected BaseHolder getHolder() {
		return new ListBaseHolder();
	}

	public void startObserver() {
		DownloadManager.getInstance().registerObserver(this);
	}

	public void stopObserver() {
		DownloadManager.getInstance().unRegisterObserver(this);
	}

	@Override
	public void onDownloadStateChanged(final DownloadInfo info) {
		refreshHolder(info);
	}

	@Override
	public void onDownloadProgressed(DownloadInfo info) {
		refreshHolder(info);
	}

	private void refreshHolder(final DownloadInfo info) {
		List<BaseHolder> displayedHolders = getDisplayedHolders();
		for (int i = 0; i < displayedHolders.size(); i++) {
			BaseHolder baseHolder = displayedHolders.get(i);
			if (baseHolder instanceof ListBaseHolder) {
				final ListBaseHolder holder = (ListBaseHolder) baseHolder;
				AppInfo appInfo = holder.getData();
				if (appInfo.getId() == info.getId()) {
					UIUtils.post(new Runnable() {
						@Override
						public void run() {
							holder.refreshState(info.getDownloadState(), info.getProgress());
						}
					});
				}
			}
		}
	}

	@Override
	public void onItemClickInner(int position) {
		List<AppInfo> data = getData();
		if (position < data.size()) {
			Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
			AppInfo info = data.get(position);
			intent.putExtra(DetailActivity.PACKAGENAME, info.getPackageName());
			UIUtils.startActivity(intent);
		}
	}
}
