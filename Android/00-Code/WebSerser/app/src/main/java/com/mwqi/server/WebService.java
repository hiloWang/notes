package com.mwqi.server;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.widget.Toast;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

import java.io.File;

/**
 * Created by mwqi on 2014/5/30.
 */
public class WebService extends Service {

    private Server server;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        startForeground(9999, new Notification());
        startServer();
    }

    @Override
    public void onDestroy() {
        stopServer();
        super.onDestroy();
    }

    private void startServer() {
        if (server != null) {
            Toast.makeText(this, "服务器已经开启", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new StartRunnable()).start();
    }

    private void stopServer() {
        if (server != null) {
            new Thread(new StopRunnable()).start();
        }
    }

    class StartRunnable implements Runnable {
        @Override
        public void run() {
            try {
                File JETTY_DIR = new File(Environment.getExternalStorageDirectory(), "jetty");
                // Set jetty.home
                System.setProperty("jetty.home", JETTY_DIR.getAbsolutePath());

                // ipv6 workaround for froyo
                System.setProperty("java.net.preferIPv6Addresses", "false");

                server = new Server(8090);
                // server.setHandler(new DefaultHandler());
                ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
                contextHandler.setContextPath("/");
                server.setHandler(contextHandler);
                ServlertConfig.config(contextHandler);

                server.start();
                server.join();
                Toast.makeText(WebService.this, "服务器启动", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                server = null;
                e.printStackTrace();
                Toast.makeText(WebService.this, "服务器启动失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class StopRunnable implements Runnable {
        @Override
        public void run() {
            try {
                server.stop();
                server = null;
                Toast.makeText(WebService.this, "服务器关闭", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(WebService.this, "服务器关闭失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
