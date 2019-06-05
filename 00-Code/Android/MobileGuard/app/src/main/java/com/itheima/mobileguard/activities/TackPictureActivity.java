package com.itheima.mobileguard.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.ui.CameraPreview;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public class TackPictureActivity extends Activity {
	private CameraPreview mPreview;
	private Camera camera;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 检查照相机是否存在
		 */
		if(!checkCameraHardware(this)){
			finish();
		}
		camera = getCameraInstance();
		if(camera == null	){
			finish();//不存在 
		}
		setContentView(R.layout.activity_tackpicture);
//		Parameters parameters = camera.getParameters();
//		parameters.set 使用默认的照相机设置
		// Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, camera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        new Thread(){
        	public void run(){
        		try {
					Thread.sleep(3000);
					camera.takePicture(null, null, new PictureCallback() {
						public void onPictureTaken(byte[] data, Camera camera) {
								try {
									final File file = new File(Environment.getExternalStorageDirectory(),"picture.jpg");
									FileOutputStream fos = new FileOutputStream(file);
									fos.write(data);
									fos.close();
									HttpUtils utils = new HttpUtils();
									RequestParams rp = new RequestParams();
									rp.addBodyParameter("filepath", file);
									utils.send(HttpMethod.POST,
										    "http://192.168.1.199:8080/FileUploadForAndroid/servlet/FileUploadServlet",
										    rp,
										    new RequestCallBack<String>() {
												public void onFailure(
														HttpException arg0,
														String arg1) {
													finish();//偷拍完毕就关闭activity
												}
												public void onSuccess(
														ResponseInfo<String> arg0) {
													file.delete();
													finish();//偷拍完毕就关闭activity
												}});
//									camera.startPreview();//拍完一直 就会预览界面就会停止掉  重新开始预览
//									finish();//偷拍完毕就关闭activity
									
								} catch (IOException e) {
									e.printStackTrace();
								}
						}
					});
					
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        }.start();
	}

	
	
	
	//点击事件
	public void capture(View v){
			/**
			 * 参数 快门操作  位图不经压缩的图片 JPEG图片回调
			 */
		camera.takePicture(null, null, new PictureCallback() {
			public void onPictureTaken(byte[] data, Camera camera) {
					try {
						FileOutputStream fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(),"picture.jpg"));
						fos.write(data);
						fos.close();
						camera.startPreview();//拍完一直 就会预览界面就会停止掉  重新开始预览
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		});
	}
	@Override
	protected void onDestroy() {
		camera.release();//activity退出时 释放资源
		super.onDestroy();
	}
	
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}

	/** A safe way to get an instance of the Camera object.一个安全的方法去访问硬件 */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	    	// 传入参数1  则是打开 前置摄像头
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
}
