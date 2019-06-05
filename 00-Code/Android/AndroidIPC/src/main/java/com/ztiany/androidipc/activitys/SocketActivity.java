package com.ztiany.androidipc.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ztiany.androidipc.R;
import com.ztiany.androidipc.service.TcpServerService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketActivity extends AppCompatActivity {

    private Socket mCliect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socket);
        startService(new Intent(this, TcpServerService.class));
        new Thread(new Runnable() {
            @Override
            public void run() {
                doConnection();
            }


        }).start();
    }


    private void doConnection() {
        Socket socket = null;
        while (socket == null) {
            try {
                socket = new Socket("localhost", 8888);
                mCliect = socket;
                OutputStream outputStream = mCliect.getOutputStream();
                PrintWriter out = new PrintWriter(new OutputStreamWriter(outputStream));
                out.print("哈哈哈");
                BufferedReader s = new BufferedReader(new InputStreamReader(mCliect.getInputStream()));
                Log.d("SocketActivity", s.readLine());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }

}
