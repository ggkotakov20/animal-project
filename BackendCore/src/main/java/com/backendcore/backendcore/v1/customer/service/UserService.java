package com.backendcore.backendcore.v1.customer.service;

import com.backendcore.backendcore.config.Format;
import com.backendcore.backendcore.config.auth.JwtService;
import com.backendcore.backendcore.v1.customer.dto.response.UserResponse;
import com.backendcore.backendcore.v1.customer.models.User;
import com.backendcore.backendcore.v1.customer.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private static JwtService jwtService;

    public static UserResponse getUser(String authorizationHeader) {
        UserResponse res = new UserResponse();
        try {
            User user = jwtService.extractUser(authorizationHeader);
            res.setStatusCode(HttpStatus.OK.value());
            res.setFirstName(user.getFirstName());
            res.setLastName(user.getLastName());
            res.setEmail(user.getEmail());
            res.setDateCreated(Format.formatTimestamp(user.getDateCreated()));
            res.setDateUpdated(Format.formatTimestamp(user.getDateUpdated()));
            res.setLastLogin(Format.formatTimestamp(user.getLastLogin()));
            res.setAccessToken(user.getToken());
        } catch (UsernameNotFoundException e) {
            res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            res.setMessage("Invalid email or password");
        }
        return res;
    }
}
