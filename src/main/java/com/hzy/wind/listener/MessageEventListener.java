package com.hzy.wind.listener;


import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import io.jsonwebtoken.Claims;


/**
 * Created by EduHzy-019 on 2018-05-16.
 * MESSAGE事件的监听器，处理消息发送的指令
 */
public class MessageEventListener extends TangtBaseListener {

    @Override
    public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {
        //判断是否为高亮消息
        String token = client.getHandshakeData().getSingleUrlParam("token");
        Claims claims = getClaimsByToken(token);
        boolean isPower = isPowerByClaims(claims);
        String roomStr = client.getHandshakeData().getSingleUrlParam("roomId");
        //设置群发消息的结构
        data.setPower(isPower);
        data.setSendMan(getUserNameByClaims(claims));
        data.setSendManId(getUserIdByClaims(claims).intValue());
        data.setType((isPower?MesType.HIGH_LIGHT.getTypeCode():MesType.GENERAL.getTypeCode()));
        //广播消息
        broadcastMes(client,Long.valueOf(roomStr),Event.MESSAGE.getName(),data);
    }
}
