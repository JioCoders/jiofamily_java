package com.jiocoders.java.jiofamily.networkmodel.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Data
public class UserData {
    int userId;
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