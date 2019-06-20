package com.mwqi.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.mwqi.R;
import com.mwqi.bean.AppInfo;
import com.mwqi.bean.DownloadInfo;
import com.mwqi.manager.DownloadManager;
import com.mwqi.ui.widget.ProgressHorizontal;
import com.mwqi.utils.UIUtils;

/**
 * Created by mwqi on 2014/6/7.
 */
public class AppDetailBottomHolder extends BaseHolder<AppInfo> implements OnClickListener, DownloadManager.DownloadObserver {
	private Button mBtnFavorites, mBtnShare, mBtnProgress;
	private FrameLayout mLayout;
	private ProgressHorizontal mProgeressView;
	private float mProgress;
	private DownloadManager mDownloadManager;
	private int mState;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.app_detail_bottom);
		mBtnFavorites = (Button) view.findViewById(R.id.bottom_favorites);
		mBtnShare = (Button) view.findViewById(R.id.bottom_share);
		mBtnProgress = (Button) view.findViewById(R.id.progress_btn);
		mBtnFavorites.setOnClickListener(this);
		mBtnShare.setOnClickListener(this);
		mBtnProgress.setOnClickListener(this);
		mBtnFavorites.setText(R.string.bottom_favorites);
		mBtnShare.setText(R.string.bottom_share);

		mLayout = (FrameLayout) view.findViewById(R.id.progress_layout);
		mProgeressView = new ProgressHorizontal(UIUtils.getContext());
		mProgeressView.setId(R.id.detail_progress);
		mProgeressView.setOnClickListener(this);
		mProgeressView.setProgressTextVisible(true);
		mProgeressView.setProgressTextColor(Color.WHITE);
		mProgeressView.setProgressTextSize(UIUtils.dip2px(18));
		mProgeressView.setBackgroundResource(R.drawable.progress_bg);
		mProgeressView.setProgressResource(R.drawable.progress);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mLayout.addView(mProgeressView, params);
		return view;
	}

	@Override
	public void setData(AppInfo data) {
		if (mDownloadManager == null) {
			mDownloadManager = DownloadManager.getInstance();
		}
		DownloadInfo downloadInfo = mDownloadManager.getDownloadInfo(data.getId());
		if (downloadInfo != null) {
			mState = downloadInfo.getDownloadState();
			mProgress = downloadInfo.getProgress();
		} else {
			mState = DownloadManager.STATE_NONE;
			mProgress = 0;
		}
		super.setData(data);
	}

	@Override
	public void refreshView() {
		refreshState(mState, mProgress);
	}

	public void refreshState(int state, float progress) {
		mState = state;
		mProgress = progress;
		switch (mState) {
			case DownloadManager.STATE_NONE:
				mProgeressView.setVisibility(View.GONE);
				mBtnProgress.setVisibility(View.VISIBLE);
				mBtnProgress.setText(UIUtils.getString(R.string.app_state_download));
				break;
			case DownloadManager.STATE_PAUSED:
				mProgeressView.setVisibility(View.VISIBLE);
				mProgeressView.setProgress(progress);
				mProgeressView.setCenterText(UIUtils.getString(R.string.app_state_paused));
				mBtnProgress.setVisibility(View.GONE);
				break;
			case DownloadManager.STATE_ERROR:
				mProgeressView.setVisibility(View.GONE);
				mBtnProgress.setVisibility(View.VISIBLE);
				mBtnProgress.setText(R.string.app_state_error);
				break;
			case DownloadManager.STATE_WAITING:
				mProgeressView.setVisibility(View.VISIBLE);
				mProgeressView.setProgress(progress);
				mProgeressView.setCenterText(UIUtils.getString(R.string.app_state_waiting));
				mBtnProgress.setVisibility(View.GONE);
				break;
			case DownloadManager.STATE_DOWNLOADING:
				mProgeressView.setVisibility(View.VISIBLE);
				mProgeressView.setProgress(progress);
				mProgeressView.setCenterText("");
				mBtnProgress.setVisibility(View.GONE);
				break;
			case DownloadManager.STATE_DOWNLOADED:
				mProgeressView.setVisibility(View.GONE);
				mBtnProgress.setVisibility(View.VISIBLE);
				mBtnProgress.setText(R.string.app_state_downloaded);
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bottom_favorites:
				UIUtils.showToastSafe(R.string.bottom_favorites);
				break;
			case R.id.bottom_share:
				UIUtils.showToastSafe(R.string.bottom_share);
				break;
			case R.id.progress_btn:
			case R.id.detail_progress:
				if (mState == DownloadManager.STATE_NONE || mState == DownloadManager.STATE_PAUSED || mState == DownloadManager.STATE_ERROR) {
					mDownloadManager.download(getData());
				} else if (mState == DownloadManager.STATE_WAITING || mState == DownloadManager.STATE_DOWNLOADING) {
					mDownloadManager.pause(getData());
				} else if (mState == DownloadManager.STATE_DOWNLOADED) {
					mDownloadManager.install(getData());
				}
				break;
			default:
				break;
		}
	}

	public void startObserver() {
		mDownloadManager.registerObserver(this);
	}

	public void stopObserver() {
		mDownloadManager.unRegisterObserver(this);
	}

	@Override
	public void onDownloadStateChanged(DownloadInfo info) {
		refreshHolder(info);
	}

	@Override
	public void onDownloadProgressed(DownloadInfo info) {
		refreshHolder(info);
	}

	private void refreshHolder(final DownloadInfo info) {
		AppInfo appInfo = getData();
		if (appInfo.getId() == info.getId()) {
			UIUtils.runInMainThread(new Runnable() {
				@Override
				public void run() {
					refreshState(info.getDownloadState(), info.getProgress());
				}
			});
		}
	}
}
