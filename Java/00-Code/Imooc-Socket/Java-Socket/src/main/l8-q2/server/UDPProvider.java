package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.UUID;

import clink.utils.ByteUtils;
import clink.utils.CloseUtils;
import foo.TCPConstants;
import foo.UDPConstants;


/**
 * UDP（认为是服务端），监听网络上的UDP包，向按照协议发送包的另一端提供 TCP 连接信息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 21:17
 */
class UDPProvider {

    private static Provider PROVIDER_INSTANCE;

    static void start(int portServer) {
        stop();
        String sn = UUID.randomUUID().toString();
        PROVIDER_INSTANCE = new Provider(sn, portServer);
        PROVIDER_INSTANCE.start();
    }

    static void stop() {
        if (PROVIDER_INSTANCE != null) {
            PROVIDER_INSTANCE.exit();
            PROVIDER_INSTANCE = null;
        }
    }

    private static class Provider extends Thread {

        private byte[] mSn;
        private int mPort;
        private volatile boolean mDone;
        private byte[] mBuffer;
        private DatagramSocket mDatagramSocket;

        Provider(String sn, int portServer) {
            this.mSn = sn.getBytes();
            this.mPort = portServer;
            mBuffer = new byte[128];
        }

        @Override
        public void run() {
            try {
                //构建UDP
                mDatagramSocket = new DatagramSocket(mPort);
                //用于接收数据
                DatagramPacket receivePack = new DatagramPacket(mBuffer, mBuffer.length);

                while (!mDone) {
                    //接收数据
                    mDatagramSocket.receive(receivePack);
                    String clientIp = receivePack.getAddress().getHostAddress();
                    int clientPort = receivePack.getPort();
                    int receiveLength = receivePack.getLength();
                    byte[] receiveData = receivePack.getData();

                    //判断接收的数据是否合法，因为UDP可以广播，只有符合规范的数据才需要处理
                    boolean isValid = receiveLength >=
                            (UDPConstants.HEADER.length/*通用头*/ + 2/*两个字节的cmd*/ + 4/*4个字节的端口*/)/*长度校验*/
                            && ByteUtils.startsWith(receiveData, UDPConstants.HEADER);/*头部校验*/

                    System.out.println("UDPProvider receive form ip:" + clientIp + "\tport:" + clientPort + "\tdataValid:" + isValid);

                    //无效则继续
                    if (!isValid) {
                        continue;
                    }
                    //有效则解析
                    int start = UDPConstants.HEADER.length;
                    int cmd = receiveData[start++] << 8//short 高位
                            | (receiveData[start++] & 0XFF);////short 低位

                    int responsePort = receiveData[start++] << 24//int 32-24
                            | ((receiveData[start++] & 0XFF) << 16)// 24-16
                            | ((receiveData[start++] & 0XFF) << 8)// 16-8
                            | (receiveData[start] & 0XFF);// 8-0

                    //1 表示获取端口号，port 是回传的端口号
                    if (cmd == 1 && responsePort > 0) {
                        ByteBuffer byteBuffer = ByteBuffer.wrap(mBuffer);
                        byteBuffer.put(UDPConstants.HEADER);//通用头部
                        byteBuffer.putShort((short) 2);//2 表示回传
                        byteBuffer.putInt(TCPConstants.PORT_SERVER);//TCP的端口
                        byteBuffer.put(mSn);
                        int len = byteBuffer.position();

                        /*发送包*/
                        mDatagramSocket.send(new DatagramPacket(mBuffer, len, receivePack.getAddress(), responsePort));
                        System.out.println("UDPProvider response to:" + clientIp + "\tport:" + responsePort + "\tdataLen:" + len);
                    } else {
                        System.out.println("UDPProvider receive cmd nonsupport; cmd:" + cmd + "\tport:" + clientPort);
                    }

                }


            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                close();
            }
        }

        private void close() {
            CloseUtils.close(mDatagramSocket);
        }

        private void exit() {
            mDone = true;
            close();
        }
    }

}
