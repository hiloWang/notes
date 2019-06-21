package com.itheima.mobileguard.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.domain.AppInfo;
import com.itheima.mobileguard.engine.AppInfoParser;
import com.itheima.mobileguard.utils.DensityUtil;
import com.stericson.RootTools.RootTools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AppManagerActivity extends Activity implements OnClickListener {

    private TextView tv_appmanager_systeminfo;
    private TextView tv_appmanager_sdinfo;
    private ListView lv_appinfo;
    private LinearLayout ll_appManager_loading;
    private TextView tv_appmanager_appcount;
    private PopupWindow popupWindow;
    private PackageUninstallReceiver receiver;
    private AppInfoAdapter adapter;
    /**
     * Liveview 中 被点击的条目锁包含的内容
     */
    private AppInfo appInfo;
    /**
     * 系统应用
     */
    private List<AppInfo> systemAppInfos;
    /**
     * 用户的应用
     */
    private List<AppInfo> userAppInfos;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appmanager);
        tv_appmanager_sdinfo = (TextView) findViewById(R.id.tv_appmanager_sdinfo);
        tv_appmanager_systeminfo = (TextView) findViewById(R.id.tv_appmanager_systeminfo);
        lv_appinfo = (ListView) findViewById(R.id.lv_appinfos);
        ll_appManager_loading = (LinearLayout) findViewById(R.id.ll_appmanamger_loading);
        tv_appmanager_appcount = (TextView) findViewById(R.id.tv_appmanager_appcount);

        // 注册广播
        receiver = new PackageUninstallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        registerReceiver(receiver, filter);

        fillInfo();
        loadAppInfos();
        lv_appinfo.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // 得到返回的ListView中的item的包含的对象
                Object obj = lv_appinfo.getItemAtPosition(position);
                appInfo = (AppInfo) obj;
                if (obj != null && obj instanceof AppInfo) {

                    View contentView = View.inflate(AppManagerActivity.this,
                            R.layout.item_appmanager_popup, null);
                    LinearLayout ll_uninstall = (LinearLayout) contentView
                            .findViewById(R.id.ll_uninstall);
                    LinearLayout ll_start = (LinearLayout) contentView
                            .findViewById(R.id.ll_start);
                    LinearLayout ll_share = (LinearLayout) contentView
                            .findViewById(R.id.ll_share);
                    LinearLayout ll_setting = (LinearLayout) contentView
                            .findViewById(R.id.ll_setting);
                    ll_uninstall.setOnClickListener(AppManagerActivity.this);
                    ll_share.setOnClickListener(AppManagerActivity.this);
                    ll_start.setOnClickListener(AppManagerActivity.this);
                    ll_setting.setOnClickListener(AppManagerActivity.this);
                    // 获取当前被点击的View在屏幕上的位置
                    int[] location = new int[2];
                    view.getLocationInWindow(location);

                    dismissPopupWindow();
                    // -2 -2 时包裹内容
                    popupWindow = new PopupWindow(contentView, -2, -2);
                    // PopupWindow默认是没有背景色的 但是要播放动画必须要有背景色 所以设置一个透明的背景色
                    popupWindow.setBackgroundDrawable(new ColorDrawable(
                            Color.TRANSPARENT));
                    // 设置动画
                    AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
                    aa.setDuration(200);
                    aa.setRepeatCount(0);
                    ScaleAnimation sa = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                            Animation.RELATIVE_TO_SELF, 0,
                            Animation.RELATIVE_TO_SELF, 1);
                    sa.setDuration(200);
                    sa.setRepeatCount(0);
                    AnimationSet as = new AnimationSet(false);
                    as.addAnimation(aa);
                    as.addAnimation(sa);
                    popupWindow.showAtLocation(parent, Gravity.LEFT
                            + Gravity.TOP, 10, location[1] - (view.getHeight() + DensityUtil.px2dip(AppManagerActivity.this, 12f)));
                    contentView.startAnimation(as);
                }
            }
        });

        lv_appinfo.setOnScrollListener(new OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                dismissPopupWindow();// 拖动时隐藏popup

                if (userAppInfos != null && systemAppInfos != null) {
                    /*
                     * if(firstVisibleItem <= userAppInfos.size()){
					 * tv_appmanager_appcount
					 * .setText("用户程序"+userAppInfos.size()+"个"); }else
					 * if(view.getFirstVisiblePosition() >= userAppInfos.size()
					 * -1 ){
					 * tv_appmanager_appcount.setText("系统程序"+systemAppInfos
					 * .size()+"个"); }
					 */
                    if (firstVisibleItem >= (userAppInfos.size() + 1)) {
                        tv_appmanager_appcount.setText("系统程序："
                                + systemAppInfos.size() + "个");
                    } else {
                        tv_appmanager_appcount.setText("用户程序："
                                + userAppInfos.size() + "个");
                    }
                }
            }
        });
    }

    /**
     * 获取系统和sd卡的可用空间 并赋值给对应控件
     */
    private void fillInfo() {
        File sdFile = Environment.getExternalStorageDirectory();
        long sdFreeSpace = sdFile.getFreeSpace();
        File systemFile = Environment.getRootDirectory();
        long systemFreeSpace = systemFile.getFreeSpace();

        System.out.println("usable:" + Formatter.formatFileSize(this, systemFile.getUsableSpace()) + ",total:" + systemFile.getTotalSpace());
//		StatFs sf = new StatFs(systemFile.getPath());
//		long blockSize = sf.getBlockSize();
//		long blockCount = sf.getBlockCount();
//		long availCount = sf.getAvailableBlocks();
//		System.out.println(blockSize);
//		System.out.println(blockCount);
//		System.out.println(availCount);
        // 格式化
        String sd = Formatter.formatFileSize(this, sdFreeSpace);
        String system = Formatter.formatFileSize(this, systemFreeSpace);
        tv_appmanager_sdinfo.setText("SD卡可用空间" + sd);
        tv_appmanager_systeminfo.setText("系统可用空间" + system);
    }

    /**
     * 消息处理器
     */
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            ll_appManager_loading.removeAllViews();
            ll_appManager_loading.setVisibility(View.INVISIBLE);

            if (adapter == null) {
                adapter = new AppInfoAdapter();
                lv_appinfo.setAdapter(adapter);
            } else {
                adapter.notifyDataSetChanged();
            }

            System.out.println("来了");
        }
    };

    /**
     * 数据适配器
     */
    private class AppInfoAdapter extends BaseAdapter {
        public int getCount() {
            return systemAppInfos.size() + userAppInfos.size();
        }

        public Object getItem(int position) {
            AppInfo appInfo = null;
            if (position == 0) {
                return null;
            } else if (position == userAppInfos.size() + 1) {
                return null;
            }
            if (position <= userAppInfos.size()) {
                appInfo = userAppInfos.get(position - 1);
                return appInfo;
            } else if (position >= userAppInfos.size() + 1 + 1) {
                int location = position - userAppInfos.size() - 1 - 1;
                appInfo = systemAppInfos.get(location);
            }

            return appInfo;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (position == 0) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("用户程序" + userAppInfos.size() + "个");
                tv.setTextColor(Color.WHITE);
                return tv;
            } else if (position == userAppInfos.size() + 1) {
                TextView tv = new TextView(getApplicationContext());
                tv.setBackgroundColor(Color.GRAY);
                tv.setText("系统程序" + systemAppInfos.size() + "个");
                tv.setTextColor(Color.WHITE);
                return tv;
            }
            AppInfo appInfo = null;
            if (position <= userAppInfos.size()) {
                appInfo = userAppInfos.get(position - 1);
            } else if (position >= userAppInfos.size() + 1 + 1) {
                int location = position - userAppInfos.size() - 1 - 1;
                appInfo = systemAppInfos.get(location);
            }

            ViewHolder holder = null;
            if (convertView != null && convertView instanceof LinearLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(AppManagerActivity.this,
                        R.layout.item_appinfo, null);
                holder = new ViewHolder();
                holder.iv_appinfo_icon = (ImageView) view
                        .findViewById(R.id.iv_appinfo_icon);
                holder.tv_appname = (TextView) view
                        .findViewById(R.id.tv_appname);
                holder.tv_appcategory = (TextView) view
                        .findViewById(R.id.tv_appcategory);
                holder.tv_appsize = (TextView) view
                        .findViewById(R.id.tv_appsize);
                view.setTag(holder);
            }
            holder.iv_appinfo_icon.setImageDrawable(appInfo.getAppIcon());
            holder.tv_appname.setText(appInfo.getAppName());
            holder.tv_appsize.setText(Formatter.formatFileSize(
                    AppManagerActivity.this, appInfo.getAppSize()));
            if (appInfo.isInRom()) {
                holder.tv_appcategory.setText("手机内存");
            } else {
                holder.tv_appcategory.setText("SD卡");
            }
            return view;
        }

    }

    /**
     * View持有者 用来记住控件的子控件
     *
     * @author Administrator
     */
    private class ViewHolder {
        ImageView iv_appinfo_icon;
        TextView tv_appname;
        TextView tv_appcategory;
        TextView tv_appsize;
    }

    /**
     * 加载APP的信息
     */
    public void loadAppInfos() {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.loading);
        ll_appManager_loading.startAnimation(a);
        ll_appManager_loading.setVisibility(View.VISIBLE);
        systemAppInfos = new ArrayList<AppInfo>();
        userAppInfos = new ArrayList<AppInfo>();
        new Thread() {
            public void run() {
                List<AppInfo> infos = AppInfoParser
                        .getAllAppinfo(AppManagerActivity.this);
                for (AppInfo info : infos) {
                    if (info.isUserApp()) {
                        userAppInfos.add(info);
                    } else {
                        systemAppInfos.add(info);
                    }
                }
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
            popupWindow = null;
        }
    }

    @Override
    protected void onDestroy() {
        dismissPopupWindow();
        // 反注册广播
        unregisterReceiver(receiver);
        receiver = null;
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_start:// 启动
                startApplication();
                break;
            case R.id.ll_share:// 分享
                shareApp();
                System.out.println("lai le a ");
                break;
            case R.id.ll_uninstall:// 卸载
                System.out.println("lai le a ");
                uninstallApplication();
                break;
            case R.id.ll_setting:// 详细设置
                showAppSetting();
                System.out.println("lai le a ");
                break;
        }
        dismissPopupWindow();
    }

    private void showAppSetting() {
        Intent intent = new Intent();
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
        startActivity(intent);
    }

    /**
     * 删除一个程序 卸载成功后需要刷新界面 注册广播
     */
    private void uninstallApplication() {

        boolean b = appInfo.isUserApp();
        if (b) {
            // 用户程序 正常卸载
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setAction(Intent.ACTION_DELETE);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setData(Uri.parse("package:" + appInfo.getPackageName()));
            startActivity(intent);
        } else {
            if (!RootTools.isRootAvailable()) {// 手机没有root
                Toast.makeText(this, "卸载系统应用需要ＲＯＯＴ权限", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            try {
                if (!RootTools.isAccessGiven()) {
                    Toast.makeText(this, "请先给该应用分配root权限", Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                /*
                 * RootTools.sendShell("mount -a remount ,rw /system", 30000);
				 * RootTools.sendShell("rm -r "+appInfo.getApkPath(), 30000);
				 * RootTools.sendShell("mount -a remount ,r /system", 30000);
				 */
                RootTools.sendShell("mount -o remount ,rw /system", 3000);
                RootTools.sendShell("rm -r " + appInfo.getApkPath(), 30000);
                RootTools.sendShell("mount -o remount ,r /system", 3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通过短信分享该应用
     */
    private void shareApp() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra(Intent.EXTRA_TEXT,
                "分享一个应用给你，这个应用很不错:" + appInfo.getAppName()
                        + "下载路径：https://play.google.com/store/apps/details?id="
                        + appInfo.getPackageName());
        intent.setType("text/plain");
        startActivity(intent);
    }

    /**
     * 启动一个application
     */
    public void startApplication() {
        PackageManager pm = getPackageManager();
        // 通过包管理器，传入包名 ，获取启动此包对应的application的意图
        Intent intent = pm.getLaunchIntentForPackage(appInfo.getPackageName());
        // 有的application没有界面 所以有可能intent为null
        if (intent == null) {
            Toast.makeText(this, "该程序没有界面", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(intent);
        }
    }

    private class PackageUninstallReceiver extends BroadcastReceiver {

        public void onReceive(Context context, Intent intent) {
            // 只要有应用被卸载了 就刷新界面
            System.out.println("ddsssssssssssssssssssssssssssssss"
                    + intent.getAction() + intent.getDataString());
            loadAppInfos();

        }
    }


}
