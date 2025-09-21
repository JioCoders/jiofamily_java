package com.jiocoders.java.jiofamily.swagger;

import org.springframework.http.ResponseEntity;

import com.jiocoders.java.jiofamily.networkmodel.request.RequestLogin;
import com.jiocoders.java.jiofamily.networkmodel.response.ResponseLogin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Login", description = "the Login Api")
public interface LoginInfo {

        @Operation(summary = "Login ", description = "fetches Login and get user by email and password")
        @ApiResponses(value = {
                        @ApiResponse(responseCode = "200", description = "successful Login"),
                        @ApiResponse(responseCode = "400", description = "user not found")
        })
        ResponseEntity<ResponseLogin> loginUser(@RequestBody RequestLogin request);

}