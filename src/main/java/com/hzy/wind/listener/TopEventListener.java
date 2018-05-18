package com.hzy.wind.listener;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.entity.TopData;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import io.jsonwebtoken.Claims;
import org.springframework.util.StringUtils;

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
        //群发将消息置顶
        TopData topData = JSON.parseObject(basePacket.getContent(),TopData.class);
        basePacket.setType(MesType.SYSTEM.getTypeCode());
        basePacket.setPower(isPowerByClaims(claims));
        broadcastMes(Event.TOP,socketIOClient,getRoomIdByClaims(claims),basePacket);
        //如果消息Id为空 则再执行发送消息的操作
        if(topData.getMessageId() == null || StringUtils.isEmpty(topData.getMessageId())){
            broadcastMes(Event.MESSAGE,socketIOClient,claims.get("room_id",Long.class),new BasePacket(MesType.HIGH_LIGHT,topData.getContent(),getUserNameByClaims(claims),getUserIdByClaims(claims)));
        }
    }
}
