package com.jiocoders.java.jiofamily.networkmodel.request;

public class RequestActivate {
    int userId;
    boolean isActive;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
