package com.jiocoders.java.jiofamily.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

import static com.jiocoders.java.jiofamily.utils.DbConstant.TABLE_COMPANY;

@Data
@Entity
@Table(name = TABLE_COMPANY)
public class Company implements Serializable {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int companyId;

    @Column(name = "name")
    private String companyName;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private long createdAt;

}
