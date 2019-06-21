package com.ztiany.bionic.tcpudp;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.widget.EditText;

import com.ztiany.bionic.R;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 本地服务
 */
public class LocalEchoActivity extends AbstractEchoActivity {

    private EditText messageEdit;

    public LocalEchoActivity() {
        super(R.layout.activity_local_echo);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        messageEdit = findViewById(R.id.message_edit);
    }

    protected void onStartButtonClicked() {
        String name = portEdit.getText().toString();
        String message = messageEdit.getText().toString();

        if ((name.length() > 0) && (message.length() > 0)) {
            String socketName;
            if (isFilesystemSocket(name)) {
                File file = new File(getFilesDir(), name);
                socketName = file.getAbsolutePath();
            } else {
                socketName = name;
            }

            ServerTask serverTask = new ServerTask(socketName);
            serverTask.start();

            ClientTask clientTask = new ClientTask(socketName, message);
            clientTask.start();
        }
    }

    private boolean isFilesystemSocket(String name) {
        return name.startsWith("/");
    }

    private native void nativeStartLocalServer(String name) throws Exception;

    private void startLocalClient(String name, String message) throws Exception {
        LocalSocket clientSocket = new LocalSocket();
        try {
            // Set the socket namespace
            LocalSocketAddress.Namespace namespace;
            if (isFilesystemSocket(name)) {
                namespace = LocalSocketAddress.Namespace.FILESYSTEM;
            } else {
                namespace = LocalSocketAddress.Namespace.ABSTRACT;
            }

            // Construct local socket address
            LocalSocketAddress address = new LocalSocketAddress(name, namespace);

            // Connect to local socket
            logMessage("Connecting to " + name);
            clientSocket.connect(address);
            logMessage("Connected.");

            // Get message as bytes
            byte[] messageBytes = message.getBytes();

            // Send message bytes to the socket
            logMessage("Sending to the socket...");
            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(messageBytes);
            logMessage(String.format("Sent %d bytes: %s", messageBytes.length, message));

            // Receive the message back from the socket
            logMessage("Receiving from the socket...");
            InputStream inputStream = clientSocket.getInputStream();
            int readSize = inputStream.read(messageBytes);

            String receivedMessage = new String(messageBytes, 0, readSize);
            logMessage(String.format("Received %d bytes: %s", readSize, receivedMessage));

            // Close streams
            outputStream.close();
            inputStream.close();

        } finally {
            clientSocket.close();
        }
    }

    private class ServerTask extends AbstractEchoTask {

        private final String name;

        public ServerTask(String name) {
            this.name = name;
        }

        protected void onBackground() {
            logMessage("Starting server.");
            try {
                nativeStartLocalServer(name);
            } catch (Exception e) {
                logMessage(e.getMessage());
            }
            logMessage("Server terminated.");
        }
    }

    private class ClientTask extends Thread {

        private final String name;
        private final String message;

        public ClientTask(String name, String message) {
            this.name = name;
            this.message = message;
        }

        public void run() {
            logMessage("Starting client.");
            try {
                startLocalClient(name, message);
            } catch (Exception e) {
                logMessage(e.getMessage());
            }
            logMessage("Client terminated.");
        }
    }

}
