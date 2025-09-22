package com.jiocoders.java.jiofamily.networkmodel.response;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.networkmodel.response.data.UserData;

import java.util.ArrayList;
import java.util.List;

public class ResponseUserList extends ResponseBase {

    List<UserData> userList = new ArrayList<>();

    public List<UserData> getUserList() {
        return userList;
    }

    public void setUserList(List<UserData> userList) {
        this.userList = userList;
    }
}
