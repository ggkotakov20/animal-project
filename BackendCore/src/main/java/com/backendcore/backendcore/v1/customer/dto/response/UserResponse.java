package com.backendcore.backendcore.v1.customer.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse extends Response{
    private String firstName;
    private String lastName;
    private String email;
    private String dateCreated;
    private String dateUpdated;
    private String lastLogin;
    private String tokenType;
    private String accessToken;
}
