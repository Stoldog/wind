package com.hzy.wind.listener;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.entity.SilenceData;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import com.hzy.wind.type.Role;
import com.hzy.wind.utils.HttpClientUtil;
import com.hzy.wind.utils.JwtUtil;
import io.jsonwebtoken.Claims;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by EduHzy-019 on 2018-05-16.
 */
public class SilenceEventListener extends TangtBaseListener {
    @Override
    public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {
        //获取操作者Token的信息
        Claims claims = getClaimsByToken(client.getHandshakeData().getSingleUrlParam("token"));
        //判断权限
        if(!checkIsPowerAndSend(client,claims)){
            return;
        }
        Integer userId = getUserIdByClaims(claims);
        String userName = getUserNameByClaims(claims);
        Long roomId = getRoomIdByClaims(claims);
        SilenceData silenceData = JSON.parseObject(data.getContent(),SilenceData.class);
        //判断是否需要全体禁言，进行不同的业务操作
        if(MesType.SILENCE_ALL.getTypeCode()==data.getType()){
            doSilenceAll(client,roomId);
        }else {
            doSilence(client,silenceData.getClientUUID());
        }
        //发送群体消息
        broadcastMes(Event.SILENCE,client,getRoomIdByClaims(claims),new BasePacket(MesType.SYSTEM,data.getContent(),userName,userId));
    }

    /**
     * 单独禁言操作
     *  1.修改Client的权限符
     *  2.向堂堂网发送请求
     * @param socketIOClient
     * @param clientUUID
     */
    private void doSilence(SocketIOClient socketIOClient,String clientUUID) throws UnsupportedEncodingException {
        //获取需要禁言的客户端
        SocketIOClient silenceClient = socketIOClient.getNamespace().getClient(UUID.fromString(clientUUID));
        Claims silenceClaims = getClaimsByToken(silenceClient.getHandshakeData().getSingleUrlParam("token"));
        List<String> paramList = new ArrayList<>();
        String tangtHost = socketIOClient.getHandshakeData().getHttpHeaders().get("tangtHost");
        //修改需要禁言的客户端的token权限符
        paramList.add(JwtUtil.createJWT(silenceClaims,secretKey,"role",Role.SILENT_MAN.getName()));
        silenceClient.getHandshakeData().getUrlParams().put("token",paramList);
        //向堂堂网发送请求
        StringBuilder resUrl = new StringBuilder(tangtHost).append("/res/course/updateSilence.jspx");
        resUrl.append("?roomId=")
                .append(getRoomIdByClaims(silenceClaims))
                .append("&userId=")
                .append(getUserIdByClaims(silenceClaims).longValue())
                .append("&silence=")
                .append(1);

        String tangtData = HttpClientUtil.httpGetRequest(resUrl.toString());
    }

    /**
     * 全体禁言业务操作
     *  1.修改房间下所有Client的权限符
     *  2.向堂堂网发送请求
     * @param socketIOClient
     * @param roomId
     */
    private void doSilenceAll(SocketIOClient socketIOClient,Long roomId){
        //获取名称空间
        SocketIONamespace socketIONamespace = socketIOClient.getNamespace();
        String tangtHost = socketIOClient.getHandshakeData().getHttpHeaders().get("tangtHost");
        //获取房间下的所有客户端
        for (UUID uuid : socketIONamespace.getRoomClient(roomId.toString())) {
            SocketIOClient ioClient = socketIONamespace.getClient(uuid);
            Claims claims1 = getClaimsByToken(ioClient.getHandshakeData().getSingleUrlParam("token"));
            //如果为房间管理员 则跳过
            if(getRoleByClaims(claims1).getName().equals(Role.ROOM_ADMIN.getName())){
                continue;
            }
            List<String> paramList = new ArrayList<>();
            //修改需要取消禁言的客户端的token权限符
            paramList.add(JwtUtil.createJWT(claims1,secretKey,"role",Role.SILENT_MAN.getName()));
            ioClient.getHandshakeData().getUrlParams().put("token",paramList);
        }
        //向堂堂网发送请求
        StringBuilder resUrl = new StringBuilder(tangtHost).append("/res/course/updateSilence.jspx");
        resUrl.append("?roomId=")
                .append(roomId)
                .append("&silence=")
                .append(1);

        String tangtData = HttpClientUtil.httpGetRequest(resUrl.toString());
    }
}
