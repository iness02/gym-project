package com.example.trainer_workload.security;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


@Component
public class JwtDecoder {

    @Value("${jwt.secret}")
    String secretKey;

    public Jwt<?, ?> decode(String token){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        SecretKey secretKey1= Keys.hmacShaKeyFor(keyBytes);
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(secretKey1)
                .build();
        return jwtParser.parse(token);
    }
}