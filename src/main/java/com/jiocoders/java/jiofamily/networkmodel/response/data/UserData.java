package com.jiocoders.java.jiofamily.networkmodel.response.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserData {
    int userId;

    List<Integer> interestIds;

    String firstName;

    String lastName;

    String emailId;

    String mobileNo;

    CommonData department;

    CommonData userLocationData;

    char gender;

    long dob;

    String bio;

    boolean isActive;

    long createdAt;
}