package com.jiocoders.java.jiofamily.service.api;

import com.jiocoders.java.jiofamily.networkmodel.request.RequestLogin;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseLogin;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<ResponseLogin> loginUser(RequestLogin requestLogin);
}
