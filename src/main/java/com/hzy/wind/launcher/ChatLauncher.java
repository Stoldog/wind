package com.hzy.wind.launcher;

import com.corundumstudio.socketio.*;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.hzy.wind.entity.BasePacket;
import com.hzy.wind.type.Command;
import com.hzy.wind.type.MesType;

/**
 * Created by EduHzy-019 on 2018-05-15.
 */
public class ChatLauncher {
    public static void main(String[] args) throws InterruptedException {
        String tempToken = "MOTHER_FUCKER";
        Configuration config = new Configuration();
        config.setHostname("localhost");
        config.setPort(9092);

        final SocketIOServer server = new SocketIOServer(config);
        SocketIONamespace socketIONamespace = server.addNamespace("/tangt");
        //监听连接事件
        socketIONamespace.addConnectListener(new ConnectListener() {
            @Override
            public void onConnect(SocketIOClient client) {
                //1.token，这里做解析JWT，设置权限等操作
                String tokenStr = client.getHandshakeData().getSingleUrlParam("token");
                if(!tempToken.equals(tokenStr)){
                    System.out.println("token error : "+tokenStr);
                    client.disconnect();
                    return;
                }
                client.joinRoom("TT");
                socketIONamespace.getBroadcastOperations().sendEvent("chatevent",new BasePacket(Command.MESSAGE, MesType.GENERAL, "已加入房间TT!","系统",0));
            }
        });
        //监听其他的事件


        server.start();

        Thread.sleep(Integer.MAX_VALUE);

        server.stop();
    }
}
