package com.capgemini.enrollment_service.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    private JwtAuthenticationFilter filter;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setup() throws Exception {
        filter = new JwtAuthenticationFilter();

        // Inject only jwtUtil dependency since objectMapper is not declared in the filter.
        var jwtUtilField = filter.getClass().getDeclaredField("jwtUtil");
        jwtUtilField.setAccessible(true);
        jwtUtilField.set(filter, jwtUtil);

        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();

        // Clear the Security context for a clean state before each test
        SecurityContextHolder.clearContext();
    }

    @Test
    void validToken_SetsAuthentication() throws ServletException, IOException {
        String validToken = "valid.token.here";
        request.addHeader("Authorization", "Bearer " + validToken);

        // Create a mock claims object
        JsonNode mockClaims = new ObjectMapper()
                .createObjectNode()
                .put("userId", 123)
                .put("role", "STUDENT");

        // Stubbing jwtUtil behavior
        when(jwtUtil.validateJwtToken(validToken)).thenReturn(true);
        when(jwtUtil.getClaims(validToken)).thenReturn(mockClaims);
        when(jwtUtil.getUsernameFromJwtToken(validToken)).thenReturn("testuser");

        // Execute the filter
        filter.doFilter(request, response, filterChain);

        // Verify that the SecurityContext now contains an Authentication object
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        // Verify that the chain continued to process the request
        verify(filterChain).doFilter(request, response);
    }
}
