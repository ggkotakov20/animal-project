package com.backendcore.backendcore.v1.customer.repository;

import com.backendcore.backendcore.v1.customer.models.UserPet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserPetRepository extends JpaRepository<UserPet, Integer> {
    Optional<List<UserPet>> findByOwnerId(int ownerId);
    Optional<List<UserPet>> findByClinicId(int clinicId);
}
