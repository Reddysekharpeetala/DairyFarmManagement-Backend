package com.example.demo.controller;



import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {

        Optional<User> userOptional =
                userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid username or password.");
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body("Invalid username or password.");
        }

        if (!"Active".equalsIgnoreCase(user.getStatus())) {
            return ResponseEntity
                    .badRequest()
                    .body("User account is inactive.");
        }

        LoginResponse loginResponse = new LoginResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );

        return ResponseEntity.ok(loginResponse);
    }
}