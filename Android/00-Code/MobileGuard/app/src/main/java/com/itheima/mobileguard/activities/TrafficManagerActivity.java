package com.itheima.mobileguard.activities;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.TrafficStats;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.domain.TrafficInfo;
import com.itheima.mobileguard.engine.TrafficInfoParser;

public class TrafficManagerActivity extends Activity {

	private ConnectivityManager cm;
	private ListView lv_traffic;
	private TextView tv_mobile;
	private TextView tv_wifi;
	private List<TrafficInfo> infos;
	private TrafficAdapter adapter;
	private MyConn conn;
	private RelativeLayout rl_loading;
	private ImageView iv_loading_out;
	private Animation a;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trafficmanager);
		a = AnimationUtils.loadAnimation(this, R.anim.loading);
		cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		lv_traffic = (ListView) findViewById(R.id.lv_traffic);
		tv_mobile = (TextView) findViewById(R.id.tv_traffic_mobile);
		tv_wifi = (TextView) findViewById(R.id.tv_traffic_wifi);
		rl_loading = (RelativeLayout) findViewById(R.id.iv_traffic_loading);
		iv_loading_out = (ImageView) findViewById(R.id.iv_traffic_loading_out);
		conn = new MyConn();
//		//uid  用户的id   app被安装到系统时 有系统分配的UID
//		TrafficStats.getUidRxBytes(10003);
//		TrafficStats.getUidTxBytes(10003);
//		 proc/uid_stat/10041/tcp_rcv 存储的就是下载的流量
//		 proc/uid_stat/10041/tcp_snd 存储的就是上传的流量
//			cm.getActiveNetworkInfo().getTypeName(); // WIFI 或者 MOBILE
			fillData();
	}
	
	

	@Override
	protected void onStart() {
		fillData();
		super.onStart();
	}
	
	private void fillData() {
		rl_loading.setVisibility(View.VISIBLE);
		iv_loading_out.startAnimation(a);
		//总的流量 	全部的网络信息 包括移动 和WIFI
		long totalTraffic = TrafficStats.getTotalRxBytes() + TrafficStats.getTotalTxBytes();
		//移动流量 手机的2g 3g 4g 产生的流量	//rx receiver 接收下载  tranfer 发送 上传 
		long mobileTraffic = TrafficStats.getMobileRxBytes() + TrafficStats.getTotalRxBytes();
		long wifiTrafic = totalTraffic - mobileTraffic;
		
		tv_mobile.setText(Formatter.formatFileSize(this, mobileTraffic));
		tv_wifi.setText(Formatter.formatFileSize(this, wifiTrafic));
		new Thread(){
			public void run(){
				//获取流量
				infos = TrafficInfoParser.findInternetApp(getApplicationContext());
				Collections.sort(infos, conn);
				runOnUiThread(new Runnable() {
					public void run() {
							if(adapter == null){
								adapter = new TrafficAdapter();
								lv_traffic.setAdapter(adapter);
							}else{
								adapter.notifyDataSetChanged();
							}
								rl_loading.setVisibility(View.INVISIBLE);
								rl_loading.removeAllViews();
					}
				});
			}
		}.start();
	}
	
	private class TrafficAdapter  extends BaseAdapter{

		@Override
		public int getCount() {
			return infos.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			View view;
			ViewHolder holder;
			if(convertView == null){
				holder = new ViewHolder();
				view = View.inflate(getApplicationContext(), R.layout.item_traffic_info, null);
				holder.iv = (ImageView) view.findViewById(R.id.iv_traffic_icon);
				holder.tv_name = (TextView) view.findViewById(R.id.tv_traffic_appname);
				holder.tv_total = (TextView) view.findViewById(R.id.tv_traffic_total);
				holder.tv_up = (TextView) view.findViewById(R.id.tv_traffic_up);
				holder.tv_down  = (TextView) view.findViewById(R.id.tv_traffic_down);
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.iv.setImageDrawable(infos.get(position).getIcon());
			holder.tv_name.setText(infos.get(position).getAppName());
			long up = infos.get(position).getDownTraffic();
			long down = infos.get(position).getUpTraffic();
			if(up <= 0){
				up = 0;
			}
			if(down <= 0){
				down = 0;
			}
			long total = up + down;
			holder.tv_total.setText(Formatter.formatFileSize(getApplicationContext(), total));
			holder.tv_up.setText("上传:"+Formatter.formatFileSize(getApplicationContext(), up));
			holder.tv_down.setText("下载:"+Formatter.formatFileSize(getApplicationContext(), down));
			return view;
		}
		
	
	}
	
		static class ViewHolder{
			ImageView iv;
			TextView tv_up;
			TextView tv_down;
			TextView tv_name;
			TextView tv_total;
		}
	
	/**
	 * 比较器
	 * @author Administrator
	 *
	 */
	private class MyConn implements Comparator<TrafficInfo>{

		@SuppressLint("UseValueOf")
		public int compare(TrafficInfo lhs, TrafficInfo rhs) {	
			int result = new Long(rhs.getUpTraffic()+rhs.getDownTraffic()).compareTo(new Long(lhs.getUpTraffic()+lhs.getDownTraffic()));
			if(result == 0){
				return lhs.getAppName().compareTo(rhs.getAppName());
			}else{
				return result;
			}
		}
	}
}
