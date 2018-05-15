package com.hzy.wind.type;

/**
 * Created by EduHzy-019 on 2018-05-15.
 */
public enum Event {
    MESSAGE(0,"交流"),
    SILENCE(1,"禁言"),
    UNSILENCE(2,"取消禁言"),
    TOP(4,"置顶"),
    RECALL(8,"撤回"),
    NOTE(16,"笔记"),
    ERROR(-1,"")
    ;
    private int code;
    private String name;

    Event(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
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
