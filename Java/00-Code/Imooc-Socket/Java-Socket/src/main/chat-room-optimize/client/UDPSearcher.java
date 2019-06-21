package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import clink.utils.ByteUtils;
import clink.utils.CloseUtils;
import foo.constants.UDPConstants;


/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:56
 */
class UDPSearcher {

    private static final int LISTEN_PORT = UDPConstants.PORT_CLIENT_RESPONSE;

    static ServerInfo searchServer(int timeout) {
        //先开启监听
        Listener listener = null;
        CountDownLatch resultCountDownLatch = new CountDownLatch(1);
        try {
            listener = listen(resultCountDownLatch);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //启动失败，返回。
        if (listener == null) {
            return null;
        }

        //监听器启动之后，发送广播搜索TCP服务
        try {
            sendBroadcast();
        } catch (IOException e) {
            e.printStackTrace();
            //发送失败，返回。
            return null;
        }

        try {
            //等待十秒
            resultCountDownLatch.await(timeout, TimeUnit.MILLISECONDS);
            System.out.println("UDPSearcher Finished.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //获取结果并退出
        List<ServerInfo> serverInfoList = listener.getServerAndClose();
        if (serverInfoList != null && !serverInfoList.isEmpty()) {
            System.out.println("UDPSearcher search success");
            return serverInfoList.get(0);
        }
        System.out.println("UDPSearcher search fail");
        return null;
    }


    private static Listener listen(CountDownLatch receiveLatch) throws InterruptedException {
        System.out.println("UDPSearcher start listen.");
        CountDownLatch startCountDownLatch = new CountDownLatch(1);
        Listener listener = new Listener(LISTEN_PORT, startCountDownLatch, receiveLatch);
        listener.start();
        startCountDownLatch.await();
        return listener;
    }

    /*发送广播*/
    private static void sendBroadcast() throws IOException {

        try (DatagramSocket datagramSocket = new DatagramSocket()) {

            ByteBuffer byteBuffer = ByteBuffer.allocate(128);
            byteBuffer.put(UDPConstants.HEADER);//公共头部
            byteBuffer.putShort((short) 1);//命令 1 表示搜索服务器
            byteBuffer.putInt(UDPConstants.PORT_CLIENT_RESPONSE);//对方通过这个端口会送TCP服务器信息

            DatagramPacket datagramPacket = new DatagramPacket(byteBuffer.array(), byteBuffer.position() + 1);
            datagramPacket.setAddress(InetAddress.getByName("255.255.255.255"));
            datagramPacket.setPort(UDPConstants.PORT_SERVER);

            datagramSocket.send(datagramPacket);
            System.out.println("UDPSearcher sendBroadcast success");
        }
    }

    /**
     * TCP服务信息接收者
     */
    private static class Listener extends Thread {

        private final CountDownLatch mStartCountDownLatch;
        private final CountDownLatch mReceiveLatch;
        private final int mListenPort;
        private DatagramSocket mDatagramSocket;
        private final byte[] buffer = new byte[128];
        private final int mMinLen = (UDPConstants.HEADER.length/*通用头*/ + 2/*两个字节的cmd*/ + 4/*4个字节的端口*/);
        private List<ServerInfo> mInfoArrayList = new ArrayList<>();
        private volatile boolean mDone;

        Listener(int listenPort, CountDownLatch startCountDownLatch, CountDownLatch receiveLatch) {
            mStartCountDownLatch = startCountDownLatch;
            mReceiveLatch = receiveLatch;
            mListenPort = listenPort;
        }

        @Override
        public void run() {
            mStartCountDownLatch.countDown();
            /*
             *一个死循环不断地读取
             * 是try cache包裹while，还是 while包裹try cache？当发生异常后不需要再继续循环时，就使用 try cache 包裹while，，否则  while 中使用 try cache 包裹执行代码。
             * 这里发生异常就表示连接断开了或者读取超时了，没有继续循环的必要。
             */
            try {
                mDatagramSocket = new DatagramSocket(mListenPort);
                System.out.println("UDPSearcher Listener start listen");

                while (!mDone) {
                    //创建包
                    DatagramPacket receivePack = new DatagramPacket(buffer, buffer.length);
                    //接收数据包
                    mDatagramSocket.receive(receivePack);

                    //解析包，判断接收的数据是否合法，因为UDP可以广播，只有符合规范的数据才需要处理
                    int receiveLength = receivePack.getLength();
                    String ip = receivePack.getAddress().getHostAddress();
                    int port = receivePack.getPort();
                    byte[] data = receivePack.getData();

                    boolean isValid = receiveLength >= mMinLen/*长度校验*/
                            && ByteUtils.startsWith(data, UDPConstants.HEADER);/*头部校验*/

                    System.out.println("UDPSearcher receive form ip:" + ip + "\tport:" + port + "\tdataValid:" + isValid);

                    if (!isValid) {
                        continue;
                    }

                    ByteBuffer byteBuffer = ByteBuffer.wrap(data, UDPConstants.HEADER.length, receiveLength);

                    short cmd = byteBuffer.getShort();
                    int tcpPort = byteBuffer.getInt();

                    /*判断命令和端口是否合法，2是约定的命令，表示回送消息*/
                    if (cmd != 2 || tcpPort <= 0) {
                        System.out.println("UDPSearcher receive cmd:" + cmd + "\tserverPort:" + tcpPort);
                        continue;
                    }

                    String sn = new String(data, mMinLen, receiveLength - mMinLen);
                    mInfoArrayList.add(new ServerInfo(tcpPort, ip, sn));
                    mReceiveLatch.countDown();
                }

            } catch (IOException ignore) {
            } finally {
                close();
            }

            System.out.println("UDPSearcher listener finished.");
        }

        private void close() {
            mDone = true;
            CloseUtils.close(mDatagramSocket);
        }

        List<ServerInfo> getServerAndClose() {
            close();
            return mInfoArrayList;
        }
    }//Listener end

}
