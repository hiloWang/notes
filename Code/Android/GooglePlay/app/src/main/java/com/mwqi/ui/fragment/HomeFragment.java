package com.mwqi.ui.fragment;

import com.mwqi.bean.AppInfo;
import com.mwqi.http.protocol.GameProtocol;
import com.mwqi.http.protocol.HomeProtocol;
import com.mwqi.ui.adapter.ListBaseAdapter;
import com.mwqi.ui.holder.HomePictureHolder;
import com.mwqi.ui.holder.MoreHolder;
import com.mwqi.ui.widget.InterceptorFrame;
import com.mwqi.ui.widget.LoadingPage.LoadResult;

import android.view.View;
import android.widget.AbsListView;

import com.mwqi.ui.widget.BaseListView;
import com.mwqi.utils.UIUtils;

import java.util.List;

public class HomeFragment extends BaseFragment {
	private HomePictureHolder mHolder;
	private List<String> mPicture;
	private BaseListView mListView;
	private HomeAdapter mAdapter;
	private List<AppInfo> mDatas;

	@Override
	protected LoadResult load() {
		HomeProtocol protocol = new HomeProtocol();
		mDatas = protocol.load(0);
		mPicture = protocol.getPictureUrl();
		return check(mDatas);
	}

	/** 可见时，需要启动监听，以便随时根据下载状态刷新页面 */
	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null) {
			mAdapter.startObserver();
			mAdapter.notifyDataSetChanged();
		}
	}

	/** 不可见时，需要关闭监听 */
	@Override
	public void onPause() {
		super.onPause();
		if (mAdapter != null) {
			mAdapter.stopObserver();
		}
	}

	@Override
	protected View createLoadedView() {
		mListView = new BaseListView(UIUtils.getContext());
		if (mPicture != null && mPicture.size() > 0) {
			mHolder = new HomePictureHolder();
			mHolder.setData(mPicture);
			mListView.addHeaderView(mHolder.getRootView());
		}

		mAdapter = new HomeAdapter(mListView, mDatas);
	
		mListView.setAdapter(mAdapter);

		if (mHolder != null) {
			InterceptorFrame frame = new InterceptorFrame(UIUtils.getContext());
			frame.addInterceptorView(mHolder.getRootView(), InterceptorFrame.ORIENTATION_LEFT | InterceptorFrame.ORIENTATION_RIGHT);
			frame.addView(mListView);
			return frame;
		} else {
			return mListView;
		}
	}

	class HomeAdapter extends ListBaseAdapter {

		public HomeAdapter(AbsListView listView, List<AppInfo> datas) {
			super(listView, datas);
		}

		/** 加载更多 */
		@Override
		public List<AppInfo> onLoadMore() {
			HomeProtocol protocol = new HomeProtocol();
			return protocol.load(getData().size());
		}
	}
}
