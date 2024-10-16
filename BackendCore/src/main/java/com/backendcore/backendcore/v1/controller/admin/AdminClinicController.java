package com.backendcore.backendcore.v1.controller.admin;

import com.backendcore.backendcore.v1.service.ClinicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/clinic")
public class AdminClinicController {
    private final ClinicService clinicService;

    @Operation(summary = "Create a clinic")
    @PostMapping("")
    public ResponseEntity<Map<String, String>> createClinic(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody String name
    ){
        return clinicService.createClinic(authorizationHeader, name);
    }
}
