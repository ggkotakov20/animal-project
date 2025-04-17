package com.backendcore.backendcore.v1.customer.repository;

import com.backendcore.backendcore.v1.customer.models.ValidToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ValidTokenRepository extends JpaRepository<ValidToken, UUID> {
    List<ValidToken> findByUserId(Integer userId);
    ValidToken findByToken(String token);
}
