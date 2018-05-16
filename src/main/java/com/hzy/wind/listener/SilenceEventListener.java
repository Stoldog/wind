package com.hzy.wind.listener;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.hzy.wind.base.BaseListener;
import com.hzy.wind.base.entity.BasePacket;

/**
 * Created by EduHzy-019 on 2018-05-16.
 */
public class SilenceEventListener extends BaseListener {
    @Override
    public void onData(SocketIOClient client, BasePacket data, AckRequest ackSender) throws Exception {

    }
}
