package com.capgemini.enrollment_service.security;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        } else {
            logger.debug("No Authorization header found or it doesn't start with Bearer");
        }

        if (token != null) {
            boolean isValid = jwtUtil.validateJwtToken(token);
            logger.debug("Is token valid? {}", isValid);

            if (isValid) {
                String username = jwtUtil.getUsernameFromJwtToken(token);
                JsonNode claims = jwtUtil.getClaims(token);
                Long userId = claims.has("userId") ? claims.get("userId").asLong() : 0L;
                String role = claims.has("role") ? claims.get("role").asText() : "USER";

                logger.debug("Extracted claims - userId: {}, role: {}", userId, role);

                UserPrincipal userPrincipal = new UserPrincipal(userId, username, role);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                logger.debug("Authentication set in SecurityContextHolder: {}", authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}
