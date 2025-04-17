package com.backendcore.backendcore.v1.customer.repository;

import com.backendcore.backendcore.v1.customer.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository  extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByToken(String token);
}