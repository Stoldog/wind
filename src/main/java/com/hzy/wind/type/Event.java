package com.hzy.wind.type;

/**
 * Created by EduHzy-019 on 2018-05-15.
 *
 */
public enum Event {
    MESSAGE(0,"MESSAGE","交流"),
    SILENCE(1,"SILENCE","禁言"),
    UNSILENCE(2,"UNSILENCE","取消禁言"),
    TOP(4,"TOP","置顶"),
    RECALL(8,"RECALL","撤回"),
    NOTE(16,"NOTE","笔记"),
    SYSTEM(32,"SYSTEM","系统消息"),
    IMAGE(64,"IMAGE","图片"),
    NOTE_IMAGE(128,"NOTE_IMAGE","笔记图片"),
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
