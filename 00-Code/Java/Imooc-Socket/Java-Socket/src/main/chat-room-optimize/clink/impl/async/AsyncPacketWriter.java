package clink.impl.async;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Collection;
import java.util.HashMap;

import clink.core.Frame;
import clink.core.IoArgs;
import clink.core.ReceivePacket;
import clink.frame.AbsReceiveFrame;
import clink.frame.CancelReceiveFrame;
import clink.frame.HeartbeatReceiveFrame;
import clink.frame.ReceiveEntityFrame;
import clink.frame.ReceiveFrameFactory;
import clink.frame.ReceiveHeaderFrame;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/27 22:52
 */
class AsyncPacketWriter implements Closeable {

    private final PacketProvider mPacketProvider;
    private IoArgs mIoArgs = new IoArgs();

    /**
     * 用于存储当前正在接收的包，key为包的唯一标识，value记录了正在接受包的必须信息。
     */
    private final HashMap<Short, PacketModel> mPacketMap = new HashMap<>();

    /**
     * 同一个时刻，只能接受一个帧的数据
     */
    private volatile Frame frameTemp;

    AsyncPacketWriter(PacketProvider packetProvider) {
        mPacketProvider = packetProvider;
    }

    /**
     * 构建一份数据容纳封装
     * 当前帧如果没有则返回至少6字节长度的IoArgs，
     * 如果当前帧有，则返回当前帧未消费完成的区间
     *
     * @return IoArgs
     */
    synchronized IoArgs takeIoArgs() {
        //如果frameTemp为null，则先解析帧的头六个字节，用于确定是什么类型的数据，用于接下来的帧的构建
        if (frameTemp == null) {
            mIoArgs.limit(Frame.FRAME_HEADER_LENGTH);
        } else {
            mIoArgs.limit(frameTemp.getConsumableLength());
        }
        return mIoArgs;
    }

    synchronized void consumeIoArgs(IoArgs args) {
        if (frameTemp == null) {
            Frame temp;
            do {
                // 还有未消费数据，则重复构建帧，即不断使用帧把IoArgs中是数据消费掉
                temp = buildNewFrame(args);//根据数据类型创建对应的帧
            } while (temp != null && args.remained());
            //情况1：遇到一个取消帧
            //情况2：args被消费完，正好构造了一个正常的帧
            //情况3：args没有被消费完，构造了一个正常的帧

            //处理情况1
            if (temp == null) {//temp == null 说明被取消了或者args没消费完了，都直接返回
                // 最终消费数据完成，但没有可消费区间，则直接返回
                return;
            }

            //处理情况2
            frameTemp = temp;
            if (!args.remained()) {
                // 没有数据，则直接返回
                return;
            }
        }

        //处理情况3
        // 确保此时currentFrame一定不为null
        Frame currentFrame = frameTemp;
        do {
            try {
                if (currentFrame.handle(args)) { //currentFrame.handle(args)用于从args中读取数据，只有frame被填满了，才会返回true。
                    //currentFrame.handle(args) == true 说明当前currentFrame被填满了

                    if (currentFrame instanceof ReceiveHeaderFrame) {
                        // Packet 头帧消费完成，则根据头帧信息构建接收的Packet
                        ReceiveHeaderFrame headerFrame = (ReceiveHeaderFrame) currentFrame;

                        //根据头帧信息创建一个包
                        ReceivePacket packet = mPacketProvider.takePacket(
                                headerFrame.getPacketType(),
                                headerFrame.getPacketLength(),
                                headerFrame.getPacketHeaderInfo());

                        //把包的标识添加到缓存容器中
                        appendNewPacket(headerFrame.getBodyIdentifier(), packet);

                    } else if (currentFrame instanceof ReceiveEntityFrame) {
                        // Packet 实体帧消费完成，则将当前帧消费到Packet
                        completeEntityFrame((ReceiveEntityFrame) currentFrame);
                    }

                    // 接收完成后，直接退出循环，如果还有未消费数据则交给外层调度
                    frameTemp = null;
                    break;
                }//currentFrame.handle(args) end
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (args.remained());
    }

    /**
     * 当某Packet实体帧消费完成时调用
     *
     * @param frame 已经消费的帧信息
     */
    private void completeEntityFrame(ReceiveEntityFrame frame) {
        synchronized (mPacketMap) {//针对mPacketMap的同步
            short identifier = frame.getBodyIdentifier();
            int length = frame.getBodyLength();
            PacketModel model = mPacketMap.get(identifier);

            if (model == null) {
                return;
            }

            model.unreceivedLength -= length;

            //如果包对应的 model 中未消费的长度为0了，则说明该包已经接收完毕了
            //应该将其从map中移除，并且通知外部该包已经接收完毕
            if (model.unreceivedLength <= 0) {
                mPacketProvider.completedPacket(model.packet, true);
                mPacketMap.remove(identifier);
            }
        }
    }

    /**
     * 添加一个新的Packet到当前缓冲区
     *
     * @param identifier Packet标志
     * @param packet     Packet
     */
    private void appendNewPacket(short identifier, ReceivePacket packet) {
        synchronized (mPacketMap) {//针对mPacketMap的同步
            PacketModel model = new PacketModel(packet);
            mPacketMap.put(identifier, model);
        }
    }

    /**
     * 根据args创建新的帧
     * 若当前解析的帧是取消帧，则直接进行取消操作，并返回null
     *
     * @param args IoArgs
     * @return 返回新的帧
     */
    private Frame buildNewFrame(IoArgs args) {
        AbsReceiveFrame frame = ReceiveFrameFactory.createInstance(args);
        if (frame instanceof CancelReceiveFrame) {
            //取消则直接返回null
            cancelReceivePacket(frame.getBodyIdentifier());
            return null;
        } else if (frame instanceof HeartbeatReceiveFrame) {
            mPacketProvider.onReceivedHeartbeat();
            return null;
        } else if (frame instanceof ReceiveEntityFrame) {
            //如果是实体帧，则应该为其创建Channel
            WritableByteChannel channel = getPacketChannel(frame.getBodyIdentifier());
            ((ReceiveEntityFrame) frame).bindPacketChannel(channel);
        } /*else if (frame instanceof ReceiveHeaderFrame) {
            //否则 frame 就是头帧，直接返回即可
        }*/
        return frame;
    }

    /**
     * 获取Packet对应的输出通道，用以设置给帧进行数据传输
     * 因为关闭当前map的原因，可能存在返回NULL
     *
     * @param identifier Packet对应的标志
     * @return 通道
     */
    private WritableByteChannel getPacketChannel(short identifier) {
        synchronized (mPacketMap) {
            PacketModel model = mPacketMap.get(identifier);
            return model == null ? null : model.channel;
        }
    }

    /**
     * 取消某Packet继续接收数据
     *
     * @param identifier Packet标志
     */
    private void cancelReceivePacket(short identifier) {
        synchronized (mPacketMap) {
            PacketModel model = mPacketMap.get(identifier);
            if (model != null) {
                ReceivePacket packet = model.packet;
                mPacketProvider.completedPacket(packet, false);
            }
        }
    }

    @Override
    public void close() {
        synchronized (mPacketMap) {
            Collection<PacketModel> values = mPacketMap.values();
            for (PacketModel value : values) {
                mPacketProvider.completedPacket(value.packet, false);
            }
            mPacketMap.clear();
        }
    }

    /**
     * Packet提供者
     */
    interface PacketProvider {

        /**
         * 拿Packet操作
         *
         * @param type       Packet类型
         * @param length     Packet长度
         * @param headerInfo Packet headerInfo
         * @return 通过类型，长度，描述等信息得到一份接收Packet
         */
        ReceivePacket takePacket(byte type, long length, byte[] headerInfo);

        /**
         * 结束一份Packet
         *
         * @param packet    接收包
         * @param isSucceed 是否成功接收完成
         */
        void completedPacket(ReceivePacket packet, boolean isSucceed);

        /**
         * 当收到一个心跳包时触发
         */
        void onReceivedHeartbeat();

    }

    /*针对接收包信息的封装*/
    static class PacketModel {
        final ReceivePacket packet;
        final WritableByteChannel channel;
        volatile long unreceivedLength;

        PacketModel(ReceivePacket<?, ?> packet) {
            this.packet = packet;
            this.channel = Channels.newChannel(packet.open());
            this.unreceivedLength = packet.getLength();
        }

    }

}
