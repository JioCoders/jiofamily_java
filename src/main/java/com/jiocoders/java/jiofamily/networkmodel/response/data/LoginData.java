package com.jiocoders.java.jiofamily.networkmodel.response.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LoginData {
    int userId;
    String firstName;
    String lastName;
    String emailId;
    String mobileNo;
    int department;
    LocationData userLocationData;
    int companyId;
    char gender;
    long dob;
    String bio;
    String token;

}
