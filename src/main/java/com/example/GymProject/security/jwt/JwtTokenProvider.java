package com.example.GymProject.security.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.expiration}")
    private long jwtExpiration;
    @Value("${jwt.secret}")
    private String jwtSecret;
    private final ConcurrentHashMap<String, LocalDateTime> tokenValidityMap = new ConcurrentHashMap<>();


    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date expirationDate = new Date(currentDate.getTime() + jwtExpiration);
        String token = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(username)
                .issuedAt(new Date())
                .expiration(expirationDate)
                .signWith(getSignKey())
                .compact();

        tokenValidityMap.put(token, LocalDateTime.now().plusSeconds(jwtExpiration));
        return token;
    }

    String usernameFromJwt(String token) {
        return Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parser()
                    .verifyWith((SecretKey) getSignKey())
                    .build()
                    .parse(token);

            return tokenValidityMap.containsKey(token) &&
                    tokenValidityMap.get(token).isAfter(LocalDateTime.now());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public void invalidateToken(String token) {
        tokenValidityMap.remove(token);
    }
}
