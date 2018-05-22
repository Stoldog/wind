package com.hzy.wind.base.entity;

import com.hzy.wind.type.MesType;

import java.util.UUID;

/**
 * Created by EduHzy-019 on 2018-05-15.
 */
public class BasePacket {
    private String uuid;//消息唯一ID
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
    public BasePacket(int type, String content, String sendMan, Integer sendManId) {
        this.uuid = UUID.randomUUID().toString();
        this.type = type;
        this.content = content;
        this.sendMan = sendMan;
        this.sendManId = sendManId;
    }

    //带参构造
    public BasePacket(MesType type, String content, String sendMan, Integer sendManId) {
        this.uuid = UUID.randomUUID().toString();
        this.type = type.getTypeCode();
        this.content = content;
        this.sendMan = sendMan;
        this.sendManId = sendManId;
    }

    //带参构造
    public BasePacket(MesType type, String content, String sendMan, Integer sendManId,boolean isPower) {
        this.uuid = UUID.randomUUID().toString();
        this.type = type.getTypeCode();
        this.content = content;
        this.sendMan = sendMan;
        this.sendManId = sendManId;
        this.isPower = isPower;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendMan() {
        return sendMan;
    }

    public void setSendMan(String sendMan) {
        this.sendMan = sendMan;
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

    public void setPower(boolean power) {
        isPower = power;
    }

    @Override
    public String toString() {
        return "BasePacket{" +
                "uuid='" + uuid + '\'' +
                ", type=" + type +
                ", content='" + content + '\'' +
                ", sendMan='" + sendMan + '\'' +
                ", sendManId=" + sendManId +
                ", isPower=" + isPower +
                '}';
    }
}
