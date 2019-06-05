package server;

import java.util.ArrayList;
import java.util.List;

import clink.box.StringReceivePacket;
import foo.handler.ConnectorHandler;
import foo.handler.ConnectorStringPacketChain;

/**
 * 群封装
 */
class Group {

    private final String name;
    private final GroupMessageAdapter adapter;
    private final List<ConnectorHandler> members = new ArrayList<>();

    /**
     * @param name    群名
     * @param adapter 回调接口
     */
    Group(String name, GroupMessageAdapter adapter) {
        this.name = name;
        this.adapter = adapter;
    }

    String getName() {
        return name;
    }

    /**
     * 添加某个客户端
     *
     * @param handler 客户端
     * @return 是否成功
     */
    boolean addMember(ConnectorHandler handler) {
        synchronized (members) {
            if (!members.contains(handler)) {
                members.add(handler);
                //添加一个成员之后，需要把该群消息转发链添加到该成员的消息处理链中
                handler.getStringPacketChain()
                        .appendLast(new ForwardConnectorStringPacketChain());
                System.out.println("Group[" + name + "] add new member:" + handler.getClientInfo());
                return true;
            }
        }
        return false;
    }

    /**
     * 移除某个客户端
     *
     * @param handler 客户端
     * @return 是否移除成功
     */
    boolean removeMember(ConnectorHandler handler) {
        synchronized (members) {
            if (members.remove(handler)) {
                //移除该成员后，需要把该群从该成员的消息处理链中移除。
                handler.getStringPacketChain().remove(ForwardConnectorStringPacketChain.class);
                System.out.println("Group[" + name + "] leave member:" + handler.getClientInfo());
                return true;
            }
        }
        return false;
    }


    /**
     * 进行消息转发的责任链节点
     */
    private class ForwardConnectorStringPacketChain extends ConnectorStringPacketChain {

        @Override
        protected boolean consume(ConnectorHandler handler, StringReceivePacket stringReceivePacket) {
            synchronized (members) {
                for (ConnectorHandler member : members) {
                    if (member == handler) {
                        continue;
                    }
                    //由外部发送和统计
                    adapter.sendMessageToClient(member, stringReceivePacket.getEntity());
                }
                return true;
            }
        }
    }

    /**
     * 进行消息发送的Adapter
     */
    interface GroupMessageAdapter {
        /**
         * 发送消息的接口
         *
         * @param handler 客户端
         * @param msg     消息
         */
        void sendMessageToClient(ConnectorHandler handler, String msg);
    }

}
