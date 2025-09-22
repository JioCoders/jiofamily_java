package com.jiocoders.java.jiofamily.controller;

import com.jiocoders.java.jiofamily.base.ResponseBase;
import com.jiocoders.java.jiofamily.entity.Message;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestAddUser;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestResetPass;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestUpdateUser;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestValidateUser;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseUserDetail;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseUserList;
import com.jiocoders.java.jiofamily.service.api.AdminService;
import com.jiocoders.java.jiofamily.swagger.AdminInfo;
import com.jiocoders.java.jiofamily.utils.ApiConstant;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;

import static com.jiocoders.java.jiofamily.utils.ApiConstant.ADMIN_SERVICE;
import static com.jiocoders.java.jiofamily.utils.ApiConstant.DETAILS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController implements AdminInfo {

    @Override
    @GetMapping("/health")
    public String message() {
        return Message.getHealth();
    }

    @Value("${cc.welcomeMessage: Welcome to Admin Controllers}")
    String welcomeMessage;

    @GetMapping("/")
    public String welcome() {
        return welcomeMessage;
    }

    @Resource(name = ADMIN_SERVICE)
    private AdminService adminService;

    /**
     * Register a new employee with its detail
     *
     * @param request [employee info]
     * @param token
     * @return status code and a response message
     */
    @Override
    @PostMapping(ApiConstant.USER_ADD)
    public ResponseEntity<ResponseBase> addUser(@RequestBody RequestAddUser request,
            @RequestHeader(AUTHORIZATION) String token) {
        return adminService.addUser(request, token);
    }

    /**
     * Update employee API
     *
     * @param request
     * @param token
     * @return response
     */
    @Override
    @PostMapping(ApiConstant.USER_UPDATE)
    public ResponseEntity<ResponseBase> updateUser(@RequestBody RequestUpdateUser request,
            @RequestHeader(AUTHORIZATION) String token) {
        return adminService.updateUser(request, token);
    }

    /**
     * Get list of all active employees(users)
     *
     * @param token
     * @return response
     */
    @Override
    @PostMapping(ApiConstant.USER_LIST)
    public ResponseEntity<ResponseUserList> getUserList(@RequestHeader(AUTHORIZATION) String token) {
        return adminService.getUserList(token);
    }

    /**
     * Post request to get user details information in the system.
     *
     * @param id
     * @return
     */
    @Override
    @PostMapping(DETAILS)
    public ResponseEntity<ResponseUserDetail> detailUser(@RequestBody int id,
            @RequestHeader(AUTHORIZATION) String token) {
        return adminService.detailUser(id, token);
    }

    /**
     * Reset password by its employee id
     *
     * @param request (employeeId, password)
     * @param token
     * @return
     */
    @Override
    @PostMapping(ApiConstant.RESET_PASSWORD)
    public ResponseEntity<ResponseBase> resetPassword(@RequestHeader(AUTHORIZATION) String token,
            @RequestBody RequestResetPass request) {
        return adminService.resetUserPassword(token, request);
    }

    /**
     * Validate and Active any user by its user id and otp verification of email and
     * sms
     *
     * @param request (userId, otp)
     * @param token
     * @return
     */
    @PostMapping(ApiConstant.USER_VALIDATE)
    public ResponseEntity<ResponseBase> validateUser(@RequestHeader(AUTHORIZATION) String token,
            @RequestBody RequestValidateUser request) {
        return adminService.validateUser(token, request);
    }
    // /**
    // * Active or de-active any user by its user id
    // *
    // * @param request (userId, isActive)
    // * @param token
    // * @return
    // */
    // @PostMapping(ApiConstant.USER_ACTIVE)
    // public ResponseEntity<ResponseBase> activeUser(@RequestHeader(AUTHORIZATION)
    // String token,
    // @RequestBody RequestActivate request) {
    // return adminService.activeUserStatus(token, request);
    // }
}
