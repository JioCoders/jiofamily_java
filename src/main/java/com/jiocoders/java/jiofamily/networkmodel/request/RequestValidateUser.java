package com.jiocoders.java.jiofamily.networkmodel.request;

public class RequestValidateUser {
    int userId;
    String otp;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }
}
