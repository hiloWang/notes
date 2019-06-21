package com.ztiany.androidipc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class TcpServerService extends Service {

    private volatile boolean isServiceDestroy = false;

    public TcpServerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initServer();
    }

    private void initServer() {
        new Thread(
                new Runnable() {

                    @Override
                    public void run() {
                        ServerSocket mServerSocket;
                        try {
                            mServerSocket = new ServerSocket(8888);
                        } catch (IOException e) {
                            e.printStackTrace();
                            return;
                        }
                        while (!isServiceDestroy) {
                            try {
                                final Socket clientSocket = mServerSocket.accept();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            responseClient(clientSocket);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }).start();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        ).start();
    }

    private void responseClient(Socket clientSocket) throws IOException {
        BufferedReader bufferedInputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintStream printStream = new PrintStream(new BufferedOutputStream(clientSocket.getOutputStream()));

        printStream.println("你好吗");

        while (!isServiceDestroy) {

            String str = bufferedInputStream.readLine();

            Log.d("TcpServerService", str);
            if (str == null) {
                break;
            }
            printStream.println(mSgs[new Random(mSgs.length).nextInt()]);
        }

        printStream.close();
        bufferedInputStream.close();
        clientSocket.close();
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public String[] mSgs = {
            "你好吗？"
            , "草泥马"
            , "哈哈 ，我很帅"
            , "哈哈，你这俄日刀子"
            , "你大爷的"
    };
}
