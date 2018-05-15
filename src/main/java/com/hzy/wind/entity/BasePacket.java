package com.hzy.wind.entity;

import com.hzy.wind.type.Command;
import com.hzy.wind.type.MesType;

import java.util.UUID;

/**
 * Created by EduHzy-019 on 2018-05-15.
 */
public class BasePacket {
    private String uuid;//消息唯一ID
    private int commandCode;//消息的动作
    private int type;//消息的类型 0为普通消息
    private String content;//消息的内容
    private String sendMan;
    private Integer sendManId;
    private boolean isPower;


    //无参构造
    public BasePacket() {
        this.uuid = UUID.randomUUID().toString();
    }

    //带参构造
    public BasePacket(int commandCode, int type, String content,String sendMan,Integer sendManId) {
        this.uuid = UUID.randomUUID().toString();
        this.commandCode = commandCode;
        this.type = type;
        this.content = content;
        this.sendMan = sendMan;
        this.sendManId = sendManId;
    }

    //带参构造
    public BasePacket(Command commandCode, MesType type, String content, String sendMan, Integer sendManId) {
        this.uuid = UUID.randomUUID().toString();
        this.commandCode = commandCode.getTypeCode();
        this.type = type.getTypeCode();
        this.content = content;
        this.sendMan = sendMan;
        this.sendManId = sendManId;
    }



    public Integer getSendManId() {
        return sendManId;
    }

    public void setSendManId(Integer sendManId) {
        this.sendManId = sendManId;
    }

    public boolean isPower() {
        return isPower;
    }

    public void setPower(boolean isPower) {
        this.isPower = isPower;
    }

    public String getSendMan() {
        return sendMan;
    }

    public void setSendMan(String sendMan) {
        this.sendMan = sendMan;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getUuid() {
        return uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public int getCommandCode() {
        return commandCode;
    }
    public void setCommandCode(int commandCode) {
        this.commandCode = commandCode;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "BasePacket{" +
                "uuid='" + uuid + '\'' +
                ", commandCode=" + commandCode +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", sendMan='" + sendMan + '\'' +
                ", sendManId=" + sendManId +
                ", isPower=" + isPower +
                '}';
    }
}
