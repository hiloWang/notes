package com.mwqi.ui.holder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.mwqi.R;
import com.mwqi.bean.AppInfo;
import com.mwqi.bean.DownloadInfo;
import com.mwqi.http.image.ImageLoader;
import com.mwqi.manager.DownloadManager;
import com.mwqi.ui.activity.BaseActivity;
import com.mwqi.ui.widget.ProgressArc;
import com.mwqi.utils.StringUtils;
import com.mwqi.utils.UIUtils;

/**
 * Created by mwqi on 2014/6/7.
 */
public class ListBaseHolder extends BaseHolder<AppInfo> implements View.OnClickListener {

	private ImageView icon;
	private TextView tvTitle, tvSize, tvDes;
	private RatingBar rb;
	private RelativeLayout mActionLayout;
	private FrameLayout mProgressLayout;
	private ProgressArc mProgressArc;
	private TextView mActionText;
	private float mProgress;
	private DownloadManager mDownloadManager;
	private int mState;

	@Override
	public void recycle() {
		recycleImageView(icon);
	}

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.list_item);
		icon = (ImageView) view.findViewById(R.id.item_icon);
		tvTitle = (TextView) view.findViewById(R.id.item_title);
		tvSize = (TextView) view.findViewById(R.id.item_size);
		tvDes = (TextView) view.findViewById(R.id.item_bottom);
		rb = (RatingBar) view.findViewById(R.id.item_rating);

		mActionLayout = (RelativeLayout) view.findViewById(R.id.item_action);
		mActionLayout.setOnClickListener(this);
		mActionLayout.setBackgroundResource(R.drawable.list_item_action_bg);
		// mActionLayout.setDuplicateParentStateEnabled(true);

		mProgressLayout = (FrameLayout) view.findViewById(R.id.action_progress);
		mProgressArc = new ProgressArc(UIUtils.getContext());
		int arcDiameter = UIUtils.dip2px(26);
		//设置圆的直径
		mProgressArc.setArcDiameter(arcDiameter);
		//设置进度条的颜色
		mProgressArc.setProgressColor(UIUtils.getColor(R.color.progress));
		int size = UIUtils.dip2px(27);
        //进度条的宽高
		mProgressLayout.addView(mProgressArc, new ViewGroup.LayoutParams(size, size));
		mActionText = (TextView) view.findViewById(R.id.action_txt);
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
		AppInfo info = getData();
		tvTitle.setText(info.getName());
		tvSize.setText(StringUtils.formatFileSize(info.getSize()));
		tvDes.setText(info.getDes());
		rb.setRating(info.getStars());

		String url = info.getIconUrl();
		icon.setTag(url);
		ImageLoader.load(icon, url);

		refreshState(mState, mProgress);
	}

	public void refreshState(int state, float progress) {
		mState = state;
		mProgress = progress;
		switch (mState) {
		case DownloadManager.STATE_NONE:
			mProgressArc.seForegroundResource(R.drawable.ic_download);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mActionText.setText(R.string.app_state_download);
			break;
		case DownloadManager.STATE_PAUSED:
			mProgressArc.seForegroundResource(R.drawable.ic_resume);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mActionText.setText(R.string.app_state_paused);
			break;
		case DownloadManager.STATE_ERROR:
			mProgressArc.seForegroundResource(R.drawable.ic_redownload);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mActionText.setText(R.string.app_state_error);
			break;
		case DownloadManager.STATE_WAITING:
			mProgressArc.seForegroundResource(R.drawable.ic_pause);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);
			mProgressArc.setProgress(progress, false);
			mActionText.setText(R.string.app_state_waiting);
			break;
		case DownloadManager.STATE_DOWNLOADING:
			mProgressArc.seForegroundResource(R.drawable.ic_pause);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);
			mProgressArc.setProgress(progress, true);
			mActionText.setText((int) (mProgress * 100) + "%");
			break;
		case DownloadManager.STATE_DOWNLOADED:
			mProgressArc.seForegroundResource(R.drawable.ic_install);
			mProgressArc.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mActionText.setText(R.string.app_state_downloaded);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.item_action) {
			if (mState == DownloadManager.STATE_NONE || mState == DownloadManager.STATE_PAUSED
					|| mState == DownloadManager.STATE_ERROR) {
				mDownloadManager.download(getData());
			} else if (mState == DownloadManager.STATE_WAITING || mState == DownloadManager.STATE_DOWNLOADING) {
				mDownloadManager.pause(getData());
			} else if (mState == DownloadManager.STATE_DOWNLOADED) {
				mDownloadManager.install(getData());
			}
		}
	}
}
