package com.backendcore.backendcore.v1.dto.front.response;

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
    private Date dateCreated;
    private Date lastLogin;
    private String tokenType;
    private String accessToken;
}
