package com.hzy.wind.listener;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.entity.NoteData;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import com.hzy.wind.utils.HttpUtil;
import io.jsonwebtoken.Claims;

/**
 * Created by EduHzy-019 on 2018-05-28.
 */
public class NoteImageEventListener extends TangtBaseListener{
    @Override
    public void onData(SocketIOClient socketIOClient, BasePacket basePacket, AckRequest ackRequest) throws Exception {
        //获取操作者Token的信息
        Claims claims = getClaimsByToken(socketIOClient.getHandshakeData().getSingleUrlParam("token"));
        //判断权限
        if(!checkIsPowerAndSend(socketIOClient,claims)){
            return;
        }
        String tangtHost = socketIOClient.getHandshakeData().getHttpHeaders().get("tangtHost");
        NoteData noteData = JSON.parseObject(basePacket.getContent(),NoteData.class);
        //向堂堂网发送笔记请求
        StringBuilder resUrl = new StringBuilder(tangtHost).append("/res/course/addNote.jspx");
        resUrl.append("?roomId=")
                .append(getRoomIdByClaims(claims))
                .append("&userId=")
                .append(getUserIdByClaims(claims).longValue())
                .append("&playTimeline=")
                .append(noteData.getPlayTimeline().longValue())
                .append("&content=")
                .append(noteData.getContent());


        String tangtData = HttpUtil.doGet(resUrl.toString());

        //群发消息
        basePacket.setPower(isPowerByClaims(claims));
        basePacket.setType(MesType.NOTE.getTypeCode());
        basePacket.setSendMan(getUserNameByClaims(claims));
        basePacket.setSendManId(getUserIdByClaims(claims));
        broadcastMes(Event.NOTE_IMAGE,socketIOClient,getRoomIdByClaims(claims),basePacket);
    }
}
