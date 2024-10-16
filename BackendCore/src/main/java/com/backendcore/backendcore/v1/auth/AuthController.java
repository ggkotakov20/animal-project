package com.backendcore.backendcore.v1.auth;

import com.backendcore.backendcore.v1.dto.front.request.LoginRequest;
import com.backendcore.backendcore.v1.dto.front.request.RegisterRequest;
import com.backendcore.backendcore.v1.dto.front.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "Auth", description = "API for authentication ")
public class AuthController {
    private final AuthService authService;

    @Operation(summary = "To register a new member")
    @PostMapping("/auth/register")
    public ResponseEntity<UserResponse> register(
            @RequestBody RegisterRequest request
    ){
        UserResponse userResponse = authService.registerUser(request);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }

    @Operation(summary = "To login a member ")
    @PostMapping("/auth/login")
    public ResponseEntity<UserResponse> login(
            @RequestBody LoginRequest request
    ){
        UserResponse userResponse = authService.memberLogin(request);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }

    @Operation(summary = "To login a customer ")
    @PostMapping("/admin/auth/login")
    public ResponseEntity<UserResponse> adminLogin(
            @RequestBody LoginRequest request,
            HttpServletRequest httpRequest
    ){
        UserResponse userResponse = authService.adminLogin(request, httpRequest);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }

    @Operation(summary = "To register a new customer")
    @PostMapping("/admin/auth/register")
    public ResponseEntity<UserResponse> adminLogin(
            @RequestBody RegisterRequest request
    ){
        UserResponse userResponse = authService.registerAdmin(request);
        return ResponseEntity.status(userResponse.getStatusCode()).body(userResponse);
    }
}
