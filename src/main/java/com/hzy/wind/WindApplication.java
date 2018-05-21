package com.hzy.wind;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIONamespace;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.hzy.wind.base.entity.BasePacket;
import com.hzy.wind.launcher.ChatLauncher;
import com.hzy.wind.listener.*;
import com.hzy.wind.type.Event;
import com.hzy.wind.type.MesType;
import com.hzy.wind.type.Role;
import com.hzy.wind.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;

@SpringBootApplication
public class WindApplication {
    /*//常量
    public static final String NAME_SPACE = "/tangt";
    public static final String secretKey = "1&$OAlAsh!wJCIJU";

    //日志
    private static Logger logger = LoggerFactory.getLogger(ChatLauncher.class);
    @Bean
    public SocketIOServer socketIOServer(){

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
                if(socketIONamespace.getRoomClient(roomStr)!=null){
                    for (UUID uuid : socketIONamespace.getRoomClient(roomStr)) {
                        SocketIOClient roomClient = socketIONamespace.getClient(uuid);
                        Claims clientClaims = JwtUtil.parseJWT(roomClient.getHandshakeData().getSingleUrlParam("token"),secretKey);
                        if(!(clientClaims.get("user_id",Long.class).longValue()==claims.get("user_id",Long.class).longValue())){
                            continue;
                        }
                        roomClient.disconnect();
                    }
                }
                boolean isPower = Role.ROOM_ADMIN.getName().equalsIgnoreCase(claims.get("role",String.class));
                //增加权限
                client.getHandshakeData().getHttpHeaders().add("isPower",isPower);
                client.joinRoom(roomId.toString());
                //向堂堂网发送记录请求


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
                //向堂堂网发送记录请求


                //广播
                String endStr = "有用户："+claims.get("unique_name",String.class)+"退出当前房间  【"+roomId+"】  ！总人数： "+socketIONamespace.getAllClients().size()+"------"+client.getSessionId();
                socketIONamespace.getRoomOperations(roomId.toString()).sendEvent(Event.SYSTEM.getName(),new BasePacket(MesType.END,endStr,"系统消息",0));
            }
        });
        //监听其他事件
        socketIONamespace.addEventListener(Event.MESSAGE.getName(), BasePacket.class, new MessageEventListener());
        socketIONamespace.addEventListener(Event.SILENCE.getName(), BasePacket.class, new SilenceEventListener());
        socketIONamespace.addEventListener(Event.UNSILENCE.getName(), BasePacket.class, new UnSilenceEventListener());
        socketIONamespace.addEventListener(Event.TOP.getName(), BasePacket.class, new TopEventListener());
        socketIONamespace.addEventListener(Event.RECALL.getName(), BasePacket.class, new RecallEventListener());
        socketIONamespace.addEventListener(Event.NOTE.getName(), BasePacket.class, new NoteEventListener());

        server.start();
    }*/

    public static void main(String[] args) {
        SpringApplication.run(WindApplication.class, args);
    }
}
