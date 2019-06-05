package com.ztiany.bionic.tcpudp;

import android.os.Bundle;
import android.widget.EditText;

import com.ztiany.bionic.R;


public class EchoClientActivity extends AbstractEchoActivity {

    private EditText ipEdit;
    private EditText messageEdit;

    public EchoClientActivity() {
        super(R.layout.activity_echo_client);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ipEdit = findViewById(R.id.ip_edit);
        messageEdit = findViewById(R.id.message_edit);
    }

    protected void onStartButtonClicked() {
        String ip = ipEdit.getText().toString();
        Integer port = getPort();
        String message = messageEdit.getText().toString();
        if ((0 != ip.length()) && (port != null) && (0 != message.length())) {
            ClientTask clientTask = new ClientTask(ip, port, message);
            clientTask.start();
        }
    }

    private native void nativeStartTcpClient(String ip, int port, String message) throws Exception;

    private native void nativeStartUdpClient(String ip, int port, String message) throws Exception;

    private class ClientTask extends AbstractEchoTask {
        private final String ip;
        private final int port;
        private final String message;

        public ClientTask(String ip, int port, String message) {
            this.ip = ip;
            this.port = port;
            this.message = message;
        }

        protected void onBackground() {
            logMessage("Starting client.");
            try {
                nativeStartUdpClient(ip, port, message);
            } catch (Throwable e) {
                logMessage(e.getMessage());
            }
            logMessage("Client terminated.");
        }
    }
}
