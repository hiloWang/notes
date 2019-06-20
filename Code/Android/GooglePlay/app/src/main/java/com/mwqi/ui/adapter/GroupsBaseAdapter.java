package com.mwqi.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import com.mwqi.bean.CategoryInfo;
import com.mwqi.ui.holder.BaseHolder;

import java.util.List;

public abstract class GroupsBaseAdapter extends DefaultAdapter<CategoryInfo> {
	private int mCurrentPosition;

	public GroupsBaseAdapter(AbsListView listView, List<CategoryInfo> datas) {
		super(listView, datas);
	}

	//是告诉listView总共有几种样式的item
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;
	}

	//告诉ListView每个位置是哪种样式的item
	@Override
	public int getItemViewTypeInner(int position) {
		CategoryInfo groupInfo = getData().get(position);
		if (groupInfo.isTitle()) {
			return super.getItemViewTypeInner(position) + 1;
		} else {
			return super.getItemViewTypeInner(position);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		mCurrentPosition = position;
		return super.getView(position, convertView, parent);
	}

	public BaseHolder<CategoryInfo> getHolder() {
		CategoryInfo groupInfo = getData().get(mCurrentPosition);
		if (groupInfo.isTitle()) {
			return getTitleHolder();
		} else {
			return getItemHolder();
		}
	}

	protected abstract BaseHolder<CategoryInfo> getTitleHolder();

	protected abstract BaseHolder<CategoryInfo> getItemHolder();
}
