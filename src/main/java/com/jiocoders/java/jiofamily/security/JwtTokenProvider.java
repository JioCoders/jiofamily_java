package com.jiocoders.java.jiofamily.security;

import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {

    private String secretKey = "Jiocoders";

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

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
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
