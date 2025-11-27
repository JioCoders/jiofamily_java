package com.jiocoders.java.jiofamily.networkmodel.request;

import java.util.List;

import lombok.Data;

@Data
public class RequestAddUser {
    List<Integer> interestIds;
    String firstName;
    String lastName;
    int userLocationId;
    String emailId;
    String password;
    String mobile;
    int departmentId;
    String employeeId;

    char gender;
    long dob;
}
