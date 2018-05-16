package com.hzy.wind.base;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.hzy.wind.base.entity.BasePacket;

/**
 * Created by EduHzy-019 on 2018-05-16.
 */
public abstract class BaseListener implements DataListener<BasePacket> {
    public static final String secretKey = "1&$OAlAsh!wJCIJU";

    /**
     * 离开客户端所在的所有房间
     * @param client
     */
    public void leaveAllRoom(SocketIOClient client){
        for (String s : client.getAllRooms()) {
            client.leaveRoom(s);
        }
    }

    /**
     * 广播事件
     * @param client
     * @param basePacket
     */
    public void broadcastMes(SocketIOClient client,String eventName,BasePacket basePacket){
        client.getNamespace().getBroadcastOperations().sendEvent(eventName,basePacket);
    }

}
