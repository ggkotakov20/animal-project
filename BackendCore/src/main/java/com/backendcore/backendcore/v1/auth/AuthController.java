package com.backendcore.backendcore.v1.auth;

import com.backendcore.backendcore.v1.dto.front.response.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request
    ){
        UserResponse userResponse = authService.registerUser(request);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody LoginRequest request
    ){
        UserResponse userResponse = authService.memberLogin(request);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }

    @PostMapping("/admin/login")
    public ResponseEntity<UserResponse> adminLogin(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authService.adminLogin(request, httpRequest);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }
}
