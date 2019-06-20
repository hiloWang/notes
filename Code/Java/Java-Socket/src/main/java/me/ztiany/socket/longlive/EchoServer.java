package me.ztiany.socket.longlive;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/27 16:17
 */
final class EchoServer implements Closeable {

    private final int mPort;
    private ExecutorService mClientExecutor;
    private ExecutorService mServerExecutor;
    private volatile ServerSocket mServerSocket;

    @Override
    public void close() {
        mClientExecutor.shutdownNow();
        mServerExecutor.shutdownNow();
        Utils.close(mServerSocket);
    }

    interface Callback {
        void onSuccess();

        void onError(Exception e);
    }

    EchoServer(int port) {
        this.mPort = port;
        mClientExecutor = Executors.newFixedThreadPool(4);
        mServerExecutor = Executors.newSingleThreadExecutor();
    }

    private void initServerSocket(int port) throws IOException {
        mServerSocket = new ServerSocket(port);
        while (true) {
            Socket client = mServerSocket.accept();
            handleClient(client);
        }
    }

    private void handleClient(Socket client) {
        mClientExecutor.execute(() -> processClient(client));
    }

    private void processClient(Socket client) {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            InputStream inputStream = client.getInputStream();
            OutputStream outputStream;
            outputStream = client.getOutputStream();
            bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            byte[] buffer = new byte[1024];
            int readLen;
            while ((readLen = bufferedInputStream.read(buffer)) != -1) {
                System.out.println("server receive data readLend = " + readLen);
                bufferedOutputStream.write(buffer, 0, readLen);
                bufferedOutputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.close(bufferedInputStream);
            Utils.close(bufferedOutputStream);
        }
    }


    void start(@NotNull Callback callback) {
        mServerExecutor.execute(() -> {
            try {
                initServerSocket(mPort);
                callback.onSuccess();
            } catch (IOException e) {
                e.printStackTrace();
                callback.onError(e);
            }
        });
    }

}
