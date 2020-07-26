package com.hzy.wind.entity;

public class UserData {
    private int userId;
    private String cuid;
    private String userName;
    private long enterTime;
    private boolean isSilent;
    private int isPower;

    public String getCuid() {
        return cuid;
    }

    public void setCuid(String cuid) {
        this.cuid = cuid;
    }

    public int getIsPower() {
        return isPower;
    }

    public void setIsPower(int isPower) {
        this.isPower = isPower;
    }

    public long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(long enterTime) {
        this.enterTime = enterTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isSilent() {
        return isSilent;
    }

    public void setSilent(boolean silent) {
        isSilent = silent;
    }
}
