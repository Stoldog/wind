package com.hzy.wind.launcher;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;


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
                String resUrl = "";
                Map<String,Object> params = new HashMap<>();
                try {
                    String tangtData = HttpClientUtil.httpPostRequest(resUrl,params);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    client.disconnect();
                }
                List<UserData> userDataList = new ArrayList<>();
                //获取当前在线人数(人员基本信息)
                for (UUID uuid : socketIONamespace.getRoomClient(roomId.toString())) {
                    Claims tempClaims = JwtUtil.parseJWT(socketIONamespace.getClient(uuid).getHandshakeData().getSingleUrlParam("token"),secretKey);
                    UserData userData = new UserData();
                    userData.setUserId(tempClaims.get("user_id",int.class));
                    userData.setUserName(tempClaims.get("unique_name",String.class));
                    userData.setSilent((tempClaims.get("role",String.class).equals(Role.SILENT_MAN.getName())?true:false));
                    userDataList.add(userData);
                }
                //获取笔记

                // 4. 广播
                String welcomeStr = "【"+claims.get("unique_name",String.class)+"】进入了直播室"  ;
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
                String resUrl = "";
                Map<String,Object> params = new HashMap<>();
                try {
                    String resData = HttpClientUtil.httpPostRequest(resUrl,params);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    client.disconnect();
                }
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
    }
}
