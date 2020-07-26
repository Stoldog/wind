package com.hzy.wind.type;

/**
 * Created by EduHzy-019 on 2018-05-18.
 */
public enum Role {
    VISITOR(1,"VISITOR","访问用户"),
    SILENT_MAN(2,"SILENT_MAN","被禁言用户"),
    ROOM_ADMIN(4,"ROOM_ADMIN","房间管理员"),
    ERROR(-1,"",""),
    ;
    private int code;
    private String name;
    private String description;

    Role(int code, String name, String description) {
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

    public static Role getByCode(int code){
        for (Role role : Role.values()) {
            if(role.getCode()==code){
                return role;
            }
        }
        return Role.ERROR;
    }

    public static Role getByName(String name){
        for (Role role : Role.values()) {
            if(role.getName().equals(name)){
                return role;
            }
        }
        return Role.ERROR;
    }
}
