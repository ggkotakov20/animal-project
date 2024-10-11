package com.backendcore.backendcore.v1.service;

import com.backendcore.backendcore.v1.config.JwtService;
import com.backendcore.backendcore.v1.dto.front.request.UserPetRequest;
import com.backendcore.backendcore.v1.dto.front.response.UserPetResponse;
import com.backendcore.backendcore.v1.models.User;
import com.backendcore.backendcore.v1.models.UserPet;
import com.backendcore.backendcore.v1.repository.UserPetRepository;
import com.backendcore.backendcore.v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PetService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserPetRepository userPetRepository;
    @Autowired
    private JwtService jwtService;

    private static final DateTimeFormatter BIRTHDAY_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private UserPetResponse mapToResponse(UserPet pet)  {
        UserPetResponse response = new UserPetResponse();
        response.setId(pet.getId());
        response.setName(pet.getName());
        response.setBreed(pet.getBreed());
        response.setAnimalType(pet.getAnimalType());
        response.setDateCreated(pet.getDateCreated().toString());
        response.setBirthday(pet.getBirthday() != null ? pet.getBirthday().toString() : null);
        return response;
    }

    private UserPet mapToUserPet(UserPetRequest request, int ownerId) {
        LocalDate birthday;
        try {
            birthday = LocalDate.parse(request.getBirthday(), BIRTHDAY_FORMATTER);  // Parse with custom formatter
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect date format. Please use dd-MM-yyyy.");
        }

        UserPet pet = UserPet.builder()
                .id(request.getId())
                .ownerId(ownerId)
                .name(request.getName())
                .animalType(request.getAnimalType())
                .breed(request.getBreed())
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .birthday(birthday)
                .active(1)
                .build();

        return pet;
    }

    public ResponseEntity<List<UserPetResponse>> getAllUserPets(String authorizationHeader) {
        try {
            User user = jwtService.extractUser(authorizationHeader);

            // Fetch pets using repository and handle Optional
            Optional<List<UserPet>> petsOpt = userPetRepository.findByOwnerId(user.getId());
            List<UserPet> pets = petsOpt.orElse(new ArrayList<>());

            // Convert UserPet list to UserPetResponse list
            List<UserPetResponse> response = pets.stream()
                    .map(this::mapToResponse)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            // Log the exception and its message for debugging
            System.out.println("Exception occurred: " + e.getMessage());
            e.printStackTrace(); // Optional: print full stack trace for better insight
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while fetching user pets");
        }
    }

    public ResponseEntity<UserPetResponse> getUserPetById(String authorizationHeader, int id) {
        User user = jwtService.extractUser(authorizationHeader);

        Optional<UserPet> optionalUserPet = userPetRepository.findById(id);

        if (optionalUserPet.isEmpty() || !optionalUserPet.get().getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }

        return ResponseEntity.ok(mapToResponse(optionalUserPet.get()));
    }

    public ResponseEntity<UserPetResponse> createPet(String authorizationHeader, UserPetRequest request) {
        User user = jwtService.extractUser(authorizationHeader);

        if(request.getName() == null || request.getBreed() == null || request.getAnimalType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        request.setId(null);
        UserPet pet = mapToUserPet(request, user.getId());

        userPetRepository.save(pet);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(pet));
    }

    public ResponseEntity<UserPetResponse> updatePet(String authorizationHeader, UserPetRequest request) {
        User user = jwtService.extractUser(authorizationHeader);

        if(request.getName() == null || request.getBreed() == null || request.getAnimalType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        Optional<UserPet> optionalUserPet = userPetRepository.findById(request.getId());

        if (optionalUserPet.isEmpty() || !optionalUserPet.get().getOwnerId().equals(user.getId()) || optionalUserPet.get().getActive().equals(0)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }


        UserPet pet = mapToUserPet(request, user.getId());

        userPetRepository.save(pet);

        return ResponseEntity.status(HttpStatus.OK).body(mapToResponse(pet));
    }

    public ResponseEntity<UserPetResponse> deletePet(String authorizationHeader, int id) {
        User user = jwtService.extractUser(authorizationHeader);

        Optional<UserPet> optionalUserPet = userPetRepository.findById(id);

        if (optionalUserPet.isEmpty() || !optionalUserPet.get().getOwnerId().equals(user.getId()) || optionalUserPet.get().getActive() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }

        UserPet pet = optionalUserPet.get();
        pet.setActive(0);
        userPetRepository.save(pet);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<UserPetResponse> recoverPet(String authorizationHeader, int id) {
        User user = jwtService.extractUser(authorizationHeader);

        Optional<UserPet> optionalUserPet = userPetRepository.findById(id);

        if (optionalUserPet.isEmpty() || !optionalUserPet.get().getOwnerId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }

        UserPet pet = optionalUserPet.get();
        pet.setActive(1);
        userPetRepository.save(pet);

        return ResponseEntity.ok(mapToResponse(pet));
    }
}
