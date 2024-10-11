package com.backendcore.backendcore.v1.controller.front;

import com.backendcore.backendcore.v1.dto.front.response.UserResponse;
import com.backendcore.backendcore.v1.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<UserResponse> getUser(
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        UserResponse userResponse = userService.getUser(authorizationHeader);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }

}
