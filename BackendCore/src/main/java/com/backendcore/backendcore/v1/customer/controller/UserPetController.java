package com.backendcore.backendcore.v1.customer.controller;

import com.backendcore.backendcore.v1.customer.dto.request.UserPetRequest;
import com.backendcore.backendcore.v1.customer.dto.response.UserPetResponse;
import com.backendcore.backendcore.v1.customer.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/user/pets")
@Tag(name = "User controller for his pets")
public class UserPetController {
    private final PetService petService;

    @Operation(summary = "Return all pets on user")
    @GetMapping("")
    public ResponseEntity<List<UserPetResponse>> getAllUserPets(@RequestHeader("Authorization") String authorizationHeader) {
        return petService.getAllPets(authorizationHeader);
    }


    @Operation(summary = "Return specific pet by id")
    @GetMapping("/{id}")
    public ResponseEntity<UserPetResponse> getUserPetById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int id
    ) {
        return petService.getPetById(authorizationHeader,id);
    }


    @Operation(summary = "Adding a new pet for user")
    @PostMapping("")
    public ResponseEntity<UserPetResponse> createPet(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserPetRequest request
    ) {
        return petService.createPet(authorizationHeader, request);
    }

    @Operation(summary = "Editing an old pet")
    @PutMapping("")
    public ResponseEntity<UserPetResponse> updatePet(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserPetRequest request
    ) {
        return petService.updatePet(authorizationHeader, request);
    }

    @Operation(summary = "Soft delete a pet")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserPetResponse> deletePet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int id
    ) {
        return petService.deletePet(authorizationHeader, id);
    }

    @Operation(summary = "Recover a deleted pet")
    @PutMapping("/recover/{id}")
    public ResponseEntity<UserPetResponse> recoverPet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int id
    ) {
        return petService.recoverPet(authorizationHeader, id);
    }

}
