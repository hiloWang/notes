package com.itheima.mobileguard.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.domain.ProcessInfo;
import com.itheima.mobileguard.engine.ProcessInfoParser;
import com.itheima.mobileguard.utils.SystemInfoUtils;

public class ProcessManagerActivity extends Activity {

	private SharedPreferences sp;
	private TextView tv_processmanager_memory;//进程再用的内存
	private TextView tv_processmanager_runing;//当前运行的进程数
	private ListView lv_processinfo;//ListView显示数据
	private LinearLayout ll_processmanamger_loading;//加载动画
	private ProcessAdapter adapter;
	private ActivityManager am;
	private List<ProcessInfo> totalInfos;
	private List<ProcessInfo> userInfos;
	private List<ProcessInfo> systemInfos;
	private String totalMemory;
	private int runnintProrcess;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_managedr);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		tv_processmanager_memory = (TextView) findViewById(R.id.tv_processmanager_memory);
		tv_processmanager_runing = (TextView) findViewById(R.id.tv_processmanager_runing);
		lv_processinfo = (ListView) findViewById(R.id.lv_processinfo);
		ll_processmanamger_loading = (LinearLayout) findViewById(R.id.ll_processmanamger_loading);

		totalMemory = Formatter.formatFileSize(this,
				SystemInfoUtils.getTotalMemory(this));
		runnintProrcess = SystemInfoUtils.getRunningProcessCount(this);
		tv_processmanager_runing.setText("运行中进程:"
				+ runnintProrcess);

		tv_processmanager_memory.setText("可用内存/总内存:"
				+ Formatter.formatFileSize(this,
						SystemInfoUtils.getAvailableMemory(this)) + "/"
				+ totalMemory);
		fillData();// 加载数据  放在onstart中

		/**
		 * 条目点击事件
		 */
		lv_processinfo.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object obj = parent.getItemAtPosition(position);
				if (obj != null) {
					ProcessInfo info = (ProcessInfo) obj;
					ViewHolder holder = (ViewHolder) view.getTag();
					if (info.isChecked()) {
						holder.cb.setChecked(false);
						info.setChecked(false);
					} else {
						holder.cb.setChecked(true);
						info.setChecked(true);
					}
					if (adapter != null) {
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
	}
/**
 * 加载数据
 */
	@Override
	protected void onStart() {
	
	 if(adapter != null){
			adapter.notifyDataSetChanged();
		}
		super.onStart();
	}
	
	/**
	 * 加载数据
	 */
	private void fillData() {
		ll_processmanamger_loading.setVisibility(View.VISIBLE);
		ll_processmanamger_loading.startAnimation(AnimationUtils.loadAnimation(
				this, R.anim.loading));
		// 可能是耗时的操作
		new Thread() {
			public void run() {
				totalInfos = ProcessInfoParser
						.getRunningProcess(ProcessManagerActivity.this);
				systemInfos = new ArrayList<ProcessInfo>();
				userInfos = new ArrayList<ProcessInfo>();// 初始化集合
				for (ProcessInfo info : totalInfos) {// 分别存储
					if (info.isUserApp()) {
						userInfos.add(info);
					} else {
						systemInfos.add(info);
					}
					// 存储完毕可以更新UI了
					runOnUiThread(new Runnable() {
						public void run() {
							ll_processmanamger_loading
									.setVisibility(View.INVISIBLE);
							ll_processmanamger_loading.removeAllViews();
							adapter = new ProcessAdapter();
							lv_processinfo.setAdapter(adapter);
						}
					});
				}
			}
		}.start();
	}
	

/**
 * 进入设置界面
 * @param v
 */
	public void setting(View v){
		Intent intent = new Intent(this,ProcessManagerSettingActivity.class	);
		startActivity(intent);
	}
	/**
	 * 清理操作
	 * 
	 * @param view
	 */
	public void clear(View view) {
		/**
		 * 因为有些系统软件是不可以被杀死的，但是用户不会懂得这个，所以市面上的软件的做法就是欺骗用户，
		 * 并没有真的把所有进程都杀掉，也是不按照实际杀死的进程来显示数据，而是从集合中删除用户选择的
		 * 数据，达到一个提高用户体验的效果。需要注意的是，在遍历集中进行删除的时候，不要一边遍历，一边
		 * 对集合的元素进行修改，对造成并发修改异常，(当然也可以选择listIterator)可以选择一个第三方集合来
		 * 记住用户选择的进程，然后再来删除
		 */
		if (userInfos.size() == 1 && systemInfos.size() == 0) {
			Toast.makeText(this, "已达到最佳状态", Toast.LENGTH_LONG).show();
			return;
		}
		List<ProcessInfo> tempInfos = new ArrayList<ProcessInfo>();
		int count = 0;
		long clearedMem = 0;
		for (ProcessInfo info : userInfos) {
			if (info.getPackageName().equals(getPackageName())) {
				continue;
			}
			if (info.isChecked()) {
				tempInfos.add(info);
				count++;
				am.killBackgroundProcesses(info.getPackageName());
				clearedMem += info.getOccupyMemory();
			}
		}
		for (ProcessInfo info : systemInfos) {
			if (info.isChecked()) {
				tempInfos.add(info);
				am.killBackgroundProcesses(info.getPackageName());
				clearedMem += info.getOccupyMemory();
				count++;
			}
		}
		if (tempInfos.size() == 0) {
			Toast.makeText(this, "请选择需要杀死的进程", Toast.LENGTH_SHORT).show();
			return;
		}
		userInfos.removeAll(tempInfos);
		systemInfos.removeAll(tempInfos);
		adapter.notifyDataSetChanged();
		int i = runnintProrcess -= count;
		tv_processmanager_runing.setText("运行中进程:" + (i<1?1:i));

		tv_processmanager_memory
				.setText("可用内存/总内存:"
						+ Formatter.formatFileSize(this,
								(SystemInfoUtils.getAvailableMemory(this)+clearedMem)) + "/" + totalMemory);

		Toast.makeText(
				this,
				"杀死了" + count + "个进程,释放了"
						+ Formatter.formatFileSize(this, clearedMem) + "的内存",
				Toast.LENGTH_SHORT).show();
	}

	/**
	 * 全选操作
	 * 
	 * @param v
	 */
	public void selectAll(View v) {
		for (ProcessInfo info : userInfos) {
			info.setChecked(true);
		}
		for (ProcessInfo info : systemInfos) {
			info.setChecked(true);
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 反选操作
	 * 
	 * @param v
	 */
	public void oppositeSelect(View v) {
		for (ProcessInfo info : userInfos) {
			info.setChecked(!info.isChecked());
		}
		for (ProcessInfo info : systemInfos) {
			info.setChecked(!info.isChecked());
		}
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * 数据适配器
	 * 
	 * @author Administrator
	 * 
	 */
	private class ProcessAdapter extends BaseAdapter {
		public int getCount() {
			if(sp.getBoolean("showSystemProcess", true)){
				return systemInfos.size() + 1 + userInfos.size() + 1;
			}else{
				return userInfos.size() + 1;
			}
		}

		public Object getItem(int position) {
			ProcessInfo info = null;
			if (position == 0) {
				return null;
			} else if (position == userInfos.size() + 1) {
				return null;
			} else if (position <= userInfos.size()) {
				info = userInfos.get(position - 1);
			} else if (position >= userInfos.size() + 1 + 1) {
				info = systemInfos.get(position - userInfos.size() - 1 - 1);
			}
			return info;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			ProcessInfo info = null;
			if (position == 0) {
				TextView tv = new TextView(ProcessManagerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(0xBB000000);
				tv.setText("用户进程" + userInfos.size());
				return tv;
			} else if (position == userInfos.size() + 1) {
				TextView tv = new TextView(ProcessManagerActivity.this);
				tv.setTextColor(Color.WHITE);
				tv.setBackgroundColor(Color.BLACK);
				tv.setText("系统进程" + systemInfos.size());
				return tv;
			} else if (position <= userInfos.size()) {
				info = userInfos.get(position - 1);
			} else if (position >= userInfos.size() + 1 + 1) {
				info = systemInfos.get(position - userInfos.size() - 1 - 1);
			}
			View view = null;
			ViewHolder holder = null;
			if (convertView != null && convertView instanceof RelativeLayout) {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			} else {
				view = View.inflate(getApplicationContext(),
						R.layout.item_process_info, null);
				holder = new ViewHolder();
				holder.icon = (ImageView) view
						.findViewById(R.id.iv_process_icon);
				holder.tv_name = (TextView) view
						.findViewById(R.id.tv_process_appname);
				holder.tv_meminfo = (TextView) view
						.findViewById(R.id.tv_process_memory);
				holder.cb = (CheckBox) view.findViewById(R.id.cb_process);
				view.setTag(holder);
			}
			if (info.getPackageName().equals(getPackageName())) {
				holder.cb.setVisibility(View.INVISIBLE);
			} else {
				holder.cb.setVisibility(View.VISIBLE);
			}
			holder.icon.setImageDrawable(info.getIcon());
			holder.tv_name.setText(info.getAppName());
			holder.tv_meminfo.setText(Formatter.formatFileSize(
					getApplicationContext(), info.getOccupyMemory()));
			holder.cb.setChecked(info.isChecked());
			return view;
		}
	}

	static class ViewHolder {
		ImageView icon;
		TextView tv_name;
		TextView tv_meminfo;
		CheckBox cb;
	}

}
