package com.ztiany.bionic.tcpudp;


import com.ztiany.bionic.R;

public class EchoServerActivity extends AbstractEchoActivity {

    public EchoServerActivity() {
        super(R.layout.activity_echo_server);
    }

    protected void onStartButtonClicked() {
        Integer port = getPort();
        if (port != null) {
            ServerTask serverTask = new ServerTask(port);
            serverTask.start();
        }
    }

    private native void nativeStartTcpServer(int port) throws Exception;

    private native void nativeStartUdpServer(int port) throws Exception;

    private class ServerTask extends AbstractEchoTask {

        private final int port;

        public ServerTask(int port) {
            this.port = port;
        }

        protected void onBackground() {
            logMessage("Starting server.");

            try {
                nativeStartUdpServer(port);
            } catch (Exception e) {
                logMessage(e.getMessage());
            }

            logMessage("Server terminated.");
        }
    }
}
