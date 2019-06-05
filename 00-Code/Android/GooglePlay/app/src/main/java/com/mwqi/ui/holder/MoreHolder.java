package com.mwqi.ui.holder;

import java.util.concurrent.atomic.AtomicBoolean;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import com.mwqi.R;
import com.mwqi.manager.ThreadManager;
import com.mwqi.ui.adapter.DefaultAdapter;
import com.mwqi.utils.UIUtils;

public class MoreHolder extends BaseHolder<Integer> implements OnClickListener {
	public static final int HAS_MORE = 0;
	public static final int NO_MORE = 1;
	public static final int ERROR = 2;

	private RelativeLayout mLoading, mError;
	private DefaultAdapter mAdapter;

	public MoreHolder(DefaultAdapter adapter, boolean hasMore) {
		setData(hasMore ? HAS_MORE : NO_MORE);
		mAdapter = adapter;
	}

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.list_more_loading);
		mLoading = (RelativeLayout) view.findViewById(R.id.rl_more_loading);
		mError = (RelativeLayout) view.findViewById(R.id.rl_more_error);
		mError.setOnClickListener(this);
		return view;
	}

	@Override
	public void refreshView() {
		Integer data = getData();
		mLoading.setVisibility(data == HAS_MORE ? View.VISIBLE : View.GONE);
		mError.setVisibility(data == ERROR ? View.VISIBLE : View.GONE);
	}

	@Override
	public View getRootView() {
		if (getData() == HAS_MORE) {
			loadMore();
		}
		return super.getRootView();
	}

	public MoreHolder setAdapter(DefaultAdapter adapter) {
		mAdapter = adapter;
		return this;
	}

	@Override
	public void onClick(View v) {
		loadMore();
	}

	public void loadMore() {
		mAdapter.loadMore();
	}
}
