package com.capgemini.login_service.service;

import com.capgemini.login_service.entity.User;
import com.capgemini.login_service.repository.UserRepository;
import com.capgemini.login_service.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    void authenticate_ValidCredentials_ReturnsUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPass");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password", "encodedPass")).thenReturn(true);

        User result = authService.authenticate("testuser", "password");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    void authenticate_InvalidPassword_ThrowsException() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("encodedPass");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpass", "encodedPass")).thenReturn(false);

        assertThrows(RuntimeException.class,
                () -> authService.authenticate("testuser", "wrongpass"));
    }
}