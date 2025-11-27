package com.jiocoders.java.jiofamily.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.jiocoders.java.jiofamily.utils.DbConstant.TABLE_USER;

// @TypeDefs({
// @TypeDef(name = "integer-array", typeClass = IntArrayType.class) })

@Data
@Entity
@Table(name = TABLE_USER)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // Stored as "1,2,3"
    @Column(name = "interest_ids", length = 200)
    private String interestIds;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email_id", nullable = false)
    private String emailId;

    @Column(name = "mobile_no")
    private String mobileNo;

    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    @Column(name = "is_super_admin", nullable = false)
    private Boolean isSuperAdmin;

    @Column(name = "department_id", nullable = false)
    private int departmentId;

    @Column(name = "emp_id")
    private String employeeId;

    @Column(name = "user_location")
    private int userLocation;

    @Column(name = "gender")
    private char gender;

    @Column(name = "dob")
    private long dob;

    @Column(name = "bio")
    private String bio;

    @Column(name = "company_id")
    private int companyId;

    @Column(name = "password")
    private String password;

    @Column(name = "otp")
    private String otp;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private long createdAt;

    // Convert DB -> List<Integer>
    public List<Integer> getInterestIdList() {
        if (interestIds == null || interestIds.isEmpty())
            return List.of();
        return Arrays.stream(interestIds.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();
    }

    // Convert List<Integer> -> DB value
    public void setInterestIdList(List<Integer> ids) {
        this.interestIds = ids == null || ids.isEmpty()
                ? null
                : ids.stream().map(String::valueOf).collect(Collectors.joining(","));
    }

}