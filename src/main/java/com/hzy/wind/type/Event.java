package com.hzy.wind.type;

/**
 * Created by EduHzy-019 on 2018-05-15.
 *
 */
public enum Event {
    MESSAGE(0,"MESSAGE","交流"),
    CHANGE(1,"CHANGE","修改命令"),
    SYSTEM(32,"SYSTEM","系统消息"),
    IMAGE(64,"IMAGE","图片"),
    ERROR(-1,"ERROR","错误消息")
    ;
    private int code;
    private String name;
    private String description;

    Event(int code, String name,String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public static Event getByCode(int code){
        for (Event event : Event.values()) {
            if(event.code==code){
                return event;
            }
        }
        return Event.ERROR;
    }
}
