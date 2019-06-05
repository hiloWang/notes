package com.itheima.mobileguard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.services.GuardServer1;
import com.itheima.mobileguard.services.KeepLifeService;
import com.itheima.mobileguard.utils.MD5Utils;
import com.itheima.mobileguard.utils.UiUtils;
import com.startapp.android.publish.StartAppAd;
import com.startapp.android.publish.StartAppSDK;

public class HomeActivity extends Activity {

    private GridView gv_home_function;
    private SharedPreferences sp;
    // 九大功能
    private String[] items = {"手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "手机杀毒",
            "缓存清理", "高级工具", "设置中心"};
    // 九大功能图片id
    private int[] icons = {R.drawable.safe, R.drawable.callmsgsafe,
            R.drawable.app_selector, R.drawable.taskmanager,
            R.drawable.netmanager, R.drawable.trojan, R.drawable.sysoptimize,
            R.drawable.atools, R.drawable.settings};


    @Override
    public void onBackPressed() {
        StartAppAd saa = new StartAppAd(this);
        saa.onBackPressed();
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        StartAppAd saa = new StartAppAd(this);
        saa.onResume();
        super.onResume();
    }


    // 初始化
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        StartAppSDK.init(this, "105756043", "208401311", true);

        setContentView(R.layout.activity_home);


        //开启流氓守护
        Intent intent = new Intent(this, GuardServer1.class);
        startService(intent);
        //开启流氓守护
        Intent keep = new Intent(this, KeepLifeService.class);
        startService(keep);

        // 获取SharedPreference
        sp = getSharedPreferences("config", MODE_PRIVATE);
        // 显示功能条目
        gv_home_function = (GridView) findViewById(R.id.gv_home_function);
        // 注册功能点击事件
        gv_home_function.setOnItemClickListener(new MyOnItemClickListener());
    }

    /**
     * 修改防盗功能的名字 即时生效
     */
    @Override
    protected void onStart() {
        gv_home_function.setAdapter(new GVBaseAdapterea());
        super.onStart();
    }

    // 定义点击事件
    private class MyOnItemClickListener implements OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            Intent intent;
            switch (position) {
                case 0:
                    // 0 位置是手机防盗 设置相应的点击事件
                    // 判断用户是否设置了密码
                    String passowrd = sp.getString("password", "");
                    // 没有设置密码
                    if (TextUtils.isEmpty(passowrd)) {
                        showSetupPasswordDialog();// 弹出设置密码对话框
                    } else {// 设置过密码
                        showEnterPasswordDialog(passowrd);
                    }
                    break;
                case 1:
                    intent = new Intent(HomeActivity.this, CallSafeActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(HomeActivity.this, AppManagerActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(HomeActivity.this, ProcessManagerActivity.class);
                    startActivity(intent);
                    break;
                case 4:
                    intent = new Intent(HomeActivity.this, TrafficManagerActivity.class);
                    startActivity(intent);
                    break;
                case 5:
                    intent = new Intent(HomeActivity.this, AntilVirusActivity.class);
                    startActivity(intent);
                    break;
                case 6:
                    intent = new Intent(HomeActivity.this, ClearCacheActivity.class);
                    startActivity(intent);
                    break;
                case 7:
                    intent = new Intent(HomeActivity.this, AtoolsActivity.class);
                    startActivity(intent);
                    break;
                case 8://进入设置中心
                    //进入设置中心
                    intent = new Intent(HomeActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private AlertDialog dialog;// 享元模式
    private EditText et_home_setupPassowrd;
    private EditText et_home_confirmPassowrd;
    private EditText et_home_enterPassowrd;
    private Button bt_home_ok;
    private Button bt_home_cancel;

    // 弹出设置密码的对话框
    public void showSetupPasswordDialog() {
        AlertDialog.Builder builder = new Builder(this);
        View view = View
                .inflate(this, R.layout.dialog_home_setuppassword, null);
        dialog = builder.setView(view).show();
        et_home_setupPassowrd = (EditText) view
                .findViewById(R.id.et_home_setuppassword);
        et_home_confirmPassowrd = (EditText) view
                .findViewById(R.id.et_home_confirmpassword);
        bt_home_ok = (Button) view.findViewById(R.id.bt_home_ok);
        bt_home_cancel = (Button) view.findViewById(R.id.bt_home_cancel);
        // 给按钮注册点击事件
        bt_home_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 现获取用户的输入
                String newPassword = et_home_setupPassowrd.getText().toString();
                String confirmPassowrd = et_home_confirmPassowrd.getText()
                        .toString();
                if (TextUtils.isEmpty(newPassword)
                        || TextUtils.isEmpty(confirmPassowrd)) {
                    UiUtils.showToast(HomeActivity.this, "密码和确认密码都必须输入");
                    return;
                }
                if (!newPassword.equals(confirmPassowrd)) {
                    UiUtils.showToast(HomeActivity.this, "两次输入不一致");
                    return;
                }
                Editor editor = sp.edit();
                editor.putString("password", MD5Utils.MD5Encode(newPassword));
                editor.commit();
                loadLostFindActivity();
                dialog.dismiss();
            }
        });
        bt_home_cancel.setOnClickListener(new OnClickListener() {// 取消了设置
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // 弹出输入密码的对话框
    private void showEnterPasswordDialog(final String password) {
        AlertDialog.Builder builder = new Builder(this);
        View view = View.inflate(this, R.layout.dialog_home_enterpssword, null);
        dialog = builder.setView(view).show();
        et_home_enterPassowrd = (EditText) view.findViewById(R.id.et_home_enterpassword);
        bt_home_ok = (Button) view.findViewById(R.id.bt_home_ok);
        bt_home_cancel = (Button) view.findViewById(R.id.bt_home_cancel);

        // 给按钮注册点击事件
        bt_home_ok.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // 现获取用户的输入
                String enterPassword = et_home_enterPassowrd.getText()
                        .toString();
                if (TextUtils.isEmpty(enterPassword)) {
                    UiUtils.showToast(HomeActivity.this, "请输入密码");
                    return;
                } else {// 校验密码
                    if (password.equals(MD5Utils.MD5Encode(enterPassword))) {
                        loadLostFindActivity();
                        dialog.dismiss();
                    } else {
                        UiUtils.showToast(HomeActivity.this, "密码错误");
                    }
                }
            }
        });
        bt_home_cancel.setOnClickListener(new OnClickListener() {// 取消了设置
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void loadLostFindActivity() {
        Intent intent = new Intent(this, LostFindActivity.class);
        this.startActivity(intent);
    }

    // 自定义适配器
    private class GVBaseAdapterea extends BaseAdapter {
        public int getCount() {
            return items.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = View.inflate(HomeActivity.this, R.layout.item_home_grid,
                        null);
            } else {
                view = convertView;
            }
            ImageView iv = (ImageView) view.findViewById(R.id.iv_home_itme);
            TextView tv = (TextView) view.findViewById(R.id.tv_home_itme);
            iv.setImageResource(icons[position]);
            tv.setText(items[position]);
            String newName = sp.getString("newName", "");
            if (!TextUtils.isEmpty(newName) && position == 0) {
                tv.setText(newName);
            }
            return view;
        }
    }


}