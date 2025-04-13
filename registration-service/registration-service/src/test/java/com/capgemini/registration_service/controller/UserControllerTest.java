package com.capgemini.registration_service.controller;

import com.capgemini.registration_service.entity.Role;
import com.capgemini.registration_service.entity.User;
import com.capgemini.registration_service.repository.UserRepository;
import com.capgemini.registration_service.security.SecurityConfig;
import com.capgemini.registration_service.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import({UserControllerTest.TestConfig.class, SecurityConfig.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class TestConfig {
        @Bean
        @Primary
        UserService userService() {
            return Mockito.mock(UserService.class);
        }

        @Bean
        @Primary
        UserRepository userRepository() {
            return Mockito.mock(UserRepository.class);
        }
    }

    @Test
    void registerUser_Success() throws Exception {
        // Create complete valid user
        User user = new User();
        user.setFullName("John Doe");
        user.setUsername("johndoe");
        user.setEmail("john@example.com");
        user.setPassword("securePassword123");
        user.setRole(Role.STUDENT);

        when(userService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("johndoe"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void getUserByUsername_AdminAccess_Success() throws Exception {
        User adminUser = new User();
        adminUser.setUsername("admin");
        adminUser.setRole(Role.ADMIN);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/register/users/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    void registerUser_MissingFields_ReturnsBadRequest() throws Exception {
        // Create invalid user with missing required fields
        User invalidUser = new User();
        invalidUser.setUsername("johndoe");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest());
    }
}