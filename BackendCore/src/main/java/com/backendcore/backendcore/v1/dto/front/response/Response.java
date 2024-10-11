package com.backendcore.backendcore.v1.dto.front.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)  // Only include non-null values
public class Response {
    private Integer statusCode;  // Use Integer to handle nulls
    private String error;
    private String message;

    public boolean hasStatusCode() {
        return statusCode != null && statusCode != 0;
    }
}
