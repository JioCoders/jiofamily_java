package com.jiocoders.java.jiofamily.entity;

import jakarta.persistence.*;
import lombok.Data;

import static com.jiocoders.java.jiofamily.utils.DbConstant.TABLE_DEPARTMENT;

@Data
@Entity
@Table(name = TABLE_DEPARTMENT)
public class Department {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int departmentId;

    @Column(name = "name")
    private String departmentName;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private long createdAt;

    @Column(name = "company_id")
    private long companyId;

}
