package com.example.trainer_workload.security;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class JwtToPrincipalConverter {

    @Value("${jwt.secret}")
    String secretKey;


    public UserPrincipal convert(Jwt<?, ?> jwt) {
        Claims claims = (Claims) jwt.getBody();

        String username = claims.getSubject();
        Collection<? extends GrantedAuthority> authorities = extractAuthoritiesFromClaim(claims);


        return UserPrincipal.builder()
                .username(username)
                .authorities(authorities)
                .build();

    }


    private Collection<? extends GrantedAuthority> extractAuthoritiesFromClaim(Claims claims) {
        List<String> authorityStrings = claims.get("authorities", List.class);

        return authorityStrings != null
                ? authorityStrings.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())
                : List.of(); // Return an empty list if authorities are null
    }
}
