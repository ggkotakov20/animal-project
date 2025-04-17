package com.backendcore.backendcore.v1.customer.service;

import com.backendcore.backendcore.config.Format;
import com.backendcore.backendcore.config.auth.JwtService;
import com.backendcore.backendcore.v1.customer.dto.request.UserPetRequest;
import com.backendcore.backendcore.v1.customer.dto.response.UserPetResponse;
import com.backendcore.backendcore.v1.customer.exception.NotFoundException;
import com.backendcore.backendcore.v1.customer.models.Clinic;
import com.backendcore.backendcore.v1.customer.models.User;
import com.backendcore.backendcore.v1.customer.models.UserPet;
import com.backendcore.backendcore.v1.customer.repository.ClinicRepository;
import com.backendcore.backendcore.v1.customer.repository.UserPetRepository;
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
import java.util.stream.Collectors;

@Service
public class PetService {
    @Autowired
    private UserPetRepository userPetRepository;
    @Autowired
    private ClinicRepository clinicRepository;
    @Autowired
    private JwtService jwtService;


    private UserPetResponse mapToResponse(UserPet pet) {
        UserPetResponse response = new UserPetResponse();

        if (pet.getClinicId() == null) {
            response.setClinic("");
        } else {
            Optional<Clinic> optClinic = clinicRepository.findById(pet.getClinicId());
            if (optClinic.isPresent()) {
                response.setClinic(optClinic.get().getName());
            } else {
                response.setClinic("");
            }
        }

        response.setId(pet.getId());
        response.setName(pet.getName());
        response.setBreed(pet.getBreed());
        response.setAnimalType(pet.getAnimalType());

        // Format dateCreated
        if (pet.getDateCreated() != null) {
            response.setDateCreated(Format.formatTimestamp(pet.getDateCreated()));
        } else {
            response.setDateCreated(null);
        }

        response.setBirthday(pet.getBirthday() != null ? pet.getBirthday().format(Format.BIRTHDAY_FORMATTER) : null);

        return response;
    }

    private UserPet mapToUserPet(UserPetRequest request, int ownerId) {
        LocalDate birthday;
        try {
            birthday = LocalDate.parse(request.getBirthday(), Format.BIRTHDAY_FORMATTER);  // Parse with custom formatter
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect date format. Please use dd-MM-yyyy.");
        }

        UserPet pet;

        if (request.getId() == null || request.getId() == 0) {
            // Creating a new UserPet
            pet = UserPet.builder()
                    .ownerId(ownerId)
                    .name(request.getName())
                    .animalType(request.getAnimalType())
                    .breed(request.getBreed())
                    .dateCreated(new Timestamp(System.currentTimeMillis())) // Set dateCreated only for new pets
                    .birthday(birthday)
                    .active(1)
                    .build();
        } else {
            // Updating an existing UserPet
            Optional<UserPet> optionalPet = userPetRepository.findById(request.getId());
            if (optionalPet.isEmpty()) {
                throw new NotFoundException("Pet not found");
            }
            pet = optionalPet.get();

            // Update only the fields that need to be changed
            pet.setName(request.getName());
            pet.setAnimalType(request.getAnimalType());
            pet.setBreed(request.getBreed());
            pet.setBirthday(birthday);
        }

        return pet;
    }

    public ResponseEntity<List<UserPetResponse>> getAllPets(String authorizationHeader) {
        User user = jwtService.extractUser(authorizationHeader);

        // Fetch pets using repository and handle Optional
        Optional<List<UserPet>> petsOpt = userPetRepository.findByOwnerId(user.getId());
        List<UserPet> pets = petsOpt.orElse(new ArrayList<>());

        // Convert UserPet list to UserPetResponse list
        List<UserPetResponse> response = pets.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<UserPetResponse> getPetById(String authorizationHeader, int id) {
        User user = jwtService.extractUser(authorizationHeader);

        Optional<UserPet> optionalUserPet = userPetRepository.findById(id);

        if (optionalUserPet.isEmpty() || !optionalUserPet.get().getOwnerId().equals(user.getId())) {
            throw new NotFoundException("Pet not found");
        }

        return ResponseEntity.ok(mapToResponse(optionalUserPet.get()));
    }

    public ResponseEntity<UserPetResponse> createPet(String authorizationHeader, UserPetRequest request) {
        User user = jwtService.extractUser(authorizationHeader);

        if (request.getName() == null || request.getBreed() == null || request.getAnimalType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        request.setId(null); // Ensure id is null for new pets
        UserPet pet = mapToUserPet(request, user.getId());

        userPetRepository.save(pet);

        return ResponseEntity.status(HttpStatus.CREATED).body(mapToResponse(pet));
    }

    public ResponseEntity<UserPetResponse> updatePet(String authorizationHeader, UserPetRequest request) {
        User user = jwtService.extractUser(authorizationHeader);

        if (request.getName() == null || request.getBreed() == null || request.getAnimalType() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request");
        }

        Optional<UserPet> optionalUserPet = userPetRepository.findById(request.getId());

        if (optionalUserPet.isEmpty() || !optionalUserPet.get().getOwnerId().equals(user.getId()) || optionalUserPet.get().getActive().equals(0)) {
            throw new NotFoundException("Pet not found");
        }

        UserPet pet = mapToUserPet(request, user.getId()); // This will handle both creation and update logic

        userPetRepository.save(pet);

        return ResponseEntity.status(HttpStatus.OK).body(mapToResponse(pet));
    }

    public ResponseEntity<UserPetResponse> deletePet(String authorizationHeader, int id) {
        User user = jwtService.extractUser(authorizationHeader);

        Optional<UserPet> optionalUserPet = userPetRepository.findById(id);

        if (optionalUserPet.isEmpty() || !optionalUserPet.get().getOwnerId().equals(user.getId()) || optionalUserPet.get().getActive() == 0) {
            throw new NotFoundException("Pet not found");
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
            throw new NotFoundException("Pet not found");
        }

        UserPet pet = optionalUserPet.get();
        pet.setActive(1);
        userPetRepository.save(pet);

        return ResponseEntity.ok(mapToResponse(pet));
    }
}
