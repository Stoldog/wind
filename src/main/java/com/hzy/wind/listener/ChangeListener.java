package com.hzy.wind.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.type.Event;

import java.util.UUID;

/**
 * Created by EduHzy-019 on 2018-06-07.
 */
public class ChangeListener implements DataListener<BasePacket> {
    @Override
    public void onData(SocketIOClient socketIOClient, BasePacket basePacket, AckRequest ackRequest) throws Exception {
        String roomStr = socketIOClient.getHandshakeData().getSingleUrlParam("roomId");
        String ownUserId = socketIOClient.getHandshakeData().getHttpHeaders().get("userId");
        for (UUID uuid : socketIOClient.getNamespace().getRoomClient(roomStr)) {
            //获取客户端
            SocketIOClient socketIOClient1 = socketIOClient.getNamespace().getClient(uuid);
            String userId = socketIOClient1.getHandshakeData().getHttpHeaders().get("userId");
            //如果等于空跳过
            if(userId==null || userId.equals("")){continue;}
            //如果userId不相跳过
            if(!ownUserId.equals(userId)){continue;}
            socketIOClient1.sendEvent(Event.CHANGE.getName(),basePacket);
        }
    }
}
