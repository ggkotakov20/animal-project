package com.backendcore.backendcore.v1.customer.dto.request;

import lombok.Data;

@Data
public class UserPetRequest {
    private Integer id;
    private String name;
    private String animalType;
    private String breed;
    private String birthday;
}