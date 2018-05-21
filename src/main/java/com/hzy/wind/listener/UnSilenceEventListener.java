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
 * Created by EduHzy-019 on 2018-05-18.
 */
public class UnSilenceEventListener extends TangtBaseListener{
    @Override
    public void onData(SocketIOClient socketIOClient, BasePacket basePacket, AckRequest ackRequest) throws Exception {
        //获取操作者Token的信息
        Claims claims = getClaimsByToken(socketIOClient.getHandshakeData().getSingleUrlParam("token"));
        //判断权限
        if(!checkIsPowerAndSend(socketIOClient,claims)){
            return;
        }
        Integer userId = getUserIdByClaims(claims);
        String userName = getUserNameByClaims(claims);
        Long roomId = getRoomIdByClaims(claims);
        SilenceData silenceData = JSON.parseObject(basePacket.getContent(),SilenceData.class);
        //判断是否需要全体禁言，进行不同的业务操作
        if(MesType.UNSILENCE_ALL.getTypeCode()==basePacket.getType()){
            doUnSilenceAll(socketIOClient,roomId);
        }else {
            doUnSilence(socketIOClient,silenceData.getClientUUID());
        }
        //发送群体消息
        broadcastMes(Event.UNSILENCE,socketIOClient,getRoomIdByClaims(claims),new BasePacket(MesType.SYSTEM,basePacket.getContent(),userName,userId));
    }

    /**
     * 单独取消禁言操作
     *  1.修改Client的权限符
     *  2.向堂堂网发送请求
     * @param socketIOClient
     * @param clientUUID
     */
    private void doUnSilence(SocketIOClient socketIOClient,String clientUUID){
        //获取需要取消禁言的客户端
        SocketIOClient silenceClient = socketIOClient.getNamespace().getClient(UUID.fromString(clientUUID));
        Claims silenceClaims = getClaimsByToken(silenceClient.getHandshakeData().getSingleUrlParam("token"));
        List<String> paramList = new ArrayList<>();
        //修改需要取消禁言的客户端的token权限符
        paramList.add(JwtUtil.createJWT(silenceClaims,secretKey,"role",Role.VISITOR.getName()));
        silenceClient.getHandshakeData().getUrlParams().put("token",paramList);

        //向堂堂网发送请求
        String resUrl = "";
        Map<String,Object> params = new HashMap<>();
        try {
            String tangtData = HttpClientUtil.httpPostRequest(resUrl,params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            socketIOClient.disconnect();
        }
    }

    /**
     * 全体取消禁言业务操作
     *  1.修改房间下所有Client的权限符
     *  2.向堂堂网发送请求
     * @param socketIOClient
     * @param roomId
     */
    private void doUnSilenceAll(SocketIOClient socketIOClient,Long roomId){
        SocketIONamespace socketIONamespace = socketIOClient.getNamespace();
        for (UUID uuid : socketIONamespace.getRoomClient(roomId.toString())) {
            SocketIOClient ioClient = socketIONamespace.getClient(uuid);
            Claims claims1 = getClaimsByToken(ioClient.getHandshakeData().getSingleUrlParam("token"));
            List<String> params = new ArrayList<>();
            //修改需要取消禁言的客户端的token权限符
            params.add(JwtUtil.createJWT(claims1,secretKey,"role",Role.VISITOR.getName()));
            ioClient.getHandshakeData().getUrlParams().put("token",params);
        }
        //向堂堂网发送请求
        String resUrl = "";
        Map<String,Object> params = new HashMap<>();
        try {
            String tangtData = HttpClientUtil.httpPostRequest(resUrl,params);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            socketIOClient.disconnect();
        }
    }
}
