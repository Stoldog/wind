package com.hzy.wind.core.pojo;

/**
 * Created by EduHzy-019 on 2018-05-16.
 */
public class TokenPOJO {
    private Integer userId;
    private String userName;
    private String role;
    private Long roomId;
    private long TTLMillis;

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public long getTTLMillis() {
        return TTLMillis;
    }

    public void setTTLMillis(long TTLMillis) {
        this.TTLMillis = TTLMillis;
    }
}
