package com.backendcore.backendcore.v1.repository;

import com.backendcore.backendcore.v1.models.PaymentsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsLogRepository extends JpaRepository<PaymentsLog, Integer> {
}
