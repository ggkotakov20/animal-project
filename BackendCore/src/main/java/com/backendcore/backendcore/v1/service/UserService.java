package com.backendcore.backendcore.v1.service;

import com.backendcore.backendcore.v1.config.JwtService;
import com.backendcore.backendcore.v1.dto.front.response.UserResponse;
import com.backendcore.backendcore.v1.models.User;
import com.backendcore.backendcore.v1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;


    public UserResponse getUser(String authorizationHeader) {
        UserResponse res = new UserResponse();
        try {
            User user = jwtService.extractUser(authorizationHeader);
            res.setStatusCode(HttpStatus.OK.value());
            res.setFirstName(user.getFirstName());
            res.setLastName(user.getLastName());
            res.setEmail(user.getEmail());
            res.setDateCreated(user.getDateCreated());
            res.setAccessToken(user.getToken());
        } catch (UsernameNotFoundException e) {
            res.setStatusCode(HttpStatus.UNAUTHORIZED.value());
            res.setMessage("Invalid email or password");
        } catch (Exception e) {
            res.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        return res;
    }
}
