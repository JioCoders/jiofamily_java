package com.jiocoders.java.jiofamily.repository;

import com.jiocoders.java.jiofamily.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import static com.jiocoders.java.jiofamily.utils.DbConstant.TABLE_DEPARTMENT;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {

    Department findByDepartmentId(int departmentId);

    @Modifying
    @Query(value = "update " + TABLE_DEPARTMENT + " u set u.is_active=false where u.id=?1", nativeQuery = true)
    void removeDepartment(String departmentId);

}
