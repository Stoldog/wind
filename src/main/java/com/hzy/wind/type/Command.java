package com.hzy.wind.type;

/**
 * Created by EduHzy-019 on 2018-05-15.
 */
public enum Command {
    MESSAGE(0,"交流"),
    SILENCE(1,"禁言"),
    UNSILENCE(2,"取消禁言"),
    TOP(4,"置顶"),
    RECALL(8,"撤回"),
    NOTE(16,"笔记"),
    ERROR(-1,"")
    ;

    private int typeCode;
    private String typeName;

    Command(int typeCode, String typeName) {
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public int getTypeCode() {
        return typeCode;
    }

    public String getTypeName() {
        return typeName;
    }

    public static Command getByCode(int typeCode) {
        for(Command command:Command.values()) {
            if(command.getTypeCode()==typeCode) {return command;}
        }
        return Command.ERROR;
    }

    public static boolean checkCode(int codeSum,int typeCode) {
        return (typeCode & codeSum)==typeCode?true:false;
    }

}
