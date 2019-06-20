package com.itheima.mobileguard;

import android.app.Application;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Date;

/**
 * 一个应用程序只会初始化一个application对象，该对象代表该应用程序
 * 维护的是当前应用程序的全部状态
 * 单一实例 单台模式
 * <p>
 * 清单文件需要配置
 *
 * @author Administrator
 */
public class MobileGuardApplication extends Application {

    /**
     * 应用程序在第一次被创建的时候调用的方法，在任何其对象创建之前执行的逻辑
     */
    public void onCreate() {
        Thread.setDefaultUncaughtExceptionHandler(new MyExceptionHandler());
        super.onCreate();
    }

    private class MyExceptionHandler implements UncaughtExceptionHandler {
        /**
         * 当发送了未捕获到异常时，不能阻止虚拟机的推出，只能在虚拟机退出之前把exception信息记录下来
         * 方便程序员修复bug
         */
        public void uncaughtException(Thread thread, Throwable ex) {
            //把一场信息保存到本地
            try {
                Date date = new Date();
                File file = new File(Environment.getExternalStorageDirectory(), "exception.log");
                PrintWriter pw = new PrintWriter(new FileWriter(file, true), true);
                System.out.println("出现了异常，但是被哥捕获了");
                //各种厂商的build的是实现可能不一样，有的字段可能没有，所以通过反射把有的字段都记录下来
                //build代表用户使用手机的各种信息 和设备的各种信息
                Field[] fields = Build.class.getDeclaredFields();
                StringBuilder sb = new StringBuilder();
                pw.println(date.toLocaleString());
                for (Field field : fields) {
                    System.out.println(field.getName() + ":::" + field.get(null));
                    pw.println(field.getName() + ":::" + field.get(null));
                }
                ex.printStackTrace(pw);
                pw.close();
                //早死早超生 杀死自己的进程 系统会认为的时意外终止 重新启动该进程
                android.os.Process.killProcess(android.os.Process.myPid());
                //原地复活
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class MyExecptionHandler implements UncaughtExceptionHandler {
        //当线程出现了未捕获的异常执行的方法。
        //不能阻止java虚拟机退出，只是在jvm退出之前， 留了一点时间， 留一个遗言
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            try {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                System.out.println("产生了异常，但是被哥给捕获了。");
                Field[] fileds = Build.class.getDeclaredFields();
                for (Field filed : fileds) {
                    System.out.println(filed.getName() + "--" + filed.get(null));
                    sw.write(filed.getName() + "--" + filed.get(null) + "\n");
                }
                ex.printStackTrace(pw);
                File file = new File(Environment.getExternalStorageDirectory(), "log.txt");
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sw.toString().getBytes());
                fos.close();
                pw.close();
                sw.close();
                //早死早超生
                android.os.Process.killProcess(android.os.Process.myPid());
                //原地复活
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    /**
     * 内存不足时调用的方法
     */
    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
