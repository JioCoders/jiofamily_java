package com.jiocoders.java.jiofamily.networkmodel.response;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.networkmodel.response.data.UserData;

public class ResponseUserDetail extends ResponseBase {

    UserData userDetail = new UserData();

    public UserData getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserData userDetail) {
        this.userDetail = userDetail;
    }
}
