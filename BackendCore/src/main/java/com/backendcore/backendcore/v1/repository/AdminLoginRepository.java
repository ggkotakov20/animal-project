package com.backendcore.backendcore.v1.repository;

import com.backendcore.backendcore.v1.models.AdminLogin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminLoginRepository extends JpaRepository<AdminLogin, Integer> {
}
