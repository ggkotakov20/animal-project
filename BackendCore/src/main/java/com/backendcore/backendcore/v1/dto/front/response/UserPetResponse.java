package com.backendcore.backendcore.v1.dto.front.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPetResponse {
    public Integer id;
    public String name;
    public String animalType;
    public String breed;
    public String dateCreated;
    public String birthday;
}
