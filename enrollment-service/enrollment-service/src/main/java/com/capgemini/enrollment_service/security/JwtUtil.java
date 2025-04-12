package com.capgemini.enrollment_service.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Validates token structure and expiration.
    public boolean validateJwtToken(String token) {
        try {
            String[] parts = token.split("\\.");
            // Allow tokens with 2 parts (no signature) or 3 parts
            if (parts.length < 2 || parts.length > 3) {
                logger.error("Invalid JWT format. Expected 2 or 3 parts, got {}", parts.length);
                return false;
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            JsonNode payloadNode = objectMapper.readTree(payloadJson);

            // Convert exp from seconds to milliseconds (if needed)
            long exp = payloadNode.get("exp").asLong() * 1000;
            if (System.currentTimeMillis() >= exp) {
                logger.error("Token is expired.");
                return false;
            }
            return true;
        } catch (Exception e) {
            logger.error("Exception during token validation: ", e);
            return false;
        }
    }

    // Extracts the username (subject) from the token.
    public String getUsernameFromJwtToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return objectMapper.readTree(payloadJson).get("sub").asText();
        } catch (Exception e) {
            logger.error("Exception extracting username from token: ", e);
        }
        return null;
    }

    // Returns the complete claims as a JsonNode.
    public JsonNode getClaims(String token) {
        try {
            String[] parts = token.split("\\.");
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
            return objectMapper.readTree(payloadJson);
        } catch (Exception e) {
            logger.error("Exception extracting claims from token: ", e);
        }
        return null;
    }
}
