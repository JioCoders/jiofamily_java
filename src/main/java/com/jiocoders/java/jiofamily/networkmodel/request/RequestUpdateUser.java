package com.jiocoders.java.jiofamily.networkmodel.request;

import java.util.List;

import lombok.Data;

@Data
public class RequestUpdateUser {
    int userId;
    List<Integer> interestIds;
    String firstName;
    String lastName;
    String emailId;
    String mobileNo;
    char gender;
    long dob;
    String bio;
    int departmentId;
    String employeeId;
    int userLocationId;
}
