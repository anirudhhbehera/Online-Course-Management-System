package com.capgemini.login_service.service;

import com.capgemini.login_service.entity.User;
import com.capgemini.login_service.repository.UserRepository;
import com.capgemini.login_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public User authenticate(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return user;
    }

    // Updated to generate token using additional user details
    public String generateJwtToken(User user) {
        return jwtUtil.generateToken(user.getUsername(), user.getId(), user.getRole().toString());
    }
}
