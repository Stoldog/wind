package com.hzy.wind.launcher;

import com.alibaba.fastjson.JSON;
import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.entity.UserData;
import com.hzy.wind.listener.*;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import com.hzy.wind.type.Role;
import com.hzy.wind.utils.HttpClientUtil;
import com.hzy.wind.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;


/**
 * Created by EduHzy-019 on 2018-05-15.
 */
public class ChatLauncher {
    //常量
    public static String nameSpace = "/tangt";
    public static String tangtHost;

    //日志
    private static Logger logger = LoggerFactory.getLogger(ChatLauncher.class);
    public static void main(String[] args) throws MalformedURLException {

        //初始化参数
        URL url = new URL(args[0]);
        tangtHost = args[1];
        //服务器配置
        Configuration config = new Configuration();
        config.setHostname(url.getHost());
        config.setPort(url.getPort());
        config.setMaxHttpContentLength(3*1024*1024);//最大3M的本文长度
        config.setMaxFramePayloadLength(3*1024*1024);//最大3M的本文长度
        final SocketIOServer server = new SocketIOServer(config);
        //服务器监听事件
        SocketIONamespace socketIONamespace = server.addNamespace(nameSpace);
        //监听连接事件
        socketIONamespace.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                //1. 解密
                String tokenStr = client.getHandshakeData().getSingleUrlParam("token");
                String roomStr = client.getHandshakeData().getSingleUrlParam("roomId");
                //使用默认的密钥
                Claims claims = JwtUtil.parseJWT(tokenStr);
                if(claims==null){
                    logger.error("Token Error: "+tokenStr);
                    client.disconnect();
                    return;
                }
                // 2. 从TOKEN中获取房间号
                Long roomId = claims.get("room_id",Long.class);
                if(roomId == null || roomId == 0){
                    logger.error("Token Parse Error: roomId is null or 0");
                    return;
                }
                Long clientUserId = claims.get("user_id",Long.class).longValue();
                boolean isPower = Role.ROOM_ADMIN.getName().equalsIgnoreCase(claims.get("role",String.class));
                Date enterTime = new Date();
                //增加权限及其他的参数
                client.getHandshakeData().getHttpHeaders().add("tangtHost",tangtHost);
                client.getHandshakeData().getHttpHeaders().add("isPower",(isPower?1:0));
                client.getHandshakeData().getHttpHeaders().add("enterTime",enterTime.getTime());
                client.getHandshakeData().getHttpHeaders().add("userId",clientUserId);
                client.joinRoom(roomId.toString());
                // 3. 判断是否重复加入


                // 4. 广播
                String welcomeStr = "【"+claims.get("unique_name",String.class)+"】进入了直播室"  ;
                //向客户端单独发送
                //client.sendEvent(Event.SYSTEM.getName(),new BasePacket(MesType.START,JSON.toJSONString(sendData),"系统消息",0));

                //群发welcome消息
                socketIONamespace.getRoomOperations(roomId.toString()).sendEvent(Event.SYSTEM.getName(),new BasePacket(MesType.START,JSON.toJSONString(welcomeStr),"系统消息",0));
            }
        });
        //监听断开连接事件
        socketIONamespace.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                String tokenStr = client.getHandshakeData().getSingleUrlParam("token");
                //使用默认的密钥
                Claims claims = JwtUtil.parseJWT(tokenStr);
                Long roomId = claims.get("room_id",Long.class);


                //广播
                String endStr = "【"+claims.get("unique_name",String.class)+"】 已离开"  ;
                socketIONamespace.getRoomOperations(roomId.toString()).sendEvent(Event.SYSTEM.getName(),new BasePacket(MesType.END,JSON.toJSONString(endStr),"系统消息",0));
            }
        });
        //监听其他事件

        server.start();
    }
}
