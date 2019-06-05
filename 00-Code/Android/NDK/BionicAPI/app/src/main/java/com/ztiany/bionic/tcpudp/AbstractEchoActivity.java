package com.ztiany.bionic.tcpudp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ztiany.bionic.R;

public abstract class AbstractEchoActivity extends AppCompatActivity implements OnClickListener {

    protected EditText portEdit;
    protected Button startButton;
    protected ScrollView logScroll;
    protected TextView logView;
    private final int layoutID;

    public AbstractEchoActivity(int layoutID) {
        this.layoutID = layoutID;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutID);

        portEdit = findViewById(R.id.port_edit);
        startButton = findViewById(R.id.start_button);
        logScroll = findViewById(R.id.log_scroll);
        logView = findViewById(R.id.log_view);

        startButton.setOnClickListener(this);
    }

    public void onClick(View view) {
        if (view == startButton) {
            onStartButtonClicked();
        }
    }

    protected abstract void onStartButtonClicked();

    protected Integer getPort() {
        Integer port;
        try {
            port = Integer.valueOf(portEdit.getText().toString());
        } catch (NumberFormatException e) {
            port = null;
        }

        return port;
    }

    protected void logMessage(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                logMessageDirect(message);
            }
        });
    }

    protected void logMessageDirect(final String message) {
        logView.append(message);
        logView.append("\n");
        logScroll.fullScroll(View.FOCUS_DOWN);
    }


    protected abstract class AbstractEchoTask extends Thread {

        private final Handler handler;

        public AbstractEchoTask() {
            handler = new Handler();
        }

        protected void onPreExecute() {
            startButton.setEnabled(false);
            logView.setText("");
        }

        public synchronized void start() {
            onPreExecute();
            super.start();
        }

        public void run() {
            onBackground();
            handler.post(new Runnable() {
                public void run() {
                    onPostExecute();
                }
            });
        }

        protected abstract void onBackground();

        protected void onPostExecute() {
            startButton.setEnabled(true);
        }
    }

}
