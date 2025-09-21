package com.jiocoders.java.jiofamily.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jiocoders.java.jiofamily.entity.Location;
import static com.jiocoders.java.jiofamily.utils.DbConstant.TABLE_LOCATION;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {

    Location findByLocationId(int locationId);

    @Modifying
    @Query(value = "update " + TABLE_LOCATION + " u set u.is_active=false where u.id=?1", nativeQuery = true)
    void removeWarehouse(String id);

}
