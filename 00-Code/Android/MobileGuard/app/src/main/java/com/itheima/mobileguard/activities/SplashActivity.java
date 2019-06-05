package com.itheima.mobileguard.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.db.dao.AntiVirusDao;
import com.itheima.mobileguard.utils.StreamUtils;
import com.itheima.mobileguard.utils.UiUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_splash_versionname;
	private ProgressBar pb_splash_download;
	private int serverVersionCode;// 服务器的版本号
	private int clientVersionCode;// 客户端的版本号
	private String newVersionDesc;// 新版本的描述
	private String dowmloadUrl;// 新版本的下载地址
	private AlphaAnimation animation;// 动画
	private RelativeLayout rl_splash_root;// 根节点
	private ImageView iv_splash;

	// 初始化
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 界面打开就开始动画
		iv_splash = (ImageView) findViewById(R.id.iv_splash);
		animation = new AlphaAnimation(0.5f, 1.0f);// 透明度
		animation.setDuration(1000);
		animation.setRepeatMode(Animation.REVERSE);
		animation.setRepeatCount(5);
		rl_splash_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
		rl_splash_root.startAnimation(animation);
		Animation ivAnim = AnimationUtils.loadAnimation(this, R.anim.loading);
		iv_splash.startAnimation(ivAnim);
		// 找到组件
		tv_splash_versionname = (TextView) findViewById(R.id.tv_splash_versionname);
		pb_splash_download = (ProgressBar) findViewById(R.id.pb_splash_download);
		// 拷贝资产文件到应用目录
		copyDB("address.db");
		//拷贝病毒数据库到应用目录
		copyDB("antivirus.db");
		System.out.println("拷贝病毒库");
		// 创建快捷图标
		createSHORTCUT();
		
		//检查数据库更新
		updateVirus();
		
		try {
			// 设置版本号
			// flag一般写0就可以了
			PackageInfo info = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			String versionName = info.versionName;
			tv_splash_versionname.setText(versionName);
			clientVersionCode = info.versionCode;
			// 连接服务器获取版本号
			// 检测是否开启了自动更新
			sp = getSharedPreferences("config", MODE_PRIVATE);
			System.out.println(sp.getBoolean("autoUpdate", false));

			if (sp.getBoolean("autoUpdate", false)) {
				checkUpdate();
			} else {
				new Thread() {
					public void run() {
						try {
							Thread.sleep(2000);
							loadMainUI();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		} catch (NameNotFoundException e) {
			// 不会发生的异常
			e.printStackTrace();
		}

		// 隐藏图标的
		/*
		 * final PackageManager pm = getPackageManager(); new Thread(){ public
		 * void run(){ SystemClock.sleep(5000);
		 * pm.setComponentEnabledSetting(getComponentName(), 2, 1); } }.start();
		 */

	}

	private void updateVirus() {
			new Thread(){
				public void run(){
					//先判断病毒数据库是否存在
					if(AntiVirusDao.isVirusDBExists(getApplicationContext())){
						HttpUtils utils = new HttpUtils();
						final String version = AntiVirusDao.getVirusVersion(getApplicationContext());
						//服务器地址
						String uri = "http://192.168.1.199:8080/FileUploadForAndroid/servlet/VirusVersionCheck?version="+version;
						utils.send(HttpMethod.GET, uri, new RequestCallBack<String>(){
							public void onFailure(HttpException arg0,
									String arg1) {
							}
							public void onSuccess(ResponseInfo arg0) {
								String result = arg0.result.toString();
								if(result == null){//说明不需要更新
									return;
								}
								try {
									JSONObject obj = new JSONObject(result);
									String md5 = obj.getString("md5");
									String desc = obj.getString("desc");
									//跟新数据库 和 版本
									AntiVirusDao.updateVirusVersoin(getApplicationContext(), String.valueOf(Integer.parseInt(version)+1));
									AntiVirusDao.updateVirusAPI(getApplicationContext(), md5,desc);
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					}
			}
			}.start();
	}

	/**
	 * 创建快捷图标
	 */
	public void createSHORTCUT() {
		if (sp.getBoolean("created", false)) {
			System.out.println(sp.getBoolean("created", false));
			return;
		}
		sp.edit().putBoolean("created", true).commit();
		/*
		 * boolean isCreatedShortcut = sp.getBoolean("isCreatedShortcut",
		 * false); if(isCreatedShortcut){ Toast.makeText(this, "已经创建快捷方式",
		 * Toast.LENGTH_LONG).show(); return; } //
		 * com.android.launcher.action.INSTALL_SHORTCUT //快捷方式必须使用隐式意图 Intent
		 * doWhat = new Intent(); doWhat.addCategory(Intent.CATEGORY_DEFAULT);
		 * doWhat.setAction("com.itheima.mobileguard.activities.HomeActivity");
		 * 
		 * Intent intent = new Intent();
		 * intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		 * //生成什么样的快捷方式 intent.putExtra("duplicate", false);//只允许创建一个快捷图标
		 * intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "黑马安全卫士");
		 * intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
		 * BitmapFactory.decodeResource(getResources(), R.drawable.luncher_bg));
		 * intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, doWhat); //发出广播
		 * 给我创建一个快捷方式 sendBroadcast(intent);
		 * sp.edit().putBoolean("isCreatedShortcut", true);
		 */
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// 干什么事，叫什么名，长什么样
		Intent shortcutIntent = new Intent();
		intent.putExtra("duplicate", false);// 只允许一个快捷图标
		shortcutIntent.setAction("aaa.bbb.ccc");// 隐式意图的action
		shortcutIntent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);// 快捷方式干什么事
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "黑马快捷");// 快捷方式的名字
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
				BitmapFactory.decodeResource(getResources(), R.drawable.app));
		// 快捷方式的图标
		sendBroadcast(intent);// 发送广播，给我创建一个快捷方式
	}

	/**
	 * 拷贝数据库资源文件到应用目录
	 * 
	 * @param dbName
	 */
	private void copyDB(final String dbName) {
		new Thread() {
			InputStream in = null;
			FileOutputStream out = null;

			public void run() {
				File file = new File(getFilesDir(), dbName);
				if (file.exists() && file.length() > 0) {
					System.out.println("资源文件已经存在");
					return;
				}
				try {
					in = getAssets().open(dbName);
					out = openFileOutput(dbName, MODE_PRIVATE);
					byte[] buf = new byte[1024];
					int len = 0;
					while ((len = in.read(buf)) != -1) {
						out.write(buf, 0, len);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (in != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					if (out != null) {
						try {
							in.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}.start();

	}

	private int Load_MAINUI = 1;// 加载到主界面
	private int SHOW_DiALOF = 2;// 弹出对话框
	// 消息处理器
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case 1:// 加载到主界面
				loadMainUI();
				break;
			case 2:
				// 显示对话框
				showUpdateDialog();
				break;
			}
		}
	};

	// 加载到主界面
	private void loadMainUI() {
		Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
		startActivity(intent);
		finish();// 用户在主界面点击 返回键不应该返回log界面 而是应当直接退出
	}

	private AlertDialog dialog;

	// 显示更新对话框
	protected void showUpdateDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("更新提示").setMessage(newVersionDesc)
				.setPositiveButton("更新", new OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// 下载服务器上的新版本
						downloadNewVersion();
					}
				}).setNegativeButton("暂不更新", new OnClickListener() {// 暂且不更新
							// 就直接进入主界面
							public void onClick(DialogInterface dialog,
									int which) {
								loadMainUI();
							}
						}).setOnCancelListener(new OnCancelListener() {// 直接取消对话框时
																		// 也是进入到主界面
							public void onCancel(DialogInterface dialog) {
								loadMainUI();
							}
						});
		// 显示对话框
		dialog = builder.show();
	}

	// 冲服务器下载新的版本 提示用户安装
	protected void downloadNewVersion() {
		// 判断用户的sd卡是否准备好了
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			UiUtils.showToast(SplashActivity.this, "您的sd卡没有准备好,更细失败");
			loadMainUI();
			return;
		}
		// 暂时不判断sd卡空间是否足够
		// 利用开源的框架来下载
		HttpUtils utils = new HttpUtils();
		utils.download(dowmloadUrl,
				new File(Environment.getExternalStorageDirectory(), "temp.apk")
						.getAbsolutePath(), new RequestCallBack<File>() {
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("错误代码 10006，请联系客服");
						UiUtils.showToast(SplashActivity.this,
								"错误代码 10006，请联系客服");
					}

					// 让用户选择安装 下面是系统自带的apk安装器 通过意图启动它
					/*
					 * <action android:name="android.intent.action.VIEW" />
					 * <action
					 * android:name="android.intent.action.INSTALL_PACKAGE" />
					 * <category android:name="android.intent.category.DEFAULT"
					 * /> <data android:scheme="content" /> <data
					 * android:scheme="file" /> <data
					 * android:mimeType="application/vnd.android.package-archive"
					 * />
					 */
					public void onSuccess(ResponseInfo<File> arg0) {
						pb_splash_download.setVisibility(ProgressBar.GONE);// 下载成功进度条消失
						Intent intent = new Intent();
						intent.setAction(Intent.ACTION_VIEW);
						intent.setAction(Intent.ACTION_INSTALL_PACKAGE);
						intent.addCategory(Intent.CATEGORY_DEFAULT);
						intent.setDataAndType(Uri.fromFile(arg0.result),
								"application/vnd.android.package-archive");
						startActivityForResult(intent, 0);
						// 为什么要for result 因为如果用户有点击取消安装 还是要进入到主界面
					}

					public void onLoading(long total, long current,
							boolean isUploading) {
						super.onLoading(total, current, isUploading);
						pb_splash_download.setVisibility(ProgressBar.VISIBLE);
						pb_splash_download.setMax((int) total);
						pb_splash_download.setProgress((int) current);
					}
				});
	}

	// 有结果返回
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 进入到主界面
		loadMainUI();
		super.onActivityResult(requestCode, resultCode, data);
	}

	private long startTime;
	private long intervalTime;

	// 连接服务器 获取新的版本号和其他信息
	private void checkUpdate() {
		startTime = System.currentTimeMillis();// 记住开始的时间
		new Thread() {
			public void run() {
				// 消息对象
				Message message = Message.obtain();
				try {
					URL url = new URL(getResources().getString(
							R.string.serviceurl));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setConnectTimeout(5000);// 设置连接超时时间
					int code = conn.getResponseCode();
					if (code == 200) {
						InputStream in = conn.getInputStream();
						String content = StreamUtils.getString(in);
						JSONObject json = new JSONObject(content);
						serverVersionCode = json.getInt("versioncode");
						newVersionDesc = json.getString("desc");
						dowmloadUrl = json.getString("apkpath");
						System.out.println(serverVersionCode);
						if (serverVersionCode > clientVersionCode) {
							message.what = SHOW_DiALOF;
						} else {
							// 直接进入主界面
							message.what = Load_MAINUI;
						}
					} else {
						message.what = Load_MAINUI;
						System.out.println("错误代码 10005，请联系客服");
						UiUtils.showToast(SplashActivity.this,
								"错误代码 10005，请联系客服");
					}
				} catch (MalformedURLException e) {
					message.what = Load_MAINUI;
					System.out.println("错误代码 10001，请联系客服");
					UiUtils.showToast(SplashActivity.this, "错误代码 10001，请联系客服");
					e.printStackTrace();
				} catch (NotFoundException e) {
					message.what = Load_MAINUI;
					UiUtils.showToast(SplashActivity.this, "错误代码 10002，请联系客服");
					System.out.println("错误代码 10002，请联系客服");
					e.printStackTrace();
				} catch (IOException e) {
					message.what = Load_MAINUI;
					UiUtils.showToast(SplashActivity.this, "错误代码 10003，请联系客服");
					System.out.println("错误代码 10003，请联系客服");
					e.printStackTrace();
				} catch (JSONException e) {
					message.what = Load_MAINUI;
					UiUtils.showToast(SplashActivity.this, "错误代码 10004，请联系客服");
					System.out.println("错误代码 10004，请联系客服");
					e.printStackTrace();
				} finally {
					intervalTime = System.currentTimeMillis() - startTime;// 计算出界面运行的时间
					// 如果时间少于两秒 就让线程睡够两秒
					if (intervalTime < 2000) {
						try {
							Thread.sleep(2000 - intervalTime);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					// 发送消息
					handler.sendMessage(message);
				}
			}
		}.start();
	}
}
