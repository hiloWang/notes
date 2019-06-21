package com.mwqi.ui.fragment;

import com.mwqi.ui.widget.LoadingPage;
import com.mwqi.ui.widget.LoadingPage.LoadResult;
import com.mwqi.utils.UIUtils;
import com.mwqi.utils.ViewUtils;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseFragment extends Fragment {
	protected LoadingPage mContentView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		//每次ViewPager要展示该页面时，均会调用该方法获取显示的View
		if (mContentView == null) {//为null时，创建一个
			mContentView = new LoadingPage(UIUtils.getContext()) {
				@Override
				public LoadResult load() {
					return BaseFragment.this.load();
				}

				@Override
				public View createLoadedView() {
					return BaseFragment.this.createLoadedView();
				}
			};
		} else {//不为null时，需要把自身从父布局中移除，因为ViewPager会再次添加
			ViewUtils.removeSelfFromParent(mContentView);
		}
		return mContentView;
	}

	/** 当显示的时候，加载该页面 */
	public void show() {
		if (mContentView != null) {
			mContentView.show();
		}
	}

	public LoadResult check(Object obj) {
		if (obj == null) {
			return LoadResult.ERROR;
		}
		if (obj instanceof List) {
			List list = (List) obj;
			if (list.size() == 0) {
				return LoadResult.EMPTY;
			}
		}
		return LoadResult.SUCCEED;
	}

	/** 加载数据 */
	protected abstract LoadResult load();

	/** 加载完成的View */
	protected abstract View createLoadedView();
}
