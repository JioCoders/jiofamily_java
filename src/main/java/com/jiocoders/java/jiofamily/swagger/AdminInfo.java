package com.jiocoders.java.jiofamily.swagger;

import org.springframework.http.ResponseEntity;

import com.jiocoders.java.jiofamily.base.ResponseBase;
// import com.jiocoders.java.jiofamily.entity.User;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestAddUser;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestResetPass;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestUpdateUser;
import com.jiocoders.java.jiofamily.networkmodel.request.RequestValidateUser;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseUserDetail;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseUserList;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin", description = "the Admin Api")
public interface AdminInfo {

        @Operation(summary = "Fetch all Admin", description = "fetches all Admin entities and their data from data source")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful operation")
        })
        String message();

        @Operation(summary = "welcome message", description = "get request for welcome message")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "successfully got welcome message"),
                        @ApiResponse(responseCode = "500", description = "error found")
        })
        String welcome();

        @Operation(summary = "adds a user", description = "Adds a user to the list of users")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "201", description = "successfully added a user"),
                        @ApiResponse(responseCode = "409", description = "duplicate user")
        })
        ResponseEntity<ResponseBase> addUser(@RequestBody RequestAddUser request, String token);

        @Operation(summary = "update a user", description = "updates a user to the list of users")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successfully updated a user"),
                        @ApiResponse(responseCode = "400", description = "user not found")
        })
        ResponseEntity<ResponseBase> updateUser(@RequestBody RequestUpdateUser request, String token);

        @Operation(summary = "get a user list", description = "get the list of users")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successfully fetched a user list"),
        })
        ResponseEntity<ResponseUserList> getUserList(String token);

        @Operation(summary = "get detail of a user", description = "get a user detail by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successfully found a user details"),
                        @ApiResponse(responseCode = "400", description = "user not found")
        })
        ResponseEntity<ResponseUserDetail> detailUser(@RequestBody int id, String token);

        @Operation(summary = "reset password a user", description = "reset a user password detail by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successfully reset a user password"),
                        @ApiResponse(responseCode = "400", description = "user not found")
        })
        ResponseEntity<ResponseBase> resetPassword(String token, @RequestBody RequestResetPass request);

        @Operation(summary = "validate a user", description = "validate a user by id")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successfully validated a user"),
                        @ApiResponse(responseCode = "400", description = "user not found")
        })
        ResponseEntity<ResponseBase> validateUser(String token, @RequestBody RequestValidateUser request);
}