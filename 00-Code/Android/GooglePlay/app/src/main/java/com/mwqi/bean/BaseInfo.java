package com.mwqi.bean;

import com.mwqi.ui.holder.BaseHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwqi on 2014/7/15.
 * Info的父类，具备和holder进行绑定的功能，当子类继承时，需要在所有的set方法中调用onDataChanged，
 * 当holder的setData方法调用时，需要把holder和原数据解除绑定，和新数据进行绑定
 * 这样的好处是以后只需要改变数据就能起到刷新界面的作用，但是需要程序员在界面不显示或者回收的时候把holder和info对象进行解除绑定，
 * 因为通常info对象的生命周期要长于holder，所以info需要释放掉holder的引用，防止内存泄漏。
 */
public class BaseInfo {

	private List<BaseHolder<BaseInfo>> holders = new ArrayList<BaseHolder<BaseInfo>>();

	/** 绑定holder */
	public void bindHolder(BaseHolder<BaseInfo> holder) {
		synchronized (holders) {
			if (!holders.contains(holder)) {
				holders.add(holder);
			}
		}
	}

	/** 解除绑定 */
	public void unbindHolder(BaseHolder<BaseInfo> holder) {
		synchronized (holders) {
			if (holders.contains(holder)) {
				holders.remove(holder);
			}
		}
	}

	/** 数据改变时调用，用于刷新holder */
	protected void onDataChanged() {
		synchronized (holders) {
			for (BaseHolder<BaseInfo> holder : holders) {
				holder.refreshView();
			}
		}
	}
}
