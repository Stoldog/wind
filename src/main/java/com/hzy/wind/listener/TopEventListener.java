package com.hzy.wind.listener;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.entity.TopData;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringEscapeUtils;

/**
 * Created by EduHzy-019 on 2018-05-18.
 */
public class TopEventListener extends TangtBaseListener{
    @Override
    public void onData(SocketIOClient socketIOClient, BasePacket basePacket, AckRequest ackRequest) throws Exception {
        //获取操作者Token的信息
        Claims claims = getClaimsByToken(socketIOClient.getHandshakeData().getSingleUrlParam("token"));
        //判断权限
        if(!checkIsPowerAndSend(socketIOClient,claims)){
            return;
        }
        TopData topData = JSON.parseObject(basePacket.getContent(),TopData.class);
        topData.setContent(StringEscapeUtils.escapeHtml4(topData.getContent()));
        //如果消息Id为空 则执行发送消息的操作 再将消息置顶
        if(topData.getMessageId() == null || topData.getMessageId().equals("")){
            BasePacket basePacket1 = new BasePacket(MesType.HIGH_LIGHT,topData.getContent(),getUserNameByClaims(claims),getUserIdByClaims(claims));
            basePacket1.setUuid(basePacket.getUuid());
            broadcastMes(Event.MESSAGE,socketIOClient,claims.get("room_id",Long.class),basePacket1);
            //将topData的消息messageid改为此条消息的uuid
            topData.setMessageId(basePacket1.getUuid());
        }
        //群发将消息置顶
        basePacket.setType(MesType.SYSTEM.getTypeCode());
        basePacket.setPower(isPowerByClaims(claims));
        basePacket.setUuid(topData.getMessageId());
        basePacket.setContent(JSON.toJSONString(topData));
        broadcastMes(Event.TOP,socketIOClient,getRoomIdByClaims(claims),basePacket);


    }
}
