package com.backendcore.backendcore.v1.controller.admin;

import com.backendcore.backendcore.v1.dto.front.response.UserPetResponse;
import com.backendcore.backendcore.v1.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/admin/pets")
@Tag(name = "Admin pets")
public class AdminPetController {
    private final PetService petService;

    @Operation(summary = "Set a default clinic on pet with id {id}")
    @PutMapping("/{id}")
    public ResponseEntity<UserPetResponse> addPetToClinic(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Integer id
    ){
        return petService.addPetToClinic(authorizationHeader, id);
    }

    @Operation(summary = "Pick up any pets that are in the user's clinic")
    @GetMapping("")
    public ResponseEntity<List<UserPetResponse>> getPetsFromUserClinic(
            @RequestHeader("Authorization") String authorizationHeader
    ){
        return petService.getPetsFromAdminsClinic(authorizationHeader);
    }
}
