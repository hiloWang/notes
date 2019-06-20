package server;

import clink.box.StringReceivePacket;
import foo.handler.ConnectorHandler;
import foo.handler.ConnectorStringPacketChain;

/**
 * 负责统计
 */
class ServerStatistics {

    long receiveSize;
    long sendSize;

    ConnectorStringPacketChain statisticsChain() {
        return new StatisticsConnectorStringPacketChain();
    }

    /**
     * 接收数据的责任链节点，添加到首节点之后，则可以在每次收到消息时得到回调
     * 然后可以进行接收消息的统计
     */
    private class StatisticsConnectorStringPacketChain extends ConnectorStringPacketChain {

        @Override
        protected boolean consume(ConnectorHandler handler, StringReceivePacket stringReceivePacket) {
            // 接收数据量自增
            receiveSize++;
            return false;
        }
    }

}
