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
        SocketIONamespace socketIONamespace = server.addNamespace(NAME_SPACE);
        //监听连接事件
        socketIONamespace.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                // 1. TOKEN，这里需要解析JWT
                String tokenStr = client.getHandshakeData().getSingleUrlParam("token");
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
                // 3. 判断是否重复加入（如果重复，后入房间的覆盖先入的）
                for (String s : client.getAllRooms()) {
                    if(!s.equals(roomId.toString())){continue;}
                    client.getNamespace().getClient(client.getSessionId()).leaveRoom(s);
                }
                client.joinRoom(roomId.toString());
                // 4. 广播
                String welcomeStr = "有新用户："+claims.get("unique_name",String.class)+"加入当前房间！"+client.getSessionId();
                socketIONamespace.getBroadcastOperations().sendEvent(Event.SYSTEM.getName(),new BasePacket(MesType.START,welcomeStr,"系统消息",0));
            }
        });
        //监听断开连接事件
        socketIONamespace.addDisconnectListener(new DisconnectListener() {
            @Override
            public void onDisconnect(SocketIOClient client) {

            }
        });
        //监听其他的事件
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
