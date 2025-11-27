package com.jiocoders.java.jiofamily.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwt.secret}")
    private String secretKey;

    @Value("${security.jwt.token.expire-length:604800000}")
    private final long validityInMilliseconds = 604800000; // 30 days

    private static final String KEY_SCOPE = "scope";
    // private SecretKey SIGNING_KEY;

    // JwtTokenProvider() {
    // // String secretString = System.getenv(secretKey);
    // SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    // logger.info("Secret Key: " + secretKey);
    // }
    // @PostConstruct
    // protected void init() {
    // secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    // }

    public String createToken(String username, AuthScope scope) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_SCOPE, scope.value);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);
        SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .setSubject(username)
                .signWith(SIGNING_KEY)
                .compact();
    }

    public String getUsername(String token) {
        SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            // Use the consistent static key for parsing
            return Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException e) {
            // Log the error e.g., logger.error("Invalid JWT token: {}", e.getMessage());
            return null; // or throw a specific exception for the calling service to handle
        }
    }

    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean resolveAndValidate(String bearerToken, AuthScope scopeToVerify) {
        String token = resolveToken(bearerToken);

        if (token == null || token.isEmpty()) {
            return false;
        }

        return validateToken(token, scopeToVerify);
    }

    public String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token, AuthScope scopeToVeify) {
        SecretKey SIGNING_KEY = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));

        try {
            // Use the consistent static key for parsing
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(SIGNING_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Note: Use the same string literal "scope" used in createToken
            String claimScope = claims.get("scope", String.class);

            return scopeToVeify.value.equals(claimScope);

        } catch (JwtException | IllegalArgumentException e) {
            // Log the error
            return false;
        }
    }

}
