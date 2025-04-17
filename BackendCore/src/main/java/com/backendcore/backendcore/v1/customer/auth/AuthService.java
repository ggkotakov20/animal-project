package com.backendcore.backendcore.v1.customer.auth;

import com.backendcore.backendcore.config.auth.JwtService;
import com.backendcore.backendcore.v1.customer.dto.response.UserResponse;
import com.backendcore.backendcore.v1.customer.exception.NotFoundException;
import com.backendcore.backendcore.v1.customer.models.User;
import com.backendcore.backendcore.v1.customer.models.ValidToken;
import com.backendcore.backendcore.v1.customer.repository.UserRepository;
import com.backendcore.backendcore.v1.customer.repository.ValidTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final AuthenticationManager manager;
    @Autowired
    private ValidTokenRepository validTokenRepository;

    public UserResponse register(RegisterRequest request, HttpServletRequest httpServletRequest) {
        UserResponse res = new UserResponse();
        if (request.getFirstName() == null || request.getFirstName().isEmpty() ||
                request.getLastName() == null || request.getLastName().isEmpty() ||
                request.getPassword() == null || request.getPassword().isEmpty() ||
                request.getEmail() == null || request.getEmail().isEmpty()) {
            res.setStatusCode(400);
            res.setMessage("Some of the fields is empty");
            return res;
        }
        if (repository.findByEmail(request.getEmail()) != null) {
            res.setMessage("The email address is already in use. Please choose a different one.");
            res.setStatusCode(409);
            return res;
        }
        request.setEmail(request.getEmail().toLowerCase());

        var user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .userLevel(1)
                .password(encoder.encode(request.getPassword()))
                .dateCreated(new Timestamp(System.currentTimeMillis()))
                .lastLogin(new Timestamp(System.currentTimeMillis()))
                .status("active")
                .build();

        repository.save(user);



        String jwtToken = jwtService.generateToken(user);
        user.setToken(jwtToken);
        user.setLastLogin(new Timestamp(System.currentTimeMillis()));

        jwtToken = jwtService.generateNoExpiredToken(user);

        repository.save(user);

        String ipAddress = httpServletRequest.getRemoteAddr();
        String deviceName = httpServletRequest.getHeader("Device-Name");
        String deviceModel = httpServletRequest.getHeader("Device-Model");
        ValidToken validToken = new ValidToken(user.getId());

        if(deviceName != null && !deviceName.isEmpty()){
            validToken.setName(deviceName);
        }
        if(deviceModel != null && !deviceModel.isEmpty()){
            validToken.setModel(deviceModel);
        }
        validToken.setToken(jwtToken);
        validToken.setIp(ipAddress);
        validToken.setDate(new Timestamp(System.currentTimeMillis()));
        validTokenRepository.save(validToken);

        res.setFirstName(user.getFirstName());
        res.setLastName(user.getLastName());
        res.setEmail(user.getEmail());
        res.setTokenType("Bearer");
        res.setAccessToken(jwtToken);

        res.setMessage("User Saved successfully");
        res.setStatusCode(200);

        return res;
    }

    public UserResponse login(LoginRequest request, HttpServletRequest httpServletRequest) {
        UserResponse res = new UserResponse();

        try {

            if (request.getPassword() == null || request.getPassword().isEmpty() ||
                    request.getEmail() == null || request.getEmail().isEmpty()) {
                res.setStatusCode(400);
                res.setMessage("Some field is empty");
                return res;
            }

            var user = repository.findByEmail(request.getEmail());

            if (user == null) {
                throw new NotFoundException("Invalid email or password");
            }

            manager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Generate JWT token
            String jwtToken = jwtService.generateNoExpiredToken(user);
            user.setToken(jwtToken);
            user.setLastLogin(new Timestamp(System.currentTimeMillis()));
            repository.save(user);

            String ipAddress = httpServletRequest.getRemoteAddr();
            String deviceName = httpServletRequest.getHeader("Device-Name");
            String deviceModel = httpServletRequest.getHeader("Device-Model");

            ValidToken validToken = new ValidToken(user.getId());

            if(deviceName != null && !deviceName.isEmpty()){
                validToken.setName(deviceName);
            }
            if(deviceModel != null && !deviceModel.isEmpty()){
                validToken.setModel(deviceModel);
            }
            validToken.setToken(jwtToken);
            validToken.setIp(ipAddress);
            validToken.setDate(new Timestamp(System.currentTimeMillis()));
            validTokenRepository.save(validToken);

            res.setFirstName(user.getFirstName());
            res.setLastName(user.getLastName());
            res.setEmail(user.getEmail());
            res.setTokenType("Bearer");
            res.setAccessToken(jwtToken);
            res.setStatusCode(200);
            res.setMessage("User login successful");

            return res;
        } catch (NotFoundException | org.springframework.security.core.AuthenticationException e) {
            res.setStatusCode(401);
            res.setMessage("Invalid email or password");
            return res;
        }
    }
//    private UserResponse login(LoginRequest request, boolean isAdmin, HttpServletRequest httpRequest) {
//        UserResponse res = new UserResponse();
//        AdminLogin adminLogin = new AdminLogin();
//
//        try {
//            // Log login attempt details for admin
//            if (isAdmin) {
//                adminLogin.setIp(getServerIp());
//                adminLogin.setUsername(request.getEmail());
//                adminLogin.setBrowser(httpRequest.getHeader("User-Agent"));
//                adminLogin.setDate(new Timestamp(System.currentTimeMillis()));
//                adminLogin.setSuccessfully(0);
//                adminLogin.setBanned(0);
//            }
//
//            if (request.getPassword() == null || request.getPassword().isEmpty() ||
//                    request.getEmail() == null || request.getEmail().isEmpty()) {
//                res.setStatusCode(400);
//                res.setMessage("Some field is empty");
//                return res;
//            }
//
//            var user = repository.findByEmail(request.getEmail());
//            adminLogin.setClinicId(user.getClinicId());
//            if (user == null || (isAdmin && (user.getUserLevel() != 99 && user.getUserLevel() != 100))) {
//                res.setStatusCode(user == null ? 401 : 403);
//                res.setMessage(user == null ? "Invalid email or password" : "You are not authorized to access this resource");
//                if (isAdmin) {
//                    adminLoginRepository.save(adminLogin); // Save failed login attempt
//                }
//                return res;
//            }
//
//            manager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getEmail(),
//                            request.getPassword()
//                    )
//            );
//
//            // Generate JWT token
//            String jwtToken = isAdmin ? jwtService.generateToken(user) : jwtService.generateNoExpiredToken(user);
//            user.setToken(jwtToken);
//            user.setLastLogin(new Timestamp(System.currentTimeMillis()));
//            repository.save(user);
//
//            // Mark the admin login as successful
//            if (isAdmin) {
//                adminLogin.setSuccessfully(1);
//                adminLoginRepository.save(adminLogin);
//            }
//
//            res.setFirstName(user.getFirstName());
//            res.setLastName(user.getLastName());
//            res.setEmail(user.getEmail());
//            res.setTokenType("Bearer");
//            res.setAccessToken(jwtToken);
//            res.setStatusCode(200);
//            res.setMessage(isAdmin ? "Admin login successful" : "User login successful");
//
//            return res;
//        } catch (NotFoundException | org.springframework.security.core.AuthenticationException e) {
//            res.setStatusCode(401);
//            res.setMessage("Invalid email or password");
//            if (isAdmin) {
//                adminLoginRepository.save(adminLogin); // Save failed login attempt
//            }
//            return res;
//        } catch (Exception e) {
//            res.setStatusCode(500);
//            res.setMessage("Something went wrong. Contact the administrator");
//            return res;
//        }
//    }
//    private String getServerIp() {
//        String publicIpUrl = "http://api.ipify.org";
//        try {
//            URL url = new URL(publicIpUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
//                StringBuilder response = new StringBuilder();
//                String line;
//                while ((line = in.readLine()) != null) {
//                    response.append(line);
//                }
//                return response.toString(); // This will be the public IP address
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Unknown IP";
//        }
//    }
}
