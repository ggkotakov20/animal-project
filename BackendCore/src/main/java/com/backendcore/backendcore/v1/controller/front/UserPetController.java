package com.backendcore.backendcore.v1.controller.front;

import com.backendcore.backendcore.v1.dto.front.request.UserPetRequest;
import com.backendcore.backendcore.v1.dto.front.response.UserPetResponse;
import com.backendcore.backendcore.v1.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user/pets")
public class UserPetController {
    private final PetService petService;

    @GetMapping("")
    public ResponseEntity<List<UserPetResponse>> getAllUserPets(@RequestHeader("Authorization") String authorizationHeader) {
        return petService.getAllUserPets(authorizationHeader);
    }
    @GetMapping("/{id}")
    public ResponseEntity<UserPetResponse> getUserPetById(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int id
    ) {
        return petService.getUserPetById(authorizationHeader,id);
    }
    @PostMapping("")
    public ResponseEntity<UserPetResponse> createPet(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserPetRequest request
    ) {
        return petService.createPet(authorizationHeader, request);
    }
    @PutMapping("")
    public ResponseEntity<UserPetResponse> updatePet(
            @RequestHeader("Authorization") String authorizationHeader,
            @RequestBody UserPetRequest request
    ) {
        return petService.updatePet(authorizationHeader, request);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<UserPetResponse> deletePet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int id
    ) {
        return petService.deletePet(authorizationHeader, id);
    }
    @PutMapping("/recover/{id}")
    public ResponseEntity<UserPetResponse> recoverPet(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable int id
    ) {
        return petService.recoverPet(authorizationHeader, id);
    }

}
