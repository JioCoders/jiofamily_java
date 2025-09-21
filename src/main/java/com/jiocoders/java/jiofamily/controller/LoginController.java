package com.jiocoders.java.jiofamily.controller;

import com.jiocoders.java.jiofamily.networkmodel.request.RequestLogin;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseLogin;
import com.jiocoders.java.jiofamily.service.api.LoginService;
import com.jiocoders.java.jiofamily.swagger.LoginInfo;
import com.jiocoders.java.jiofamily.utils.ApiConstant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import static com.jiocoders.java.jiofamily.utils.ApiConstant.LOGIN_SERVICE;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/login")
public class LoginController implements LoginInfo{

    @Resource(name = LOGIN_SERVICE)
    private LoginService loginService;

    /**
     * Login service with user email id and password
     * 
     * @param requestLogin (email id, password)
     * @return user details and jwt token
     */
    @Override
    @PostMapping(ApiConstant.USER_LOGIN)
    public ResponseEntity<ResponseLogin> loginUser(@RequestBody RequestLogin requestLogin) {
        return loginService.loginUser(requestLogin);
    }

}
