package com.jiocoders.java.jiofamily.networkmodel.response;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.networkmodel.response.data.LoginData;

public class ResponseLogin extends ResponseBase {
    public LoginData userData = new LoginData();

    public LoginData getUserData() {
        return userData;
    }

    public void setUserData(LoginData userData) {
        this.userData = userData;
    }
}