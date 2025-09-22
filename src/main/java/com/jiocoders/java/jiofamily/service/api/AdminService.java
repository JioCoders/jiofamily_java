package com.jiocoders.java.jiofamily.service.api;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.networkmodel.request.*;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseUserDetail;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseUserList;
import org.springframework.http.ResponseEntity;

public interface AdminService {

    // void refreshCache(String cacheName);

    ResponseEntity<ResponseBase> addUser(RequestAddUser request, String token);

    ResponseEntity<ResponseBase> updateUser(RequestUpdateUser request, String token);

    ResponseEntity<ResponseUserList> getUserList(String token);

    ResponseEntity<ResponseBase> resetUserPassword(String token, RequestResetPass request);

    ResponseEntity<ResponseBase> validateUser(String token, RequestValidateUser request);

    ResponseEntity<ResponseBase> activeUserStatus(String token, RequestActivate request);

    ResponseEntity<ResponseUserDetail> detailUser(int id, String token);
}
