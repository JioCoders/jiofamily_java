package com.jiocoders.java.jiofamily.security;

import java.util.Base64;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.jiocoders.java.jiofamily.controller.AdminController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {    
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    private String secretKey = "JWT_SECRET";

    @Value("${security.jwt.token.expire-length:604800000}")
    private long validityInMilliseconds = 604800000; // 30 days

    private static final String KEY_SCOPE = "scope";

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    public String createToken(String username, AuthScope scope) {

        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_SCOPE, scope.value);

        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String secret = System.getenv(secretKey);
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        logger.info("Secret Key: " + secret);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .setSubject(secret)
                .signWith(key)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
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
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
            String claimScope = claims.get(KEY_SCOPE).toString();

            if (scopeToVeify.value.equals(claimScope)) {
                return true;
            }
            return false;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

}
