package com.pm.authservice.util;

// Importing classes related to JWT creation and validation
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.nio.charset.StandardCharsets; // For encoding strings into bytes
import java.security.Key;                 // General interface for encryption keys
import java.util.Base64;                  // For Base64 encoding/decoding
import java.util.Date;                    // For working with date and time

import javax.crypto.SecretKey;            // Specific key type used for HMAC algorithms

// Spring annotations for injecting configuration values and marking this as a Spring component
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

// Marks this class as a Spring-managed bean so it can be auto-injected elsewhere
@Component
public class JwtUtil {

    // Holds the secret key used to sign and validate the JWT
    private final Key secretKey;

    // Constructor that reads the secret key from the application properties file
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        System.out.println("Key" + secret); // Logs the secret for debugging (remove in production)

        // Decodes the Base64-encoded string into bytes using UTF-8 character encoding
        byte[] keyBytes = Base64.getDecoder()
                .decode(secret.getBytes(StandardCharsets.UTF_8));

        // Generates a secure HMAC key from the decoded bytes
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Generates a JWT token with email and role as claims
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email) // Sets the subject of the token (typically the user identifier)
                .claim("role", role) // Adds a custom claim for the user's role
                .issuedAt(new Date()) // Sets the time the token was issued
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Expires in 10 hours
                .signWith(secretKey) // Signs the token with the secret key
                .compact(); // Finalizes and returns the token as a compact string
    }

    // Validates the JWT token; throws an error if the token is invalid
    public void validateToken(String token) {
        try {
            // Parses and verifies the signed token using the same secret key
            Jwts.parser()
                    .verifyWith((SecretKey) secretKey) // Verifies the signature
                    .build()
                    .parseSignedClaims(token); // Parses the claims from the token

        } catch (SignatureException e) {
            // If the signature is invalid, throw a specific exception
            throw new JwtException("Invalid JWT signature");
        } catch (JwtException e) {
            // If any other issue occurs while parsing, throw a general JWT exception
            throw new JwtException("Invalid JWT");
        }
    }
}
