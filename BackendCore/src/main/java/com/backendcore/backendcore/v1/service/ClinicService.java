package com.backendcore.backendcore.v1.service;

import com.backendcore.backendcore.v1.config.auth.JwtService;
import com.backendcore.backendcore.v1.models.Clinic;
import com.backendcore.backendcore.v1.models.User;
import com.backendcore.backendcore.v1.repository.ClinicRepository;
import com.backendcore.backendcore.v1.repository.UserPetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ClinicService {
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private JwtService jwtService;

    public ResponseEntity<Map<String, String>> createClinic(String authorizationHeader, String name) {
        User user = jwtService.extractUser(authorizationHeader);

        Optional<Clinic> optClinic = clinicRepository.findByOwnerId(user.getId());
        if (optClinic.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Clinic already exists");
        }

        Clinic clinic = Clinic.builder()
                .name(name)
                .ownerId(user.getId())
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .build();

        clinicBilling(clinic);

        clinicRepository.save(clinic);
        Map<String, String> response = new HashMap<>();
        response.put("name", clinic.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    private void clinicBilling(Clinic clinic) {
        clinic.setIsPaid(1);
        clinic.setPaymentDate(new Timestamp(System.currentTimeMillis()));
        clinicRepository.save(clinic);
    }
}
