package com.itheima.mobileguard.fragments;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.db.dao.LockedAppDao;
import com.itheima.mobileguard.domain.AppInfo;
import com.itheima.mobileguard.engine.AppInfoParser;

import java.util.ArrayList;
import java.util.List;


public class UnlockAppFragment extends Fragment {
	private ContentResolver resolver;//用来通知看门狗 数据发生变化
	private Uri uri;
	private ListView lv_lock_app;//ListView 用来存储数据
	private List<AppInfo> unLockApps;//存储 没有加锁APP
	private LockedAppDao dao;// 加了锁的app ，包名存入数据库 ，用这个类查询是否加锁
	private UnlockAppAdapter adapter;
	private TextView tv_unlockedapp_count;//没加锁的APP数
	private Animation a;//向右滑动动画
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_unlock_app, null);
		a = AnimationUtils.loadAnimation(getActivity(), R.anim.to_right_locked);
		lv_lock_app = (ListView) view.findViewById(R.id.lv_unlock_app);
		tv_unlockedapp_count = (TextView) view
				.findViewById(R.id.tv_unlocked_count);
		dao = new LockedAppDao(getActivity());
		unLockApps = new ArrayList<AppInfo>();
		resolver = getActivity().getContentResolver();
		uri  = Uri.parse("content://com.itheima.mobile.change");
		return view;
	}
	/**
	 * 填充数据放在onstart中,利用appInfoParser获取全部的appInfo 判断是否加锁了 加锁了就不显示，加锁的程序存入db中
	 */
	public void onStart() {
		List<AppInfo> infos = AppInfoParser.getAllAppinfo(getActivity());
		// 过滤掉加锁程序
		unLockApps.clear();
		for (AppInfo info : infos) {
			if (dao.isLocked(info.getPackageName())) {

			} else {// 只取没加锁的
				unLockApps.add(info);
			}
		}
		if (adapter == null) {
			adapter = new UnlockAppAdapter();
		} else {
			adapter.notifyDataSetChanged();
		}
		// 更新UI
		lv_lock_app.setAdapter(adapter);
		super.onStart();
	}

	private class UnlockAppAdapter extends BaseAdapter {
		public int getCount() {
			tv_unlockedapp_count.setText("未加锁程序:" + unLockApps.size());
			return unLockApps.size();
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final View view;
			ViewHolder holder = null;
			if (convertView != null) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				holder = new ViewHolder();
				view = View.inflate(getActivity(), R.layout.item_appunlock,
						null);
				holder.iv = (ImageView) view.findViewById(R.id.iv_appinfo_icon);
				holder.iv_lock = (ImageView) view
						.findViewById(R.id.iv_app_unlock);
				holder.tv = (TextView) view.findViewById(R.id.tv_appname);
				view.setTag(holder);
			}
			holder.tv.setText(unLockApps.get(position).getAppName());
			holder.iv.setImageDrawable(unLockApps.get(position).getAppIcon());
			// 给锁添加点击监听事件
			holder.iv_lock.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					TranslateAnimation ta = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1.0f,
							Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
					ta.setDuration(300);
					view.startAnimation(a);
					new Thread() {
						public void run() {
							try {
								Thread.sleep(200);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									dao.add(unLockApps.get(position)
											.getPackageName());
									unLockApps.remove(position);// 删除条目
									resolver.notifyChange(uri, null);
									adapter.notifyDataSetChanged();// 更新UI
								}
							});
						}
					}.start();
				}
			});
			return view;
		}
	}
	static class ViewHolder {
		ImageView iv;
		TextView tv;
		ImageView iv_lock;
	}
}
