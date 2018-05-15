package com.hzy.wind.type;

/**
 * Created by EduHzy-019 on 2018-05-15.
 */
public enum MesType {
    GENERAL(0,"普通消息"),
    HIGH_LIGHT(1,"高亮消息"),
    TOP(2,"置顶消息"),
    NOTE(4,"笔记消息"),
    SYSTEM(8,"系统消息"),
    START(16,"初始消息"),
    END(32,"结束消息"),
    SILENCE_ALL(64,"全体禁言消息"),
    UNSILENCE_ALL(128,"全体禁言消息"),
    HISTORY_NOTE(256,"历史笔记消息"),
    ERROR(-1,"");

    private int typeCode;
    private String typeName;

    MesType(int typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public static MesType getByCode(int typeCode) {
        for(MesType mesType:MesType.values()) {
            if(mesType.getTypeCode()==typeCode) {return mesType;}
        }
        return MesType.ERROR;
    }

    public static boolean checkCode(int codeSum,int typeCode) {
        return (codeSum & typeCode)==typeCode?true:false;
    }
}
