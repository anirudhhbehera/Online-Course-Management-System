package com.capgemini.login_service.controller;

import com.capgemini.login_service.dto.LoginRequest;
import com.capgemini.login_service.dto.LoginResponse;
import com.capgemini.login_service.entity.User;
import com.capgemini.login_service.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login")
@RequiredArgsConstructor
public class LoginController {

    private final AuthService authService;

    @PostMapping
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            User user = authService.authenticate(loginRequest.getUsername(), loginRequest.getPassword());
            String token = authService.generateJwtToken(user);
            return ResponseEntity.ok(new LoginResponse(token, user.getUsername(), user.getRole().toString()));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
    }
}
