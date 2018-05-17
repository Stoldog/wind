package com.hzy.wind.launcher;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.listener.MessageEventListener;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import com.hzy.wind.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;


/**
 * Created by EduHzy-019 on 2018-05-15.
 */
public class ChatLauncher {
    //常量
    public static final String NAME_SPACE = "/tangt";
    public static final String secretKey = "1&$OAlAsh!wJCIJU";
    //日志
    private static Logger logger = LoggerFactory.getLogger(ChatLauncher.class);
    public static void main(String[] args) throws InterruptedException {

        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);
        //服务器监听事件
        SocketIONamespace socketIONamespace = server.addNamespace(NAME_SPACE);
        //监听连接事件
        socketIONamespace.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                //1. 解密
                String tokenStr = client.getHandshakeData().getSingleUrlParam("token");
                String roomStr = client.getHandshakeData().getSingleUrlParam("roomId");
                Claims claims = JwtUtil.parseJWT(tokenStr,secretKey);
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
                // 3. 判断是否重复加入
                /*for (SocketIOClient socketIOClient : socketIONamespace.getAllClients()) {
                    //如果客户端为自己 则跳过
                    if(socketIOClient.getSessionId().toString().equals(client.getSessionId().toString())){continue;}
                    Claims claims1 = JwtUtil.parseJWT(socketIOClient.getHandshakeData().getSingleUrlParam("token"),secretKey);
                    boolean checkToken = claims1.get("user_id",Long.class).longValue()==claims.get("user_id",Long.class).longValue();
                    boolean checkRoomId = socketIOClient.getHandshakeData().getSingleUrlParam("roomId").equals(roomStr);
                    //如果token一致，且roomId一致，则之前的(不同的浏览器)客户端退出
                    if(!checkToken || !checkRoomId){
                        continue;
                    }
                    socketIOClient.disconnect();
                }*/
                for (String s : client.getAllRooms()) {
                    System.out.println("Old Room: "+s);
                    if(s.equals(NAME_SPACE)){continue;}
                    client.leaveRoom(s);
                    client.sendEvent("leaveroom");
                }
                client.joinRoom(roomId.toString());
                // 4. 广播
                String welcomeStr = "有新用户："+claims.get("unique_name",String.class)+"加入当前房间  【"+roomId+"】  ！总人数： "+socketIONamespace.getAllClients().size()+"------"+client.getSessionId();
                socketIONamespace.getRoomOperations(roomId.toString()).sendEvent(Event.SYSTEM.getName(),new BasePacket(MesType.START,welcomeStr,"系统消息",0));
            }
        });
        //监听断开连接事件
        socketIONamespace.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {
                String tokenStr = client.getHandshakeData().getSingleUrlParam("token");
                Claims claims = JwtUtil.parseJWT(tokenStr,secretKey);
                Long roomId = claims.get("room_id",Long.class);
                String endStr = "有用户："+claims.get("unique_name",String.class)+"退出当前房间  【"+roomId+"】  ！总人数： "+socketIONamespace.getAllClients().size()+"------"+client.getSessionId();
                socketIONamespace.getRoomOperations(roomId.toString()).sendEvent(Event.SYSTEM.getName(),new BasePacket(MesType.END,endStr,"系统消息",0));
            }
        });
        //监听其他的事件
        socketIONamespace.addEventListener("join", BasePacket.class, new DataListener<BasePacket>() {
            @Override
            public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {

            }
        });
        socketIONamespace.addEventListener(Event.MESSAGE.getName(), BasePacket.class, new MessageEventListener());
        socketIONamespace.addEventListener(Event.SILENCE.getName(), BasePacket.class, new DataListener<BasePacket>() {
            @Override
            public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {

            }
        });
        socketIONamespace.addEventListener(Event.UNSILENCE.getName(), BasePacket.class, new DataListener<BasePacket>() {
            @Override
            public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {

            }
        });
        socketIONamespace.addEventListener(Event.TOP.getName(), BasePacket.class, new DataListener<BasePacket>() {
            @Override
            public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {

            }
        });
        socketIONamespace.addEventListener(Event.RECALL.getName(), BasePacket.class, new DataListener<BasePacket>() {
            @Override
            public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {

            }
        });
        socketIONamespace.addEventListener(Event.NOTE.getName(), BasePacket.class, new DataListener<BasePacket>() {
            @Override
            public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {

            }
        });

        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
