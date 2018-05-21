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
        //获取token
        String token = client.getHandshakeData().getSingleUrlParam("token");
        Claims claims = getClaimsByToken(token);
        //判断是否被禁言
        if(checkSilenceAndSend(client,claims)){
            return;
        }
        //判断是否高亮
        boolean isPower = isPowerByClaims(claims);
        String roomStr = client.getHandshakeData().getSingleUrlParam("roomId");
        //设置群发消息的结构
        data.setPower(isPower);
        data.setSendMan(getUserNameByClaims(claims));
        data.setSendManId(getUserIdByClaims(claims).intValue());
        data.setType((isPower?MesType.HIGH_LIGHT.getTypeCode():MesType.GENERAL.getTypeCode()));
        //广播消息
        broadcastMes(Event.MESSAGE,client,Long.valueOf(roomStr),data);
    }
}
