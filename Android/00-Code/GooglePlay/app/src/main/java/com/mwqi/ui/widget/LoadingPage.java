package com.mwqi.ui.widget;

import android.content.Context;
import com.mwqi.R;
import com.mwqi.manager.ThreadManager;
import com.mwqi.utils.UIUtils;
import com.mwqi.utils.ViewUtils;

import android.view.View;
import android.widget.FrameLayout;

public abstract class LoadingPage extends FrameLayout {
	private static final int STATE_UNLOADED = 0;//未知状态
	private static final int STATE_LOADING = 1;//加载状态
	private static final int STATE_ERROR = 3;//加载完毕，但是出错状态
	private static final int STATE_EMPTY = 4;//加载完毕，但是没有数据状态
	private static final int STATE_SUCCEED = 5;//加载成功

	private View mLoadingView;//加载时显示的View
	private View mErrorView;//加载出错显示的View
	private View mEmptyView;//加载没有数据显示的View
	private View mSucceedView;//加载成功显示的View

	private int mState;//当前的状态，显示需要根据该状态判断

	public LoadingPage(Context context) {
		super(context);
		init();
	}

	private void init() {
		setBackgroundColor(UIUtils.getColor(R.color.bg_page));//设置背景
		mState = STATE_UNLOADED;//初始化状态

		//创建对应的View，并添加到布局中
		mLoadingView = createLoadingView();
		if (null != mLoadingView) {
			addView(mLoadingView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		mErrorView = createErrorView();
		if (null != mErrorView) {
			addView(mErrorView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		mEmptyView = createEmptyView();
		if (null != mEmptyView) {
			addView(mEmptyView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}
		//显示对应的View
		showPageSafe();
	}

	/** 线程安全的方法 */
	private void showPageSafe() {
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				showPage();
			}
		});
	}

	/** 显示对应的View */
	private void showPage() {
		if (null != mLoadingView) {
			mLoadingView.setVisibility(mState == STATE_UNLOADED || mState == STATE_LOADING ? View.VISIBLE : View.INVISIBLE);
		}
		if (null != mErrorView) {
			mErrorView.setVisibility(mState == STATE_ERROR ? View.VISIBLE : View.INVISIBLE);
		}
		if (null != mEmptyView) {
			mEmptyView.setVisibility(mState == STATE_EMPTY ? View.VISIBLE : View.INVISIBLE);
		}

		// 只有数据成功返回了，才知道成功的View该如何显示，因为该View的显示依赖加载完毕的数据
		if (mState == STATE_SUCCEED && mSucceedView == null) {
			mSucceedView = createLoadedView();
			addView(mSucceedView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		}

		if (null != mSucceedView) {
			mSucceedView.setVisibility(mState == STATE_SUCCEED ? View.VISIBLE : View.INVISIBLE);
		}
	}

	/** 恢复状态 */
	public void reset() {
		mState = STATE_UNLOADED;
		showPageSafe();
	}

	/** 是否需要恢复状态 */
	protected boolean needReset() {
		return mState == STATE_ERROR || mState == STATE_EMPTY;
	}

	/** 有外部调用，会根据状态判断是否引发load */
	public synchronized void show() {
		if (needReset()) {
			mState = STATE_UNLOADED;
		}
		if (mState == STATE_UNLOADED) {
			mState = STATE_LOADING;
			LoadingTask task = new LoadingTask();
			ThreadManager.getLongPool().execute(task);
		}
		showPageSafe();
	}

	protected View createLoadingView() {
		return UIUtils.inflate(R.layout.loading_page_loading);
	}

	protected View createEmptyView() {
		return UIUtils.inflate(R.layout.loading_page_empty);
	}

	protected View createErrorView() {
		View view = UIUtils.inflate(R.layout.loading_page_error);
		view.findViewById(R.id.page_bt).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				show();
			}
		});
		return view;
	}

	public abstract View createLoadedView();

	public abstract LoadResult load();

	class LoadingTask implements Runnable {
		@Override
		public void run() {
			final LoadResult loadResult = load();
			UIUtils.runInMainThread(new Runnable() {//主线程改变UI
				@Override
				public void run() {
					//状态的改变和界面息息相关，所以需要放到主线程来赋值，保障同步性
					mState = loadResult.getValue();//根据枚举对象的返回值来改变显示状态码
					showPage();
				}
			});
		}
	}

	public enum LoadResult {
		ERROR(3), EMPTY(4), SUCCEED(5);
		int value;

		LoadResult(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}
	}
}
