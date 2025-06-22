package com.humg.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.humg.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Admin findByUsername(String username);
}
