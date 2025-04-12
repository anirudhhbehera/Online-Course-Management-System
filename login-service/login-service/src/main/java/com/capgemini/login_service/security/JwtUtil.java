package com.capgemini.login_service.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String jwtSecret;
    // Generate a secure key for HS512
//    private final SecretKey jwtSecretKey = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private final long jwtExpirationMs = 86400000; // 24 hours

//    public String generateToken(String username) {
//        return Jwts.builder()
//                .setSubject(username)
//                .setIssuedAt(new Date())
//                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
//                .signWith(jwtSecretKey, SignatureAlgorithm.HS512)
//                .compact();
//    }
public String generateToken(String username, Long userId, String role) {
    long expirationTime = System.currentTimeMillis() + jwtExpirationMs;

    // Header with alg set to "none" for no signature.
    String headerJson = "{\"alg\":\"none\",\"typ\":\"JWT\"}";

    // Extended payload with additional claims: sub, userId, role, and exp.
    String payloadJson = "{\"sub\":\"" + username + "\","
            + " \"userId\":" + userId + ","
            + " \"role\":\"" + role + "\","
            + " \"exp\":" + expirationTime + "}";

    // Base64 URL encode header and payload without padding.
    String encodedHeader = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(headerJson.getBytes(StandardCharsets.UTF_8));
    String encodedPayload = Base64.getUrlEncoder().withoutPadding()
            .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

    // Return the token; signature part is empty since we're not signing.
    return encodedHeader + "." + encodedPayload + ".";
}

    // If you still need the basic method only with username, you can overload as before.
    public String generateToken(String username) {
        return generateToken(username, 0L, "");
    }

}
