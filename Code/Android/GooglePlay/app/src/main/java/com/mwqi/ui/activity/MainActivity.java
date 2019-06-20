package com.mwqi.ui.activity;

import com.mwqi.R;
import com.mwqi.ui.fragment.BaseFragment;
import com.mwqi.ui.fragment.FragmentFactory;
import com.mwqi.ui.holder.MenuHolder;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.mwqi.ui.widget.*;
import com.mwqi.utils.UIUtils;

public class MainActivity extends BaseActivity {
	//主页面
	private DrawerLayout mDrawerLayout;
	private PagerTab mPageTabs;
	private ViewPager mPager;
	private MainPagerAdapter mAdapter;
	//菜单
	private FrameLayout mDrawer;
	private MenuHolder mMenuHolder;
	//ActionBar
	private ActionBar mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/** 控件初始化，会在父View中被调用 */
	protected void initView() {
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerLayout.setDrawerListener(new DemoDrawerListener());//设置drawer的开关监听
		mDrawerLayout.setDrawerShadow(R.drawable.ic_drawer_shadow, GravityCompat.START);//设置阴影
		//菜单
		mDrawer = (FrameLayout) findViewById(R.id.start_drawer);
		mMenuHolder = new MenuHolder();
		mDrawer.addView(mMenuHolder.getRootView());
		//ViewPager初始化
		mPager = (ViewPager) findViewById(R.id.pager);
		mAdapter = new MainPagerAdapter(getSupportFragmentManager());
		mPager.setAdapter(mAdapter);
		//tab的初始化、tab和ViewPager的互相绑定
		mPageTabs = (PagerTab) findViewById(R.id.tabs);
		mPageTabs.setViewPager(mPager);
		mPageTabs.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/** actionBar的初始化 */
	protected void initActionBar() {
		mActionBar = getSupportActionBar();
		mActionBar.setTitle(getString(R.string.app_name));//设置标题
		mActionBar.setDisplayHomeAsUpEnabled(true);//设置显示左侧按钮
		mActionBar.setHomeButtonEnabled(true);//设置左侧按钮可点
		//初始化开关，并和drawer关联
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close);
		mDrawerToggle.syncState();//该方法会自动和actionBar关联
	}

	/** 菜单键点击的事件处理 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//在4.0以前，android通过菜单键来增加选项，在4.0后，提倡actionBar，所以菜单键增加的按钮可以显示到actionBar上，这里也能处理ActionBar上的菜单键事件
		return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
	}

	/** drawer的监听 */
	private class DemoDrawerListener implements DrawerLayout.DrawerListener {
		@Override
		public void onDrawerOpened(View drawerView) {// 打开drawer
			mDrawerToggle.onDrawerOpened(drawerView);//需要把开关也变为打开
		}

		@Override
		public void onDrawerClosed(View drawerView) {// 关闭drawer
			mDrawerToggle.onDrawerClosed(drawerView);//需要把开关也变为关闭
		}

		@Override
		public void onDrawerSlide(View drawerView, float slideOffset) {// drawer滑动的回调
			mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
		}

		@Override
		public void onDrawerStateChanged(int newState) {// drawer状态改变的回调
			mDrawerToggle.onDrawerStateChanged(newState);
		}
	}

	/** ViewPager的适配器 */
	public class MainPagerAdapter extends FragmentPagerAdapter {
		private String[] mTabTitle;

		public MainPagerAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			mTabTitle = UIUtils.getStringArray(R.array.tab_names);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return mTabTitle[position];
		}

		@Override
		public int getCount() {
			return mTabTitle.length;
		}

		@Override
		public Fragment getItem(int position) {
			return FragmentFactory.createFragment(position);
		}
	}

	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int state) {
			//ViewPager滑动状态改变的回调
		}

		@Override
		public void onPageScrolled(int index, float offset, int offsetPx) {
			//ViewPager滑动时的回调
		}

		@Override
		public void onPageSelected(int index) {
			// ViewPager页面被选中的回调
			BaseFragment fragment = FragmentFactory.createFragment(index);
			// 当页面被选中 再显示要加载的页面....防止ViewPager提前加载(ViewPager一般加载三个，自己，左一个，右一个)
			fragment.show();// 调用show方法加载pager里面的数据
		}
	}
}
