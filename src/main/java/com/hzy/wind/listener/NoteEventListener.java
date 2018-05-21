package com.hzy.wind.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import com.hzy.wind.utils.HttpClientUtil;
import io.jsonwebtoken.Claims;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by EduHzy-019 on 2018-05-18.
 */
public class NoteEventListener extends TangtBaseListener{
    @Override
    public void onData(SocketIOClient socketIOClient, BasePacket basePacket, AckRequest ackRequest) throws Exception {
        //获取操作者Token的信息
        Claims claims = getClaimsByToken(socketIOClient.getHandshakeData().getSingleUrlParam("token"));
        //判断权限
        if(!checkIsPowerAndSend(socketIOClient,claims)){
            return;
        }
        //向堂堂网发送笔记请求
        String resUrl = "";
        Map<String,Object> params = new HashMap<>();
        try {
            String tangtData = HttpClientUtil.httpPostRequest(resUrl,params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            socketIOClient.disconnect();
        }
        //群发消息
        basePacket.setPower(isPowerByClaims(claims));
        basePacket.setType(MesType.NOTE.getTypeCode());
        broadcastMes(Event.NOTE,socketIOClient,getRoomIdByClaims(claims),basePacket);
    }
}
