package com.capgemini.registration_service.service;

import com.capgemini.registration_service.entity.User;
import com.capgemini.registration_service.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_NewUser_Success() {
        User newUser = new User();
        newUser.setUsername("newuser");
        newUser.setPassword("password");

        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        User result = userService.registerUser(newUser);

        assertNotNull(result);
        assertEquals("newuser", result.getUsername());
    }

    @Test
    void registerUser_DuplicateUsername_ThrowsException() {
        User existingUser = new User();
        existingUser.setUsername("existinguser");

        when(userRepository.findByUsername("existinguser")).thenReturn(Optional.of(existingUser));

        User newUser = new User();
        newUser.setUsername("existinguser");

        assertThrows(RuntimeException.class, () -> userService.registerUser(newUser));
    }
}