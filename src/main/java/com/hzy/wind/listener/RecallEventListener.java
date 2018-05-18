package com.hzy.wind.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import io.jsonwebtoken.Claims;

/**
 * Created by EduHzy-019 on 2018-05-18.
 */
public class RecallEventListener extends TangtBaseListener{
    @Override
    public void onData(SocketIOClient socketIOClient, BasePacket basePacket, AckRequest ackRequest) throws Exception {
        //获取操作者Token的信息
        Claims claims = getClaimsByToken(socketIOClient.getHandshakeData().getSingleUrlParam("token"));
        //判断权限
        if(!checkIsPowerAndSend(socketIOClient,claims)){
            return;
        }
        //群发消息
        basePacket.setPower(isPowerByClaims(claims));
        basePacket.setType(MesType.SYSTEM.getTypeCode());
        broadcastMes(Event.RECALL,socketIOClient,getRoomIdByClaims(claims),basePacket);
    }
}
