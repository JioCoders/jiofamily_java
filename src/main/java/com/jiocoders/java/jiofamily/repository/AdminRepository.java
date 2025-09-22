package com.jiocoders.java.jiofamily.repository;

import com.jiocoders.java.jiofamily.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AdminRepository extends JpaRepository<User, Integer> {

    // List<User> findAll();

    List<User> findAllByIsActive(boolean isActive);

    // List<User> findAllByIsActive(boolean isActive, Pageable pageable);

    User findByEmailId(String email);
    // User findById(int id);
}
