package com.itheima.mobileguard.activities;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.db.dao.BlackNumberDao;
import com.itheima.mobileguard.domain.BlackInfo;

public class CallSafeActivity extends Activity {
	private ListView lv_callsms_safe;// 显示数据的ListView
	private MyBaseAdapter adapter;// 适配器
	private List<BlackInfo> infos;// 存储黑名单
	private BlackNumberDao dao;// 黑名单查找
	private LinearLayout ll_add_number_tips;// 没有数据时的提示
	private LinearLayout ll_loading;// 读取数据进度条
	private int maxSize = 20;// 最多加载多少条数据
	private int startIndex = 0;// 加载数据的起始位置
	private MyOnScrollListener listener;// ListView滑动监听器
	private int totalRecord;// 总得记录数量

	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();// 界面初始化
		fillData(startIndex, maxSize);// 数据初始化
	}

	// 添加一条黑名单
	public void add(View v) {
		AlertDialog.Builder builder = new Builder(this);
		final View view = View
				.inflate(this, R.layout.dialog_callsafe_add, null);
		final AlertDialog dialog = builder.create();
		dialog.setView(view, 0, 0, 0, 0);// 距离上下左右的控件为
		Button bt_ok = (Button) view.findViewById(R.id.bt_callsafe_ok);
		Button bt_cancel = (Button) view.findViewById(R.id.bt_callsafe_cancel);
		bt_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditText et = (EditText) view
						.findViewById(R.id.et_callsafe_number);
				String number = et.getText().toString().trim();
				if (TextUtils.isEmpty(number)) {
					Toast.makeText(CallSafeActivity.this, "请输入号码",
							Toast.LENGTH_SHORT).show();
					return;
				}
				// 添加号码 获取模式
				CheckBox cb_phone = (CheckBox) view
						.findViewById(R.id.cb_callsafe_phone);
				CheckBox cb_sms = (CheckBox) view
						.findViewById(R.id.cb_callsafe_sms);
				String mode = "0";
				if (!cb_phone.isChecked() && !cb_sms.isChecked()) {
					Toast.makeText(CallSafeActivity.this, "请选择模式",
							Toast.LENGTH_LONG).show();
						return;
				} else if (cb_phone.isChecked() && cb_sms.isChecked()) {
					mode = "1";
				} else if (cb_phone.isChecked()) {
					mode = "3";
				} else if (cb_sms.isChecked()) {
					mode = "2";
				}
				if (dao.insert(number, mode)) {
					ll_add_number_tips.setVisibility(View.INVISIBLE);
					startIndex++;
				}
				// 把数据添加到集合中 显示出来
				BlackInfo info = new BlackInfo(number, mode);
				infos.add(0, info);
				// 通知适配器 数据发送了变化 要输刷新了
				if (adapter == null) {
					adapter = new MyBaseAdapter();
					adapter.notifyDataSetChanged();
				} else {
					adapter.notifyDataSetChanged();
				}
				dialog.dismiss();
			}
		});
		bt_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();

	}

	/**
	 * 消息处理器
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			if (infos.size() == 0) {
				ll_add_number_tips.setVisibility(View.VISIBLE);
			} else {
				if (adapter == null) {// 如果adapter == null 说明是第一次加载数据 就new出来
					adapter = new MyBaseAdapter();
					lv_callsms_safe.setAdapter(adapter);
				} else {// 加载到了新的数据就通知 数据更新了
					adapter.notifyDataSetChanged();// 通知数据已经更新了 应当刷新
				}
			}
		}
	};

	/**
	 * 显示数据数据
	 */
	private void fillData(final int startIndex, final int maxSize) {
		dao = new BlackNumberDao(CallSafeActivity.this);
		// 如果查询的数据很多的话 会花费一定的时间 所以应当在子线程中去查询
		ll_loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				totalRecord = dao.getTotalRecord();// 获取总的数据条数
				if (infos == null) {// 如果没有数据就直接赋值
					infos = dao.findpart2(startIndex, maxSize);
					handler.sendEmptyMessage(0);
				} else {// 如果有数据 就把数据添加到原来的集合中去
					// 数据已经查询到 发送消息到主线程消息队列 更新UI
					addNewDataAndSendMessage();
				}
			}
		}.start();
	}

	/**
	 * 发送消息 加上同步
	 */
	public void addNewDataAndSendMessage() {
		synchronized (infos) {
			infos.addAll(dao.findpart2(startIndex, maxSize));
			// 数据已经查询到 发送消息到主线程消息队列 更新UI
			handler.sendEmptyMessage(0);
		}
	}

	/**
	 * 组件和界面初始化
	 */
	private void init() {
		setContentView(R.layout.activity_callsafe);
		lv_callsms_safe = (ListView) findViewById(R.id.lv_callsms_safe);
		ll_add_number_tips = (LinearLayout) findViewById(R.id.ll_add_number_tips);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		listener = new MyOnScrollListener();
		lv_callsms_safe.setOnScrollListener(listener);

		lv_callsms_safe.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (infos != null && adapter != null) {
					final TextView tv = (TextView) view
					.findViewById(R.id.tv_callsafe_mode);
					final String number = infos.get(position).getNumber();
					AlertDialog dialog;
					AlertDialog.Builder builder = new Builder(
							CallSafeActivity.this);
					builder.setTitle("更改模式");
					String[] items = { "电话拦截", "短信拦截" };
					final boolean[] checkedItems = { false, false };
					builder.setMultiChoiceItems(items, checkedItems,
							new DialogInterface.OnMultiChoiceClickListener() {
								public void onClick(DialogInterface dialog,
										int which, boolean isChecked) {
									if (which == 0) {
										checkedItems[0] = isChecked;
									} else if (which == 1) {
										checkedItems[1] = isChecked;
									}
								}
							})
							.setNegativeButton("取消",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											dialog.dismiss();
										}
									})
							.setPositiveButton("确定",
									new DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											boolean phone = checkedItems[0];
											boolean sms = checkedItems[1];
											if (phone && sms) {
													if(dao.update(number, 1+"")){
														tv.setText("全部拦截");
														dialog.dismiss();
													} 
											}else if( phone){
												dao.update(number, 3+"");
												tv.setText("电话拦截");
												dialog.dismiss();
											}else if(sms){
												dao.update(number, 2+"");
												tv.setText("短信拦截");
												dialog.dismiss();
											}
										}
									});
					dialog = builder.show();
				}
			}
		});
	}

	/**
	 * ListView滑动监听器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyOnScrollListener implements OnScrollListener {
		/*
		 * OnScrollListener.SCROLL_STATE_IDLE 滚动条静止
		 * OnScrollListener.SCROLL_STATE_FLING 惯性滑行
		 * OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 滚动条触摸滑动
		 */
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// 滚动条静止的时候判断是否已经加载到当前数据的最后
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
				if (view.getLastVisiblePosition() >= totalRecord - 1) {
					Toast.makeText(CallSafeActivity.this, "没有更多的数据了",
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (view.getLastVisiblePosition() == infos.size() - 1) {// -1
																		// 因为listview的起始位置是0
					// 需要加载下一批数据
					startIndex += maxSize;
					// 更新数据
					fillData(startIndex, maxSize);
				}
			}
		}

		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
		}
	}

	/**
	 * 适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class MyBaseAdapter extends BaseAdapter {
		public int getCount() {
			return infos.size();
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		// 有多少个数据就会调用这个方法多少次
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if (convertView == null) {
				view = View.inflate(CallSafeActivity.this,
						R.layout.item_callsafe_listblacknumber, null);
				holder = new ViewHolder();
				holder.tv_mode = (TextView) view
						.findViewById(R.id.tv_callsafe_mode);
				holder.tv_number = (TextView) view
						.findViewById(R.id.tv_callsafe_number);
				holder.iv = (ImageView) view.findViewById(R.id.iv_callsafe);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			final String number = infos.get(position).getNumber();
			holder.tv_number.setText(number);
			String mode = infos.get(position).getMode();
			if ("1".equals(mode)) {
				holder.tv_mode.setText("全部拦截");
			} else if ("2".equals(mode)) {
				holder.tv_mode.setText("短信拦截");
			} else if ("3".equals(mode)) {
				holder.tv_mode.setText("电话拦截");
			}
			holder.iv.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					boolean result = dao.delete(number);
					if (result) {
						infos.remove(position);
						adapter.notifyDataSetChanged();
						if (infos.size() == 0) {
							ll_add_number_tips.setVisibility(View.VISIBLE);
						}
					}
				}
			});
			return view;
		}

		// findviewbyid 其实是比较浪费资源的 没显现一个条目这里就会执行两次，如果能有一个对象记住已经找到
		// 的View 就不需要总是执行这样的代码了 相对来说是比较节约资源的
		/**
		 * 家庭组 相当于View对象的容器
		 * 
		 * @author Administrator
		 * 
		 */
		private class ViewHolder {
			TextView tv_number;
			TextView tv_mode;
			ImageView iv;
		}
	}

}
