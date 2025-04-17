package com.backendcore.backendcore.v1.customer.controller;

import com.backendcore.backendcore.v1.customer.dto.response.UserResponse;
import com.backendcore.backendcore.v1.customer.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/customer/user")
public class UserController {

    @GetMapping("")
    public ResponseEntity<UserResponse> getUser(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        UserResponse userResponse = UserService.getUser(authorizationHeader);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }

}
