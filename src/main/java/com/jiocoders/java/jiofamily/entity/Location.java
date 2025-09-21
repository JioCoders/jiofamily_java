package com.jiocoders.java.jiofamily.entity;

import jakarta.persistence.*;
import lombok.Data;

import static com.jiocoders.java.jiofamily.utils.DbConstant.TABLE_LOCATION;

@Data
@Entity
@Table(name = TABLE_LOCATION)
public class Location {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int locationId;

    @Column(name = "name")
    private String locationName;

    @Column(name = "latitude")
    private double latitude;
    @Column(name = "longitude")
    private double longitude;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "created_at")
    private long createdAt;

    @Column(name = "company_id")
    private long companyId;

}
