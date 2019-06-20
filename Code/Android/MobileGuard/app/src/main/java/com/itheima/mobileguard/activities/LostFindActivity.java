package com.itheima.mobileguard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.utils.MD5Utils;
import com.itheima.mobileguard.utils.UiUtils;

public class LostFindActivity extends Activity {

    private SharedPreferences sp;
    private TextView tv_lostfind_safecontact;
    private ImageView iv_lostfind_lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostfind);
        //找到组件
        tv_lostfind_safecontact = (TextView) findViewById(R.id.tv_lostfind_safecontact);
        iv_lostfind_lock = (ImageView) findViewById(R.id.iv_lostfind_lock);
        //获取SharedPreference 用于存储用户设置信息的
        sp = getSharedPreferences("config", MODE_PRIVATE);
        //看用户是否设置过防盗信息
        boolean isSetup = isFinishSetup();
        //如果没有就进入设置向导界面
        if (isSetup) {
            //直接显示lostfind界面 获取用户设置的数据
            String safeContact = sp.getString("safeContact", "");
            boolean isOpen = sp.getBoolean("protecting", false);
            tv_lostfind_safecontact.setText(safeContact);
            if (isOpen) {
                iv_lostfind_lock.setImageResource(R.drawable.lock);
            } else {
                iv_lostfind_lock.setImageResource(R.drawable.unlock);
            }
        } else {
            //进入设置向导界面
            Intent intent = new Intent(this, LostSetup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 按钮点击 重新进入设置向导
     *
     * @param v
     */
    public void reEntrySetup(View v) {
        Intent intent = new Intent(this, LostSetup1Activity.class);
        startActivity(intent);
        finish();
    }

    /**
     * 判断用户是否设置了手机防盗
     *
     * @return
     */
    private boolean isFinishSetup() {
        boolean b = sp.getBoolean("finishing", false);
        return b;
    }

    /**
     * 开启防盗
     */
    public void open(View v) {
        boolean isOpen = sp.getBoolean("protecting", false);
        if (isOpen) {
            Editor editor = sp.edit();
            editor.putBoolean("protecting", false);
            editor.commit();
            iv_lostfind_lock.setImageResource(R.drawable.unlock);
        } else {
            Editor editor = sp.edit();
            editor.putBoolean("protecting", true);
            editor.commit();
            iv_lostfind_lock.setImageResource(R.drawable.lock);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lostfind, menu);
        return true;
    }

    private AlertDialog dialog;

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (R.id.item_change_name == item.getItemId()) {
//				弹出对话框
            AlertDialog.Builder builder = new Builder(this);
            builder.setTitle("修改防盗名称");
            final EditText et = new EditText(this);
            builder.setView(et);
            builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String newName = et.getText().toString();
                    Editor editor = sp.edit();
                    editor.putString("newName", newName);
                    if (!TextUtils.isEmpty(newName) && newName.length() > 5) {
                        UiUtils.showToast(LostFindActivity.this, "长度不能超过5个");
                        return;
                    }
                    editor.commit();
                }
            });
            dialog = builder.show();

        } else if (R.id.item_change_password == item.getItemId()) {

            AlertDialog.Builder builder = new Builder(this);
            View view = View.inflate(this, R.layout.dialog_lostfind_changepassword, null);
            builder.setView(view);
            dialog = builder.show();
            final EditText et_lostfind_newpassword = (EditText) view.findViewById(R.id.et_lostfind_newpassword);
            final EditText et_lostfind_oldpassword = (EditText) view.findViewById(R.id.et_lostfind_oldpassword);
            Button bt_lostfind_ok = (Button) view.findViewById(R.id.bt_lostfind_ok);
            Button bt_lostfind_cancel = (Button) view.findViewById(R.id.bt_lostfind_cancel);

            bt_lostfind_ok.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    //现获取用户的输入
                    String setOldPassword = sp.getString("password", "");//设置的密码
                    String oldPassword = et_lostfind_oldpassword.getText().toString();//输入的old密码
                    String newPassword = et_lostfind_newpassword.getText().toString();//新密码
                    if (MD5Utils.MD5Encode(oldPassword).equals(setOldPassword)) {
                        Editor editor = sp.edit();
                        editor.putString("password", MD5Utils.MD5Encode(newPassword));
                        editor.commit();
                        dialog.dismiss();
                        UiUtils.showToast(LostFindActivity.this, "密码设置成功");
                    } else {
                        UiUtils.showToast(LostFindActivity.this, "密码不匹配");
                    }
                }
            });
            bt_lostfind_cancel.setOnClickListener(new OnClickListener() {//取消了设置
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        } else if (R.id.item_howtouse == item.getItemId()) {
            AlertDialog.Builder builder = new Builder(this);
            builder.setTitle("如何使用").setMessage("部分功能需要到设置-->安全-->设备管理-->激活手机小卫士的设备管理员功能，否则无法使用，如果需要卸载此应用，需要先取消手机小卫士的设备管理员功能")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            dialog = builder.show();
        }
        return super.onMenuItemSelected(featureId, item);
    }


}

