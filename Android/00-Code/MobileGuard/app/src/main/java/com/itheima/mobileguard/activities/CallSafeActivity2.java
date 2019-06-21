package com.itheima.mobileguard.activities;

import java.util.List;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.db.dao.BlackNumberDao;
import com.itheima.mobileguard.domain.BlackInfo;

public class CallSafeActivity2 extends Activity {
	private ListView lv;
	private MyBaseAdapter adapter;
	private List<BlackInfo> infos;
	private BlackNumberDao dao;
	private LinearLayout tv_callsafe_tip;
	private LinearLayout ll;
	private int totalPage;
	private int pageSize = 20;//页面显示数量
	private int currentPage = 0;//当前页码
	private EditText et_callsafe_page;
	private TextView tv_pageinfo;
	
	protected void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		fillData(currentPage,pageSize);
	}

	/**
	 * 消息处理器
	 */
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			ll.setVisibility(View.INVISIBLE);//查询到结果把进度条设置为 不可见
			if (infos.size()==0) {
				tv_callsafe_tip.setVisibility(View.VISIBLE);//如果查询到数据是 0 就提示用户还没有添加任何数据
			}else{
				tv_pageinfo.setText(currentPage+"/"+totalPage);
				adapter = new MyBaseAdapter();
				lv.setAdapter(adapter);
			}
		}
	};
	
	/**
	 * 显示数据数据
	 */
	private void fillData(final int pageNumber,final int pageSize) {
		//如果查询的数据很多的话 会花费一定的时间 所以应当在子线程中去查询
		new Thread(){
			public void run(){
				dao = new BlackNumberDao(CallSafeActivity2.this);
				infos = dao.findpart(pageNumber, pageSize);
				// 总的页码数 = 总记录数  /	页面最多显示的条目数
				int total = dao.getTotalRecord();
				totalPage = total%pageSize==0?(total/pageSize)-1:total/pageSize;
				//数据已经查询到 发送消息到主线程消息队列 更新UI
				Message msg = Message.obtain();
				handler.sendMessage(msg);
			}
		}.start();
	}

	/**
	 * 组件和界面初始化
	 */
	private void init() {
		setContentView(R.layout.activity_callsafe2);
		lv = (ListView) findViewById(R.id.lv_callsms_safe);
		tv_callsafe_tip = (LinearLayout) findViewById(R.id.ll_add_number_tips);
		ll = (LinearLayout) findViewById(R.id.ll_loading);
		et_callsafe_page = (EditText) findViewById(R.id.et_callsafe_page);
		tv_pageinfo = (TextView) findViewById(R.id.tv_callsafe_pageinfo);
	}
	
	/**
	 * 下一页
	 * @param v
	 */
	public void nextpage(View v){
		if(currentPage == totalPage){
			Toast.makeText(this, "已经是最后一页了", Toast.LENGTH_LONG).show();
		}else{
			currentPage +=1;
			fillData(currentPage, pageSize);
		}
	}
	/**
	 * 上一页
	 * @param v
	 */
	public void previouspage(View v){
		if(currentPage <= 0){
			Toast.makeText(this, "已经是最第一页了", Toast.LENGTH_LONG).show();
		}else{
			currentPage -=1;
			fillData(currentPage, pageSize);
		}
	}
	/**
	 * 跳转
	 * @param v
	 */
	public void jump(View v){
		String pageNumber = et_callsafe_page.getText().toString().trim();
		if(TextUtils.isEmpty(pageNumber)){
			Toast.makeText(this, "请输入正确的页码", Toast.LENGTH_SHORT).show();
			return;
		}
		int num = Integer.parseInt(pageNumber);
		if(num >= 0 && num <=totalPage){
			currentPage = num;
			fillData(currentPage, pageSize);
		}
	}
	
	/**
	 * 适配器
	 * @author Administrator
	 *
	 */
	private class MyBaseAdapter extends BaseAdapter{
		public int getCount() {
			return infos.size();
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
		//有多少个数据就会调用这个方法多少次 
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = null;
			ViewHolder holder = null;
			if(convertView == null){
				view = View.inflate(CallSafeActivity2.this, R.layout.item_callsafe_listblacknumber, null);
				holder = new ViewHolder();
				holder.tv_mode = (TextView) view.findViewById(R.id.tv_callsafe_mode);
				holder.tv_number = (TextView) view.findViewById(R.id.tv_callsafe_number);
				view.setTag(holder);
			}else{
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.tv_number.setText(infos.get(position).getNumber());
			String mode = infos.get(position).getMode();
			if("1".equals(mode)){
				holder.tv_mode.setText("全部拦截");
			}else if("2".equals(mode)){
				holder.tv_mode.setText("短信拦截");
			}else if("3".equals(mode)){
				holder.tv_mode.setText("电话拦截");
		}
			return view;
		}
		//findviewbyid 其实是比较浪费资源的 没显现一个条目这里就会执行两次，如果能有一个对象记住已经找到
		//的View 就不需要总是执行这样的代码了  相对来说是比较节约资源的
		/**
		 * 家庭组  相当于View对象的容器
		 * @author Administrator
		 *
		 */
		private class ViewHolder{
			TextView tv_number;
			TextView tv_mode;
		}
	}
	
	
	
}








