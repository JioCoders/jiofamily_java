package com.jiocoders.java.jiofamily.entity;

import java.io.Serializable;

import static com.jiocoders.java.jiofamily.utils.DbConstant.TABLE_USER;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

// @TypeDefs({
// @TypeDef(name = "integer-array", typeClass = IntArrayType.class) })

@Data
@Entity
@Table(name = TABLE_USER)
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

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

}