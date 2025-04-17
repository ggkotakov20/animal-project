package com.backendcore.backendcore.v1.customer.repository;

import com.backendcore.backendcore.v1.customer.models.Clinic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClinicRepository extends JpaRepository<Clinic, Integer> {
    Optional<Clinic> findByOwnerId(Integer ownerId);
}
