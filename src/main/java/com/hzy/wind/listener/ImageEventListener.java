package com.hzy.wind.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import io.jsonwebtoken.Claims;

/**
 * Created by EduHzy-019 on 2018-05-28.
 */
public class ImageEventListener extends TangtBaseListener{
    @Override
    public void onData(SocketIOClient socketIOClient, BasePacket basePacket, AckRequest ackRequest) throws Exception {
        //获取token
        String token = socketIOClient.getHandshakeData().getSingleUrlParam("token");
        Claims claims = getClaimsByToken(token);
        //判断是否被禁言
        if(checkSilenceAndSend(socketIOClient,claims)){
            return;
        }
        //判断是否高亮
        boolean isPower = isPowerByClaims(claims);
        String roomStr = socketIOClient.getHandshakeData().getSingleUrlParam("roomId");
        //设置群发消息的结构
        basePacket.setPower(isPower);
        basePacket.setSendMan(getUserNameByClaims(claims));
        basePacket.setSendManId(getUserIdByClaims(claims).intValue());
        basePacket.setType((isPower?MesType.HIGH_LIGHT.getTypeCode():MesType.GENERAL.getTypeCode()));
        //广播消息
        broadcastMes(Event.IMAGE,socketIOClient,Long.valueOf(roomStr),basePacket);
    }
}
