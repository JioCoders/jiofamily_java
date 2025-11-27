package com.jiocoders.java.jiofamily.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.jiocoders.java.jiofamily.validator.ValidInterestIds;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class UserPreferenceDTO {

    private int id;

    @JsonProperty("interestIds")
    @ValidInterestIds
    private List<Integer> interestIds;

    private List<String> interestNames;

    @JsonProperty("firstName")
    @NotNull(message = "Name is required")
    private String firstName;
    private String lastName;
    private String emailId;
    private String mobileNo;
    private Boolean isAdmin;
    private Boolean isSuperAdmin;
    private int departmentId;
    private String employeeId;
    private int userLocation;
    private char gender;
    private long dob;
    private String bio;
    private int companyId;
    private String password;
    private String otp;
    private boolean isActive;
    private long createdAt;

}
